package com.orquideas.microservice_parcels.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseEncomiendaDTO
{
    private Long id;
    private UserDTO user;

    private String tipo;
    private String origen;
    private String destino;
    private String dniDestino;
    private String nombreDestino;
    private String apellidoDestino;
    private String estado;

    private String codigo;
    private Double precio;
    private String clave;

}
