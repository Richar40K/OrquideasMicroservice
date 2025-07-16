package com.orquideas.microservice_payment.service;


import com.orquideas.microservice_payment.DTO.CrearPagoViajeDTO;
import com.orquideas.microservice_payment.DTO.PagoEncomiendaDTO;
import com.orquideas.microservice_payment.DTO.PagoRespuestaDTO;
import com.orquideas.microservice_payment.DTO.PagoViajeDTO;
import com.orquideas.microservice_payment.entities.Pago;
import com.orquideas.microservice_payment.enums.PagoEstado;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IPagoService
{
    // Iniciar pago de viaje (crea el pago y la preferencia de Mercado Pago)
    PagoRespuestaDTO iniciarPagoViaje(CrearPagoViajeDTO dto) throws Exception;

    Optional<PagoRespuestaDTO> findById(Long id);

    List<PagoRespuestaDTO> findAll();

    // Actualizar estado de pago (por ejemplo, desde un webhook de Mercado Pago)
    PagoRespuestaDTO actualizarEstado(Long id, PagoEstado nuevoEstado);

    Optional<PagoRespuestaDTO> editarPago(Long id, String nuevosDetalles);

    void procesarWebhookMercadoPago(Map<String, Object> payload);
    Optional<Pago> findByMpPreferenceId(String id);
    void sincronizarEstadosPagos();
    List<PagoViajeDTO> getPagosViajesAprobadosPorUsuario(Long userId);
    Double obtenerTotalDePagosAprobados();
    Double obtenerTotalPagosViajes();
    Double obtenerTotalPagosPendientes();
    Double calcularTotalPagosAprobadosHoy();
    Long contarViajesAprobados();
}
