package com.orquideas.microservice_payment.client;


import com.orquideas.microservice_payment.DTO.ViajesDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "microservice-travel")
public interface ViajesClient
{
    @GetMapping("/{id}")
    ViajesDTO getViajesById(@PathVariable("id") Long id);

    @PutMapping("/{viajeId}/asientos/{numero}/ocupar")
    void ocuparAsiento(
            @PathVariable("viajeId") Long viajeId,
            @PathVariable("numero") Integer numeroAsiento
    );
}
