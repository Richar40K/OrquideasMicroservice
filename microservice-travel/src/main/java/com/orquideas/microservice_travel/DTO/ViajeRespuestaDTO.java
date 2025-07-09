package com.orquideas.microservice_travel.DTO;

import com.orquideas.microservice_travel.enums.State;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;

@Getter
@Setter
public class ViajeRespuestaDTO {
    private Long id;
    private String origen;
    private String destino;
    private LocalDate fechaSalida;
    private Time horaSalida;
    private LocalDate fechaLlegada;
    private Time horaLLegada;
    private Long busId;
    private Long userId;
    private String nombreChofer;
    private String apellidoChofer;
    private State estado;
}