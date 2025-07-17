package com.orquideas.microservice_travel.DTO;

import com.orquideas.microservice_travel.enums.State;


import java.sql.Time;
import java.time.LocalDate;


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
    private Double precio;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
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

    public String getNombreChofer() {
        return nombreChofer;
    }

    public void setNombreChofer(String nombreChofer) {
        this.nombreChofer = nombreChofer;
    }

    public String getApellidoChofer() {
        return apellidoChofer;
    }

    public void setApellidoChofer(String apellidoChofer) {
        this.apellidoChofer = apellidoChofer;
    }

    public State getEstado() {
        return estado;
    }

    public void setEstado(State estado) {
        this.estado = estado;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}