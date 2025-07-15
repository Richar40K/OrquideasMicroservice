package com.orquideas.microservice_payment.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParcelsDTO
{
    private Long id;
    private String tipo;
    private Double precio;
    private String destino;
}
