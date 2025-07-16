package com.orquideas.microservice_payment.service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.orquideas.microservice_payment.DTO.*;
import com.orquideas.microservice_payment.client.ParcelsClient;
import com.orquideas.microservice_payment.client.UserClient;
import com.orquideas.microservice_payment.entities.Pago;
import com.orquideas.microservice_payment.enums.PagoEstado;
import com.orquideas.microservice_payment.enums.PagoTipo;
import com.orquideas.microservice_payment.repository.PagoRepository;
import org.apache.catalina.User;
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
import java.util.stream.Collectors;

@Service
public class PagoParcelsServiceImpl  implements IPagoParcelsService
{
    @Autowired
    private UserClient userClient;
    @Autowired
    private ParcelsClient parcelsClient;
    @Autowired
    private PagoRepository pagoRepository;

    @Value("${mercadopago.access-token}")
    private String mpAccessToken;

    @Override
    @Transactional
    public PagoRespuestaParcelsDTO iniciarPagoEncomienda(CrearPagoParcelsDTO dto) throws Exception {
        UserDTO user = userClient.getUserById(dto.getUserId());
        if (user == null) throw new RuntimeException("Usuario no existe");

        ParcelsDTO encomienda = parcelsClient.obtenerPorId(dto.getParcelsId());
        if (encomienda == null) throw new RuntimeException("Encomienda  no existe");

        Double monto = encomienda.getPrecio();

        Pago pago = new Pago();
        pago.setTipo(PagoTipo.ENCOMIENDA);
        pago.setUserId(dto.getUserId());
        pago.setEncomiendaId(dto.getParcelsId());

        pago.setMonto(monto);
        pago.setEstado(PagoEstado.PENDIENTE);
        pago.setFecha(LocalDateTime.now());
        pago.setDetalles("Boleto para la encomienda tipo  " + encomienda.getTipo());

        pago=pagoRepository.save(pago);


        MercadoPagoConfig.setAccessToken(mpAccessToken);

        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title("Boleto de encomienda " + encomienda.getTipo())
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

    @Override
    public Optional<PagoRespuestaParcelsDTO> findById(Long id) {
        return pagoRepository.findById(id)
                .map(p -> toDto(p, null));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoRespuestaParcelsDTO> findAll() {
        Iterable<Pago> iterable = pagoRepository.findByTipo(PagoTipo.ENCOMIENDA);
        List<PagoRespuestaParcelsDTO> dtos = new ArrayList<>();
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

    @Override
    @Transactional
    public PagoRespuestaParcelsDTO actualizarEstado(Long id, PagoEstado nuevoEstado) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no existe"));
        pago.setEstado(nuevoEstado);
        pagoRepository.save(pago);
        return toDto(pago, null);
    }

    @Override
    @Transactional
    public Optional<PagoRespuestaParcelsDTO> editarPago(Long id, String nuevosDetalles) {
        return pagoRepository.findById(id)
                .map(p -> {
                    p.setDetalles(nuevosDetalles);
                    pagoRepository.save(p);
                    return toDto(p, null);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> findByMpPreferenceId(String id) {
        return pagoRepository.findByMpPreferenceId(id);
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

    @Override
    @Transactional(readOnly = true)
    public List<PagoEncomiendaDTO> getPagosEncomiendasAprobadasPorUsuario(Long userId) {
        List<Pago> pagos=pagoRepository.findByUserIdAndTipoAndEstado(userId,PagoTipo.ENCOMIENDA,PagoEstado.APROBADO);
        return pagos.stream().map( pago -> {
            PagoEncomiendaDTO dto = new PagoEncomiendaDTO();
            dto.setPagoId(pago.getId());
            dto.setMonto(pago.getMonto());
            dto.setEstado(pago.getEstado().toString());
            dto.setDetalles(pago.getDetalles());
            dto.setFecha(pago.getFecha());
            dto.setUserId(pago.getUserId());

            UserDTO user = userClient.getUserById(pago.getUserId());
            dto.setName(user.getName() + " " + user.getLastName());
            dto.setEmail(user.getEmail());

            ParcelsDTO encomienda = parcelsClient.obtenerPorId(pago.getEncomiendaId());
            dto.setParcels(encomienda);

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Double obtenerTotalPagosEncomiendas() {
        return pagoRepository.calcularTotalPagosEncomiendas();
    }

    private PagoRespuestaParcelsDTO toDto(Pago pago, String mpInitPoint) {
        PagoRespuestaParcelsDTO dto = new PagoRespuestaParcelsDTO();
        dto.setId(pago.getId());
        dto.setTipo(pago.getTipo());
        dto.setUserId(pago.getUserId());
        dto.setParcelsId(pago.getEncomiendaId());
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
}
