package com.orquideas.microservice_payment.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearPagoParcelsDTO
{
    private Long userId;
    private Long parcelsId;
    private String detalles;
}
