package com.orquideas.microservice_parcels.service;

import com.orquideas.microservice_parcels.DTO.CreateEncomiendaDTO;
import com.orquideas.microservice_parcels.DTO.ResponseEncomiendaDTO;

import com.orquideas.microservice_parcels.enums.State;

import com.orquideas.microservice_parcels.entities.Encomienda;


import java.util.List;
import java.util.Optional;

public interface IEncomeindaService
{
    ResponseEncomiendaDTO iniciarPagoViaje(CreateEncomiendaDTO dto) throws Exception;

    Optional<ResponseEncomiendaDTO> findById(Long id);

    List<ResponseEncomiendaDTO> findAll();

    void deleteById(Long id);

    Optional<Encomienda> update(Encomienda encomienda, Long id);
    Optional<ResponseEncomiendaDTO> findByCodigo(String codigo);

    ResponseEncomiendaDTO confirmarPago(Long id) throws Exception;
    //Lo siento :c
    ResponseEncomiendaDTO actualizarEstado(Long id, State nuevoEstado) throws Exception;

}
