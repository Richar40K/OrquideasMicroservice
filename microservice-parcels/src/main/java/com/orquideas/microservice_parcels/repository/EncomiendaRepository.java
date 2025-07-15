package com.orquideas.microservice_parcels.repository;

import com.orquideas.microservice_parcels.entities.Encomienda;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EncomiendaRepository extends CrudRepository<Encomienda,Long>
{

    //Lo siento kiyo
    Optional<Encomienda> findByCodigo(String codigo);
}
