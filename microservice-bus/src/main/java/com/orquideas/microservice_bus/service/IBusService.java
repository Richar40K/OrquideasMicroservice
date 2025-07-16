package com.orquideas.microservice_bus.service;

import com.orquideas.microservice_bus.entities.Bus;

import java.util.List;
import java.util.Optional;

public interface IBusService
{
    List<Bus> findAll();
    Optional<Bus> findById(Long id);
    Bus save(Bus bus);
    Optional<Bus> update(Bus bus, Long id);
    void deleteById(Long id);
    List<Bus> findByState();
    Long getBusesActivos();
}
