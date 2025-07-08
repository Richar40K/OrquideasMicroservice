package com.orquideas.microservice_bus.service;

import com.orquideas.microservice_bus.entities.Bus;
import com.orquideas.microservice_bus.enums.State;
import com.orquideas.microservice_bus.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BusServiceImpl implements IBusService{

    @Autowired
    private BusRepository busRepository;

    @Override
    public List<Bus> findAll() {
        return (List<Bus>) busRepository.findAll();
    }

    @Override
    public Optional<Bus> findById(Long id) {
        return busRepository.findById(id);
    }

    @Override
    public Bus save(Bus bus) {
        bus.setPlaca(bus.getPlaca());
        bus.setCapacidad(bus.getCapacidad());
        bus.setTipo(bus.getTipo());
        bus.setEstado(bus.getEstado());
        return busRepository.save(bus);
    }

    @Override
    public Optional<Bus> update(Bus bus, Long id) {
        Optional<Bus> busOptional = this.findById(id);
        return busOptional.map( bsOp ->{
            bsOp.setEstado(bus.getEstado());
            bsOp.setTipo(bus.getTipo());
            bsOp.setCapacidad(bus.getCapacidad());
            return Optional.of(busRepository.save(bsOp));
        }).orElseGet(()->Optional.empty());
    }

    @Override
    public void deleteById(Long id) {
        busRepository.deleteById(id);
    }

    @Override
    public List<Bus> findByState() {
        return (List<Bus>) busRepository.findByState(State.ACTIVO);
    }
}
