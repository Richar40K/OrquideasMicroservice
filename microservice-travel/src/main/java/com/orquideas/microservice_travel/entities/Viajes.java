package com.orquideas.microservice_travel.entities;

import com.orquideas.microservice_travel.enums.State;
import jakarta.persistence.*;


import java.sql.Time;
import java.time.LocalDate;

@Entity
@Table(name = "viajes")
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

    private Double precio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Rutas getRutas() {
        return rutas;
    }

    public void setRutas(Rutas rutas) {
        this.rutas = rutas;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public State getEstado() {
        return estado;
    }

    public void setEstado(State estado) {
        this.estado = estado;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
