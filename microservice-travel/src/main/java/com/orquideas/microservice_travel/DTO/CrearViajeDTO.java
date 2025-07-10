package com.orquideas.microservice_travel.DTO;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;

@Getter
@Setter
public class CrearViajeDTO {
    private Long busId;
    private Long userId;      // id del chofer
    private Long rutaId;
    private LocalDate fechaSalida;
    private Time horaSalida;
    private LocalDate fechaLlegada;
    private Time horaLLegada;
    private Double precio;
}
