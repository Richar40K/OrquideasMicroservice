package com.orquideas.microservice_travel.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "asientos_ocupados")
@Getter
@Setter
public class AsientoOcupado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long viajeId;
    private Integer numeroAsiento;
}
