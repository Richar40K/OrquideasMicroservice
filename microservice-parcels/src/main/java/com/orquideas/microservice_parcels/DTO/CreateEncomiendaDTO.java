package com.orquideas.microservice_parcels.DTO;

import lombok.Getter;

@Getter

public class CreateEncomiendaDTO
{
    private Long userId;
    private String tipo;
    private String origen;
    private String destino;
    private String dniDestino;
    private String nombreDestino;
    private String apellidoDestino;

}
