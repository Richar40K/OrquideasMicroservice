package com.orquideas.microservice_travel.controller;

import com.orquideas.microservice_travel.entities.Rutas;
import com.orquideas.microservice_travel.service.RutasServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rutas")
public class RutasController
{
    @Autowired
    private RutasServiceImpl rutasService;

    @GetMapping
    public ResponseEntity<List<Rutas>> getAllRutas(){
        return ResponseEntity.ok(rutasService.findAll());
    }
}
