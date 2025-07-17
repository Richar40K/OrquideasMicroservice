package com.orquideas.microservice_payment.DTO;



import java.sql.Time;
import java.time.LocalDate;


public class ViajesDTO
{
    private Long id;
    private RutasDTO ruta;
    private LocalDate fechaSalida;
    private Time horaSalida;
    private Long busId;
    private Double precio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RutasDTO getRuta() {
        return ruta;
    }

    public void setRuta(RutasDTO ruta) {
        this.ruta = ruta;
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
