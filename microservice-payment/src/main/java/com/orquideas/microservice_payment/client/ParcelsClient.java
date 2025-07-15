package com.orquideas.microservice_payment.client;

import com.orquideas.microservice_payment.DTO.ParcelsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-parcels")
public interface ParcelsClient
{
    @GetMapping("/{id}")
    ParcelsDTO obtenerPorId(@PathVariable("id") Long id);
}
