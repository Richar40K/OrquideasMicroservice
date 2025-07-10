package com.orquideas.microservice_payment.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.orquideas.microservice_payment.DTO.CrearPagoViajeDTO;
import com.orquideas.microservice_payment.DTO.PagoRespuestaDTO;
import com.orquideas.microservice_payment.DTO.UserDTO;
import com.orquideas.microservice_payment.DTO.ViajesDTO;
import com.orquideas.microservice_payment.client.UserClient;
import com.orquideas.microservice_payment.client.ViajesClient;
import com.orquideas.microservice_payment.entities.Pago;
import com.orquideas.microservice_payment.enums.PagoEstado;
import com.orquideas.microservice_payment.enums.PagoTipo;
import com.orquideas.microservice_payment.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PagoServiceImpl implements IPagoService
{
    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private UserClient userClient;

    @Autowired
    private ViajesClient viajesClient;

    @Value("${mercadopago.access-token}")
    private String mpAccessToken;


    @Override
    @Transactional
    public PagoRespuestaDTO iniciarPagoViaje(CrearPagoViajeDTO dto) throws Exception {
        UserDTO user = userClient.getUserById(dto.getUserId());
        if (user == null) throw new RuntimeException("Usuario no existe");

        // Validar viaje
        ViajesDTO viaje = viajesClient.getViajesById(dto.getViajeId());
        if (viaje == null) throw new RuntimeException("Viaje no existe");

        Double monto = viaje.getPrecio(); // <- PRECIO OFICIAL

        // (Opcional) Validar asiento...

        // Crear objeto Pago
        Pago pago = new Pago();
        pago.setTipo(PagoTipo.VIAJE);
        pago.setUserId(dto.getUserId());
        pago.setViajeId(dto.getViajeId());
        pago.setAsiento(dto.getAsiento());
        pago.setMonto(monto); // Usa solo el precio del viaje
        pago.setEstado(PagoEstado.PENDIENTE);
        pago.setFecha(LocalDateTime.now());
        pago.setDetalles(dto.getDetalles());

        // --------- INTEGRACIÓN MERCADO PAGO (nuevo SDK) ----------
        // 1. Inicializar configuración global
        MercadoPagoConfig.setAccessToken(mpAccessToken);

        // 2. Crear el item de la preferencia
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title("Boleto de Viaje")
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(monto)) // Usa solo el precio oficial
                .build();

        // 3. Crear el request de preferencia
        PreferenceRequest request = PreferenceRequest.builder()
                .items(List.of(itemRequest))
                .build();

        // 4. Crear el cliente y generar la preferencia
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(request);

        // ----------------------------------------------------------

        pago.setMyPaymentId(preference.getId()); // Guarda el ID de la preferencia
        pago = pagoRepository.save(pago);

        // Mapea el pago al DTO de respuesta
        return toDto(pago, preference.getInitPoint());
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PagoRespuestaDTO> findById(Long id) {
        return pagoRepository.findById(id)
                .map(p -> toDto(p, null));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoRespuestaDTO> findAll() {
        Iterable<Pago> iterable = pagoRepository.findAll();
        List<PagoRespuestaDTO> dtos = new ArrayList<>();
        for (Pago pago : iterable) {
            // Sincroniza estado con Mercado Pago solo si tiene mpPaymentId
            if (pago.getMyPaymentId() != null) {
                try {
                    MercadoPagoConfig.setAccessToken(mpAccessToken);
                    com.mercadopago.client.payment.PaymentClient client = new com.mercadopago.client.payment.PaymentClient();
                    com.mercadopago.resources.payment.Payment payment = client.get(Long.valueOf(pago.getMyPaymentId()));

                    String status = payment.getStatus(); // "approved", "rejected", etc.

                    PagoEstado nuevoEstado;
                    switch (status) {
                        case "approved":
                            nuevoEstado = PagoEstado.APROBADO;
                            break;
                        case "rejected":
                            nuevoEstado = PagoEstado.RECHAZADO;
                            break;
                        case "in_process":
                            nuevoEstado = PagoEstado.EN_PROCESO;
                            break;
                        default:
                            nuevoEstado = PagoEstado.PENDIENTE;
                            break;
                    }

                    if (!nuevoEstado.equals(pago.getEstado())) {
                        pago.setEstado(nuevoEstado);
                        pagoRepository.save(pago);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dtos.add(toDto(pago, null));
        }
        return dtos;
    }

    @Override
    @Transactional
    public PagoRespuestaDTO actualizarEstado(Long id, PagoEstado nuevoEstado) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no existe"));
        pago.setEstado(nuevoEstado);
        pagoRepository.save(pago);
        return toDto(pago, null);
    }

    @Override
    @Transactional
    public Optional<PagoRespuestaDTO> editarPago(Long id, String nuevosDetalles) {
        return pagoRepository.findById(id)
                .map(p -> {
                    p.setDetalles(nuevosDetalles);
                    pagoRepository.save(p);
                    return toDto(p, null);
                });
    }
    private PagoRespuestaDTO toDto(Pago pago, String mpInitPoint) {
        PagoRespuestaDTO dto = new PagoRespuestaDTO();
        dto.setId(pago.getId());
        dto.setTipo(pago.getTipo());
        dto.setUserId(pago.getUserId());
        dto.setViajeId(pago.getViajeId());
        dto.setAsiento(pago.getAsiento());
        dto.setMonto(pago.getMonto());
        dto.setEstado(pago.getEstado());
        dto.setMpPaymentId(pago.getMyPaymentId());
        dto.setFecha(pago.getFecha());
        dto.setDetalles(pago.getDetalles());
        dto.setMpInitPoint(mpInitPoint);
        return dto;
    }
}
