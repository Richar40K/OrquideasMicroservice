package com.orquideas.microservice_bus.repository;

import com.orquideas.microservice_bus.entities.Bus;
import com.orquideas.microservice_bus.enums.State;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BusRepository extends CrudRepository<Bus,Long>
{
    List<Bus> findByEstado(State estado);
}
