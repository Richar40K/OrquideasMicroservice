package com.orquideas.microservice_parcels.controllers;

import com.orquideas.microservice_parcels.DTO.CreateEncomiendaDTO;
import com.orquideas.microservice_parcels.DTO.ResponseEncomiendaDTO;

import com.orquideas.microservice_parcels.entities.Encomienda;
import com.orquideas.microservice_parcels.enums.State;
import com.orquideas.microservice_parcels.service.IEncomeindaService;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class EncomiendaController {
    @Autowired
    private IEncomeindaService encomeindaService;

    @PostMapping
    public ResponseEntity<ResponseEncomiendaDTO> crearEncomienda(@RequestBody CreateEncomiendaDTO dto) {
        try {
            ResponseEncomiendaDTO response = encomeindaService.iniciarPagoViaje(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseEncomiendaDTO> obtenerPorId(@PathVariable Long id) {
        return encomeindaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Obtener todas las encomiendas
    @GetMapping
    public ResponseEntity<List<ResponseEncomiendaDTO>> obtenerTodas() {
        return ResponseEntity.ok(encomeindaService.findAll());
    }


    //terrible
    @GetMapping("/code/{codigo}")
    public ResponseEntity<ResponseEncomiendaDTO> getEncomiendaByCodigo(@PathVariable String codigo) {
        return encomeindaService.findByCodigo(codigo)
                .map(ResponseEntity::ok) // Si existe, devuelve 200 OK con el DTO
                .orElseGet(() -> ResponseEntity.notFound().build()); // Si no existe, devuelve 404
    }
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ResponseEncomiendaDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> estadoRequest) throws Exception {

        State nuevoEstado = State.valueOf(estadoRequest.get("estado"));
        ResponseEncomiendaDTO encomiendaActualizada = encomeindaService.actualizarEstado(id, nuevoEstado);

        return ResponseEntity.ok(encomiendaActualizada);
    }
    // Eliminar encomienda por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        encomeindaService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}")
    public ResponseEntity<Encomienda> updateEncomienda(@RequestBody Encomienda encomienda, @PathVariable Long id) {
        Optional<Encomienda> updatedEncomienda = encomeindaService.update(encomienda, id);

        return updatedEncomienda
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
