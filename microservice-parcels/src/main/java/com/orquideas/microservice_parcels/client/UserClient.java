package com.orquideas.microservice_parcels.client;

import com.orquideas.microservice_parcels.DTO.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservice-users")
public interface UserClient
{
    @GetMapping("/{id}")
    UserDTO getUserById(@PathVariable("id") Long id);
}
