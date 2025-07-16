package com.orquideas.microservice_payment.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PagoEncomiendaDTO
{
    private Long pagoId;
    private Double monto;
    private String estado;
    private String detalles;
    private LocalDateTime fecha;

    // Información del usuario
    private Long userId;
    private String name;
    private String email;

    // Información de la encomienda
    private ParcelsDTO parcels;
}
