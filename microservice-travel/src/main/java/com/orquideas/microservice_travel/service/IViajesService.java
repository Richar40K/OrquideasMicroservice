package com.orquideas.microservice_travel.service;

import com.orquideas.microservice_travel.DTO.ActualizarViajeDTO;
import com.orquideas.microservice_travel.DTO.AsientoDTO;
import com.orquideas.microservice_travel.DTO.CrearViajeDTO;
import com.orquideas.microservice_travel.DTO.ViajeRespuestaDTO;
import com.orquideas.microservice_travel.entities.Viajes;

import java.util.List;
import java.util.Optional;

public interface IViajesService {

    List<ViajeRespuestaDTO> findAll();
    List<ViajeRespuestaDTO> listarViajesProgramados();
    Optional<ViajeRespuestaDTO> findById(Long id);
    ViajeRespuestaDTO save(CrearViajeDTO dto);
    Optional<ViajeRespuestaDTO> update(ActualizarViajeDTO dto, Long id);
    void deleteById(Long id);
    List<AsientoDTO> listarAsientosDeViaje(Long viajeId);



}
