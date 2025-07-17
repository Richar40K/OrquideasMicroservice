package com.orquideas.microservice_payment.DTO;


import java.time.LocalDateTime;
import java.util.Objects;


public class PagoViajeDTO
{
    private Long pagoId;
    private Double monto;
    private String estado;
    private String detalles;
    private LocalDateTime fecha;

    private Long userId;
    private String name;
    private String email;

    private ViajesDTO viaje;
    private Integer asiento;

    public Long getPagoId() {
        return pagoId;
    }

    public void setPagoId(Long pagoId) {
        this.pagoId = pagoId;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ViajesDTO getViaje() {
        return viaje;
    }

    public void setViaje(ViajesDTO viaje) {
        this.viaje = viaje;
    }

    public Integer getAsiento() {
        return asiento;
    }

    public void setAsiento(Integer asiento) {
        this.asiento = asiento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PagoViajeDTO that = (PagoViajeDTO) o;
        return Objects.equals(pagoId, that.pagoId) &&
                Objects.equals(monto, that.monto) &&
                Objects.equals(estado, that.estado) &&
                Objects.equals(detalles, that.detalles) &&
                Objects.equals(fecha, that.fecha) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(email, that.email) &&
                Objects.equals(viaje, that.viaje) &&
                Objects.equals(asiento, that.asiento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pagoId, monto, estado, detalles, fecha, userId, name, email, viaje, asiento);
    }

    // toString()
    @Override
    public String toString() {
        return "PagoViajeDTO{" +
                "pagoId=" + pagoId +
                ", monto=" + monto +
                ", estado='" + estado + '\'' +
                ", detalles='" + detalles + '\'' +
                ", fecha=" + fecha +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", viaje=" + viaje +
                ", asiento=" + asiento +
                '}';
    }
}
