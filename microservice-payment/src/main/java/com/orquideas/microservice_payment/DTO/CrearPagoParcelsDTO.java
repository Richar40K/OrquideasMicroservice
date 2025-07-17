package com.orquideas.microservice_payment.DTO;


public class CrearPagoParcelsDTO
{
    private Long userId;
    private Long parcelsId;
    private String detalles;

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

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }
}
