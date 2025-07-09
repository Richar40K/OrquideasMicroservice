package com.orquideas.microservice_travel.repositories;

import com.orquideas.microservice_travel.entities.Viajes;
import com.orquideas.microservice_travel.enums.State;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface ViajesRepository extends CrudRepository<Viajes,Long> {
    List<Viajes> findByEstadoAndFechaSalida(State estado, LocalDate fecha);
}
