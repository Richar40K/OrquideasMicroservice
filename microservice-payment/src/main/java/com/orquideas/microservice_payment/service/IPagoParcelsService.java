package com.orquideas.microservice_payment.service;

import com.orquideas.microservice_payment.DTO.CrearPagoParcelsDTO;
import com.orquideas.microservice_payment.DTO.CrearPagoViajeDTO;
import com.orquideas.microservice_payment.DTO.PagoRespuestaDTO;
import com.orquideas.microservice_payment.DTO.PagoRespuestaParcelsDTO;
import com.orquideas.microservice_payment.entities.Pago;
import com.orquideas.microservice_payment.enums.PagoEstado;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IPagoParcelsService {

    // Iniciar pago de viaje (crea el pago y la preferencia de Mercado Pago)
    PagoRespuestaParcelsDTO iniciarPagoEncomienda(CrearPagoParcelsDTO dto) throws Exception;

    Optional<PagoRespuestaParcelsDTO> findById(Long id);

    List<PagoRespuestaParcelsDTO> findAll();

    // Actualizar estado de pago (por ejemplo, desde un webhook de Mercado Pago)
    PagoRespuestaParcelsDTO actualizarEstado(Long id, PagoEstado nuevoEstado);

    Optional<PagoRespuestaParcelsDTO> editarPago(Long id, String nuevosDetalles);

    Optional<Pago> findByMpPreferenceId(String id);
    void sincronizarEstadosPagos();
}
