package com.orquideas.microservice_travel.controller;

import com.orquideas.microservice_travel.DTO.ActualizarViajeDTO;
import com.orquideas.microservice_travel.DTO.AsientoDTO;
import com.orquideas.microservice_travel.DTO.CrearViajeDTO;
import com.orquideas.microservice_travel.DTO.ViajeRespuestaDTO;
import com.orquideas.microservice_travel.service.IViajesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<List<ViajeRespuestaDTO>> listarProgramados(
            @RequestParam(value = "fecha", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        if (fecha == null) {
            return ResponseEntity.ok(viajesService.listarViajesProgramados());
        } else {
            return ResponseEntity.ok(viajesService.listarViajesProgramadosPorFecha(fecha));
        }
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

    @PutMapping("/{viajeId}/asientos/{numero}/ocupar")
    public ResponseEntity<?> ocuparAsiento(
            @PathVariable Long viajeId,
            @PathVariable Integer numero) {
        try {
            viajesService.ocuparAsiento(viajeId, numero);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
    @GetMapping("/rutas-populares")
    public List<Map<String, Object>> getRutasPopulares() {
        return viajesService.obtenerRutasPopulares();
    }

}
