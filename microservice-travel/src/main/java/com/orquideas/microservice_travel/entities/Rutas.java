package com.orquideas.microservice_travel.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Entity
@Table(name = "rutas")
@Getter
@Setter
public class Rutas
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String origen;
    private String destino;
    private Time duracion;
    private Long distance;
}
