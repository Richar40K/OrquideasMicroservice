package com.orquideas.microservice_payment.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;


@Getter
@Setter
public class ViajesDTO
{
    private Long id;
    private RutasDTO ruta;
    private LocalDate fechaSalida;
    private Time horaSalida;
    private Long busId;
    private Double precio;
}
