package com.orquideas.microservice_travel.DTO;

import lombok.Getter;
import lombok.Setter;

public class BusDTO
{
    private Long id;
    private String estado;
    private Long capacidad;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Long capacidad) {
        this.capacidad = capacidad;
    }
}
