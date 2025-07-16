package com.orquideas.microservice_payment.controller;

import com.orquideas.microservice_payment.DTO.CrearPagoParcelsDTO;
import com.orquideas.microservice_payment.DTO.CrearPagoViajeDTO;
import com.orquideas.microservice_payment.DTO.PagoRespuestaDTO;
import com.orquideas.microservice_payment.DTO.PagoRespuestaParcelsDTO;
import com.orquideas.microservice_payment.enums.PagoEstado;
import com.orquideas.microservice_payment.service.IPagoParcelsService;
import com.orquideas.microservice_payment.service.IPagoService;
import com.orquideas.microservice_payment.service.PagoParcelsServiceImpl;
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

    @Autowired
    private IPagoParcelsService pagoParcelsService;

    @PostMapping("/viaje")
    public ResponseEntity<PagoRespuestaDTO> iniciarPagoViaje(@RequestBody CrearPagoViajeDTO dto) throws Exception {

        PagoRespuestaDTO respuesta = pagoService.iniciarPagoViaje(dto);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/viajes/{id}")
    public ResponseEntity<PagoRespuestaDTO> findById(@PathVariable Long id) {
        Optional<PagoRespuestaDTO> pago = pagoService.findById(id);
        return pago.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/viaje")
    public ResponseEntity<List<PagoRespuestaDTO>> findAll() {
        return ResponseEntity.ok(pagoService.findAll());
    }

    @PatchMapping("/viaje/{id}/estado")
    public ResponseEntity<PagoRespuestaDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestBody PagoEstado nuevoEstado) {
        PagoRespuestaDTO actualizado = pagoService.actualizarEstado(id, nuevoEstado);
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/viaje/{id}/detalles")
    public ResponseEntity<PagoRespuestaDTO> editarDetalles(
            @PathVariable Long id,
            @RequestBody String nuevosDetalles) {
        Optional<PagoRespuestaDTO> editado = pagoService.editarPago(id, nuevosDetalles);
        return editado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> recibirWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("Payload recibido de MercadoPago: " + payload);
        pagoService.procesarWebhookMercadoPago(payload);
        return ResponseEntity.ok("OK");
    }

    @GetMapping("/sincronizar")
    public ResponseEntity<String> sincronizarPagos() {
        pagoService.sincronizarEstadosPagos();
        return ResponseEntity.ok("Pagos sincronizados");
    }


    //CONTROLLERS DE ENCOMIENDAS
    @PostMapping("/encomienda")
    public ResponseEntity<PagoRespuestaParcelsDTO> iniciarPagoViaje(@RequestBody CrearPagoParcelsDTO dto) throws Exception {
        PagoRespuestaParcelsDTO respuesta = pagoParcelsService.iniciarPagoEncomienda(dto);
        return ResponseEntity.ok(respuesta);
    }
    @GetMapping("/encomienda")
    public ResponseEntity<List<PagoRespuestaParcelsDTO>> findAllParcels(){
        return ResponseEntity.ok(pagoParcelsService.findAll());
    }
    @GetMapping("/encomienda/{id}")
    public ResponseEntity<PagoRespuestaParcelsDTO> findByIdParcels(@PathVariable Long id) {
        Optional<PagoRespuestaParcelsDTO> pago = pagoParcelsService.findById(id);
        return pago.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


}