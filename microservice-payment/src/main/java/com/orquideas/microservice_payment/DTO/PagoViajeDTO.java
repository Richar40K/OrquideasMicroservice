package com.orquideas.microservice_payment.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
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
}
