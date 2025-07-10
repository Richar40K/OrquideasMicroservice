package com.orquideas.microservice_payment.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO
{
    private Long id;
    private String name;
    private String lastName;
    private String dni;
    private String email;
    private String cellphone;
}
