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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        pago.setDetalles(dto.getDetalles());

        // Mercado Pago: Crear preferencia
        MercadoPagoConfig.setAccessToken(mpAccessToken);

        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title("Boleto de Viaje")
                .quantity(1)
                .unitPrice(BigDecimal.valueOf(monto))
                .build();

        PreferenceRequest request = PreferenceRequest.builder()
                .items(List.of(itemRequest))
                .build();

        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(request);

        pago.setMpPreferenceId(preference.getId());  // Guarda el UUID de la preferencia

        pago = pagoRepository.save(pago);

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
            String type = (String) payload.get("type");
            Map<String, Object> data = (Map<String, Object>) payload.get("data");

            if ("payment".equals(type) && data != null) {
                Long paymentId = Long.valueOf(String.valueOf(data.get("id")));
                String preferenceId = null;

                // Busca preference_id en el payload (depende de cómo MercadoPago lo mande)
                if (payload.containsKey("preference_id")) {
                    preferenceId = (String) payload.get("preference_id");
                } else if (data.containsKey("preference_id")) {
                    preferenceId = (String) data.get("preference_id");
                }
                // Si no viene, lamentablemente no puedes mapear a la preferencia original

                String status = null;

                // Puedes obtener el status del Payment si quieres (opcional)
                try {
                    MercadoPagoConfig.setAccessToken(mpAccessToken);
                    PaymentClient client = new PaymentClient();
                    Payment payment = client.get(paymentId);
                    status = payment.getStatus();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (preferenceId != null) {
                    Optional<Pago> pagoOpt = pagoRepository.findByMpPreferenceId(preferenceId);
                    if (pagoOpt.isPresent()) {
                        Pago pago = pagoOpt.get();
                        pago.setMpPaymentId(paymentId); // Guarda el paymentId numérico

                        PagoEstado nuevoEstado;
                        switch (status) {
                            case "approved": nuevoEstado = PagoEstado.APROBADO; break;
                            case "rejected": nuevoEstado = PagoEstado.RECHAZADO; break;
                            case "in_process": nuevoEstado = PagoEstado.EN_PROCESO; break;
                            default: nuevoEstado = PagoEstado.PENDIENTE; break;
                        }
                        pago.setEstado(nuevoEstado);
                        pagoRepository.save(pago);
                    }
                } else {
                    System.out.println("No se encontró el preferenceId en el payload del webhook. No se puede actualizar el Pago.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // 7. Buscar pago por mpPreferenceId
    @Override
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
        // Puedes exponer ambos IDs si gustas, o solo uno
        dto.setMpPaymentId(pago.getMpPreferenceId());
        dto.setFecha(pago.getFecha());
        dto.setDetalles(pago.getDetalles());
        dto.setMpInitPoint(mpInitPoint);
        return dto;
    }
}
