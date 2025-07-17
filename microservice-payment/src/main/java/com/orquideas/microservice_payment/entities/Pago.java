package com.orquideas.microservice_payment.entities;

import com.orquideas.microservice_payment.enums.PagoEstado;
import com.orquideas.microservice_payment.enums.PagoTipo;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")

public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PagoTipo tipo;

    private Long userId;
    private Long viajeId;
    private Integer asiento;
    private Long encomiendaId;
    private Double monto;

    @Enumerated(EnumType.STRING)
    private PagoEstado estado;

    private String mpPreferenceId;
    private Long mpPaymentId; // Asegúrate que coincide con el tipo en BD (bigint)

    private LocalDateTime fecha;
    private String detalles;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PagoTipo getTipo() {
        return tipo;
    }

    public void setTipo(PagoTipo tipo) {
        this.tipo = tipo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getViajeId() {
        return viajeId;
    }

    public void setViajeId(Long viajeId) {
        this.viajeId = viajeId;
    }

    public Integer getAsiento() {
        return asiento;
    }

    public void setAsiento(Integer asiento) {
        this.asiento = asiento;
    }

    public Long getEncomiendaId() {
        return encomiendaId;
    }

    public void setEncomiendaId(Long encomiendaId) {
        this.encomiendaId = encomiendaId;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public PagoEstado getEstado() {
        return estado;
    }

    public void setEstado(PagoEstado estado) {
        this.estado = estado;
    }

    public String getMpPreferenceId() {
        return mpPreferenceId;
    }

    public void setMpPreferenceId(String mpPreferenceId) {
        this.mpPreferenceId = mpPreferenceId;
    }

    public Long getMpPaymentId() {
        return mpPaymentId;
    }

    public void setMpPaymentId(Long mpPaymentId) {
        this.mpPaymentId = mpPaymentId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
}


