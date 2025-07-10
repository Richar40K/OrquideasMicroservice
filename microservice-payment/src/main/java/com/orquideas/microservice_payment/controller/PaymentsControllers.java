package com.orquideas.microservice_payment.controller;

import com.orquideas.microservice_payment.DTO.CrearPagoViajeDTO;
import com.orquideas.microservice_payment.DTO.PagoRespuestaDTO;
import com.orquideas.microservice_payment.enums.PagoEstado;
import com.orquideas.microservice_payment.service.IPagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class PaymentsControllers {

    @Autowired
    private IPagoService pagoService;

    @PostMapping("/viaje")
    public ResponseEntity<PagoRespuestaDTO> iniciarPagoViaje(@RequestBody CrearPagoViajeDTO dto) throws Exception {

        PagoRespuestaDTO respuesta = pagoService.iniciarPagoViaje(dto);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoRespuestaDTO> findById(@PathVariable Long id) {
        Optional<PagoRespuestaDTO> pago = pagoService.findById(id);
        return pago.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PagoRespuestaDTO>> findAll() {
        return ResponseEntity.ok(pagoService.findAll());
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PagoRespuestaDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody PagoEstado nuevoEstado) {
        PagoRespuestaDTO actualizado = pagoService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}/detalles")
    public ResponseEntity<PagoRespuestaDTO> editarDetalles(
            @PathVariable Long id,
            @RequestBody String nuevosDetalles) {
        Optional<PagoRespuestaDTO> editado = pagoService.editarPago(id, nuevosDetalles);
        return editado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @PostMapping
    public ResponseEntity<String> recibirWebhook(@RequestBody Map<String, Object> payload) {
        pagoService.procesarWebhookMercadoPago(payload);
        return ResponseEntity.ok("OK");
    }
}