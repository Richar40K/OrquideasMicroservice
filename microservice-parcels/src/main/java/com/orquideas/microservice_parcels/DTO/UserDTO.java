package com.orquideas.microservice_parcels.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO
{
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String cellphone;
}
