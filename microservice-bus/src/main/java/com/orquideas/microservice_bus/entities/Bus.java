package com.orquideas.microservice_bus.entities;

import com.orquideas.microservice_bus.enums.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bus")
@Getter
@Setter
public class Bus
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(unique = true)
    @NotBlank
    private String placa;

    private String tipo;

    private Long capacidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State estado;

}
