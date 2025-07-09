package com.orquideas.microservice_travel.entities;

import com.orquideas.microservice_travel.enums.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.time.LocalDate;

@Entity
@Table(name = "viajes")
@Getter
@Setter
public class Viajes
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaSalida;

    private Time horaSalida;

    private LocalDate fechaLlegada;

    private Time horaLLegada;

    @ManyToOne
    @JoinColumn(name ="ruta_id")
    private Rutas rutas;


    private Long userId;

    private State estado;

    private Long busId;
}
