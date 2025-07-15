package com.orquideas.microservice_travel.service;

import com.orquideas.microservice_travel.entities.Rutas;
import com.orquideas.microservice_travel.repositories.RutasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RutasServiceImpl implements IRutasService {

    @Autowired
    private RutasRepository rutasRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Rutas> findAll() {
        return (List<Rutas>) rutasRepository.findAll();
    }
}
