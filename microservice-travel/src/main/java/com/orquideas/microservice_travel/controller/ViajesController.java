package com.orquideas.microservice_travel.controller;

import com.orquideas.microservice_travel.DTO.ActualizarViajeDTO;
import com.orquideas.microservice_travel.DTO.AsientoDTO;
import com.orquideas.microservice_travel.DTO.CrearViajeDTO;
import com.orquideas.microservice_travel.DTO.ViajeRespuestaDTO;
import com.orquideas.microservice_travel.service.IViajesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ViajesController
{
    @Autowired
    private IViajesService viajesService;

    @GetMapping
    public ResponseEntity<List<ViajeRespuestaDTO>> findAll() {
        return ResponseEntity.ok(viajesService.findAll());
    }
    @GetMapping("/programados")
    public ResponseEntity<List<ViajeRespuestaDTO>> listarProgramados() {
        return ResponseEntity.ok(viajesService.listarViajesProgramados());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ViajeRespuestaDTO> findById(@PathVariable Long id) {
        Optional<ViajeRespuestaDTO> viaje = viajesService.findById(id);
        return viaje.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<ViajeRespuestaDTO> crearViaje(@RequestBody CrearViajeDTO dto) {
        ViajeRespuestaDTO creado = viajesService.save(dto);
        return ResponseEntity.ok(creado);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ViajeRespuestaDTO> actualizarViaje(@PathVariable Long id, @RequestBody ActualizarViajeDTO dto) {
        Optional<ViajeRespuestaDTO> actualizado = viajesService.update(dto, id);
        return actualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarViaje(@PathVariable Long id) {
        viajesService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/asientos")
    public ResponseEntity<List<AsientoDTO>> listarAsientos(@PathVariable Long id) {
        return ResponseEntity.ok(viajesService.listarAsientosDeViaje(id));
    }
}
