package com.orquideas.microservice_payment.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
@Getter
@Setter
public class RutasDTO
{
    private Long id;
    private String origen;
    private String destino;
    private Time duracion;
    private Long distance;
}
