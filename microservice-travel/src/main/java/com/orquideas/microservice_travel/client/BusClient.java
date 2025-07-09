package com.orquideas.microservice_travel.client;

import com.orquideas.microservice_travel.DTO.BusDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-bus")
public interface BusClient
{
    @GetMapping("/{id}")
    BusDTO getBusById(@PathVariable("id") Long id);
}
