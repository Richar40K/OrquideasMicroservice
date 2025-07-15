package com.orquideas.microservice_payment.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.resources.payment.Payment;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PagoServiceImpl implements IPagoService
{
    private static final Logger log = LoggerFactory.getLogger(PagoServiceImpl.class);

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

        ViajesDTO viaje = viajesClient.getViajesById(dto.getViajeId());
        if (viaje == null) throw new RuntimeException("Viaje no existe");

        Double monto = viaje.getPrecio();

        Pago pago = new Pago();
        pago.setTipo(PagoTipo.VIAJE);
        pago.setUserId(dto.getUserId());
        pago.setViajeId(dto.getViajeId());
        pago.setAsiento(dto.getAsiento());
        pago.setMonto(monto);
        pago.setEstado(PagoEstado.PENDIENTE);
        pago.setFecha(LocalDateTime.now());
        pago.setDetalles("Boleto para el asiento " + dto.getAsiento());

        // Guardar primero para obtener el ID
        pago = pagoRepository.save(pago);

        // Mercado Pago: Crear preferencia
        MercadoPagoConfig.setAccessToken(mpAccessToken);

        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title("Boleto de Viaje - Asiento" + dto.getAsiento())
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(monto))
                .build();

        PreferenceRequest request = PreferenceRequest.builder()
                .items(List.of(itemRequest))
                .externalReference(pago.getId().toString()) // Usar el ID local como referencia
                .expires(true)
                .expirationDateFrom(ZonedDateTime.now().toOffsetDateTime())
                .expirationDateTo(ZonedDateTime.now().plusMinutes(10).toOffsetDateTime())
                .externalReference(String.valueOf(pago.getId()))
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(request);

        pago.setMpPreferenceId(preference.getId());
        pagoRepository.save(pago); // Actualizar con el preferenceId

        return toDto(pago, preference.getInitPoint());
    }

    // 2. Listar pagos (si tienes paymentId, sincroniza estado en MP)
    @Override
    @Transactional(readOnly = true)
    public List<PagoRespuestaDTO> findAll() {
        Iterable<Pago> iterable = pagoRepository.findAll();
        List<PagoRespuestaDTO> dtos = new ArrayList<>();
        for (Pago pago : iterable) {
            if (pago.getMpPaymentId() != null) {
                try {
                    MercadoPagoConfig.setAccessToken(mpAccessToken);
                    PaymentClient client = new PaymentClient();
                    Payment payment = client.get(pago.getMpPaymentId());

                    String status = payment.getStatus();
                    // ... (actualiza estado como antes)
                } catch (MPApiException mpEx) {
                    System.err.println("No se pudo obtener el pago con id " + pago.getMpPaymentId() + ": " + mpEx.getApiResponse().getContent());
                    // Opcional: pago.setEstado(PagoEstado.PENDIENTE);
                    // pagoRepository.save(pago);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            dtos.add(toDto(pago, null));
        }
        return dtos;
    }

    // 3. Buscar pago por ID
    @Override
    @Transactional(readOnly = true)
    public Optional<PagoRespuestaDTO> findById(Long id) {
        return pagoRepository.findById(id)
                .map(p -> toDto(p, null));
    }

    // 4. Actualizar estado manualmente (opcional)
    @Override
    @Transactional
    public PagoRespuestaDTO actualizarEstado(Long id, PagoEstado nuevoEstado) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no existe"));
        pago.setEstado(nuevoEstado);
        pagoRepository.save(pago);
        return toDto(pago, null);
    }

    // 5. Editar detalles (opcional)
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

    // 6. Procesar webhook de Mercado Pago (guarda paymentId y actualiza estado)
    @Override
    @Transactional
    public void procesarWebhookMercadoPago(Map<String, Object> payload) {
        try {
            // Imprime el payload completo para debug
            System.out.println("Payload recibido de MercadoPago: " + payload);

            String type = (String) payload.get("type");
            Map<String, Object> data = (Map<String, Object>) payload.get("data");

            // Puedes validar que type sea igual a "payment", o simplemente que data tenga un id
            if (data != null && data.get("id") != null) {
                Long paymentId = Long.valueOf(String.valueOf(data.get("id")));
                String externalReference = null;
                String status = null;

                // Consulta Mercado Pago para obtener el payment completo
                try {
                    MercadoPagoConfig.setAccessToken(mpAccessToken);
                    PaymentClient client = new PaymentClient();
                    Payment payment = client.get(paymentId);
                    status = payment.getStatus();
                    externalReference = payment.getExternalReference();
                    System.out.println("Webhook recibido: paymentId = " + paymentId + ", externalReference = " + externalReference + ", status = " + status);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (externalReference != null) {
                    Optional<Pago> pagoOpt = pagoRepository.findById(Long.valueOf(externalReference));
                    if (pagoOpt.isPresent()) {
                        Pago pago = pagoOpt.get();
                        pago.setMpPaymentId(paymentId); // ¡Aquí guardas el paymentId real!
                        pago.setEstado(calcularEstado(status));
                        pagoRepository.save(pago);
                        System.out.println("Pago actualizado correctamente: id local = " + externalReference + ", nuevo estado = " + pago.getEstado());
                    } else {
                        System.out.println("No se encontró el pago por externalReference: " + externalReference);
                    }
                } else {
                    System.out.println("No se pudo obtener el externalReference para el paymentId: " + paymentId);
                }
            } else {
                System.out.println("Webhook recibido SIN data válida o sin paymentId");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }





    // 7. Buscar pago por mpPreferenceId
    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> findByMpPreferenceId(String id) {
        return pagoRepository.findByMpPreferenceId(id);
    }

    // DTO helper
    private PagoRespuestaDTO toDto(Pago pago, String mpInitPoint) {
        PagoRespuestaDTO dto = new PagoRespuestaDTO();
        dto.setId(pago.getId());
        dto.setTipo(pago.getTipo());
        dto.setUserId(pago.getUserId());
        dto.setViajeId(pago.getViajeId());
        dto.setAsiento(pago.getAsiento());
        dto.setMonto(pago.getMonto());
        dto.setEstado(pago.getEstado());

        // Cambia aquí:
        dto.setMpPaymentId(pago.getMpPaymentId() != null ? pago.getMpPaymentId().toString() : null);
        dto.setMpPreferenceId(pago.getMpPreferenceId());

        dto.setFecha(pago.getFecha());
        dto.setDetalles(pago.getDetalles());
        dto.setMpInitPoint(mpInitPoint);
        return dto;
    }
    private PagoEstado calcularEstado(String status) {
        switch (status) {
            case "approved": return PagoEstado.APROBADO;
            case "rejected": return PagoEstado.RECHAZADO;
            case "in_process": return PagoEstado.EN_PROCESO;
            case "cancelled":
            case "expired": return PagoEstado.CANCELADO;
            default: return PagoEstado.PENDIENTE;
        }
    }
    @Override
    @Transactional(readOnly = true)
    public void sincronizarEstadosPagos() {
        Iterable<Pago> iterable = pagoRepository.findAll();
        for (Pago pago : iterable) {
            if (pago.getMpPaymentId() != null) {
                try {
                    MercadoPagoConfig.setAccessToken(mpAccessToken);
                    PaymentClient client = new PaymentClient();
                    Payment payment = client.get(Long.valueOf(pago.getMpPaymentId()));
                    String status = payment.getStatus();
                    pago.setEstado(calcularEstado(status));
                    pagoRepository.save(pago);
                    System.out.println("Sincronizado pago id=" + pago.getId() + ", nuevo estado=" + pago.getEstado());
                } catch (Exception ex) {
                    System.out.println("No se pudo sincronizar pago id=" + pago.getId() + ": " + ex.getMessage());
                }
            }
        }
    }



}
