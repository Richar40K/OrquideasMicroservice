package com.orquideas.microservice_payment.service;


import com.orquideas.microservice_payment.DTO.CrearPagoViajeDTO;
import com.orquideas.microservice_payment.DTO.PagoRespuestaDTO;
import com.orquideas.microservice_payment.enums.PagoEstado;

import java.util.List;
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
}
