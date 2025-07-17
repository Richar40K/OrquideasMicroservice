package com.orquideas.microservice_payment.DTO;

import com.orquideas.microservice_payment.enums.PagoEstado;
import com.orquideas.microservice_payment.enums.PagoTipo;


import java.time.LocalDateTime;


public class PagoRespuestaParcelsDTO
{
    private Long id;
    private PagoTipo tipo;
    private Long userId;
    private Long parcelsId;
    private Double monto;
    private PagoEstado estado;
    private String mpPaymentId;      // Debería ser el paymentId (número, pero como String está bien para evitar problemas con Long en JS)
    private LocalDateTime fecha;
    private String detalles;
    private String mpInitPoint;
    private String mpPreferenceId;

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

    public Long getParcelsId() {
        return parcelsId;
    }

    public void setParcelsId(Long parcelsId) {
        this.parcelsId = parcelsId;
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

    public String getMpPaymentId() {
        return mpPaymentId;
    }

    public void setMpPaymentId(String mpPaymentId) {
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

    public String getMpInitPoint() {
        return mpInitPoint;
    }

    public void setMpInitPoint(String mpInitPoint) {
        this.mpInitPoint = mpInitPoint;
    }

    public String getMpPreferenceId() {
        return mpPreferenceId;
    }

    public void setMpPreferenceId(String mpPreferenceId) {
        this.mpPreferenceId = mpPreferenceId;
    }
}
