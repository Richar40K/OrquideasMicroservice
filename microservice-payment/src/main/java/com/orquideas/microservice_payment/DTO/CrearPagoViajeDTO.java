package com.orquideas.microservice_payment.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearPagoViajeDTO
{
    private Long userId;
    private Long viajeId;
    private Integer asiento;
    private String detalles;
}
