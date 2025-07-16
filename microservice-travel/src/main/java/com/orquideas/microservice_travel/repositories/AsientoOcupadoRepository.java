package com.orquideas.microservice_travel.repositories;

import com.orquideas.microservice_travel.entities.AsientoOcupado;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AsientoOcupadoRepository extends CrudRepository<AsientoOcupado,Long>
{
    Optional<AsientoOcupado> findByViajeIdAndNumeroAsiento(Long viajeId, Integer numeroAsiento);
    List<AsientoOcupado> findByViajeId(Long viajeId);
}
