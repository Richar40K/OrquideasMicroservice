package com.orquideas.microservice_travel.DTO;

import java.sql.Time;
import java.time.LocalDate;


public class ActualizarViajeDTO {
    private LocalDate fechaSalida;
    private Time horaSalida;
    private LocalDate fechaLlegada;
    private Time horaLLegada;
    private Double precio;

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Time getHoraSalida() {
        return horaSalida;
    }

    public void setHoraSalida(Time horaSalida) {
        this.horaSalida = horaSalida;
    }

    public LocalDate getFechaLlegada() {
        return fechaLlegada;
    }

    public void setFechaLlegada(LocalDate fechaLlegada) {
        this.fechaLlegada = fechaLlegada;
    }

    public Time getHoraLLegada() {
        return horaLLegada;
    }

    public void setHoraLLegada(Time horaLLegada) {
        this.horaLLegada = horaLLegada;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
