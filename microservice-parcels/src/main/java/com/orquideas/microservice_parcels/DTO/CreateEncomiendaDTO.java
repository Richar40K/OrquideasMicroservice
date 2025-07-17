package com.orquideas.microservice_parcels.DTO;

public class CreateEncomiendaDTO
{
    private Long userId;
    private String tipo;
    private String origen;
    private String destino;
    private String dniDestino;
    private String nombreDestino;
    private String apellidoDestino;


    public Long getUserId() {
        return userId;
    }

    public String getTipo() {
        return tipo;
    }

    public String getOrigen() {
        return origen;
    }

    public String getDestino() {
        return destino;
    }

    public String getDniDestino() {
        return dniDestino;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public String getApellidoDestino() {
        return apellidoDestino;
    }
}
