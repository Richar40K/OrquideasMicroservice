package com.orquideas.microservice_bus.entities;

import com.orquideas.microservice_bus.enums.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "bus")

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

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Long capacidad) {
        this.capacidad = capacidad;
    }

    public State getEstado() {
        return estado;
    }

    public void setEstado(State estado) {
        this.estado = estado;
    }
}
