package com.orquideas.microservice_travel.DTO;


import java.sql.Time;
import java.time.LocalDate;

public class CrearViajeDTO {
    private Long busId;
    private Long userId;      // id del chofer
    private Long rutaId;
    private LocalDate fechaSalida;
    private Time horaSalida;
    private LocalDate fechaLlegada;
    private Time horaLLegada;
    private Double precio;


    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRutaId() {
        return rutaId;
    }

    public void setRutaId(Long rutaId) {
        this.rutaId = rutaId;
    }

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
