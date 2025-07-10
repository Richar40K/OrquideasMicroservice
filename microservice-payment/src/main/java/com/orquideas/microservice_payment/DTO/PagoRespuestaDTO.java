package com.orquideas.microservice_payment.DTO;

import com.orquideas.microservice_payment.enums.PagoEstado;
import com.orquideas.microservice_payment.enums.PagoTipo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PagoRespuestaDTO
{
    private Long id;
    private PagoTipo tipo;          // VIAJE (por ahora)
    private Long userId;
    private Long viajeId;
    private Integer asiento;
    private Double monto;
    private PagoEstado estado;
    private String mpPaymentId;     // Id de preferencia/pago de Mercado Pago
    private LocalDateTime fecha;
    private String detalles;        // Opcional (comentarios)
    private String mpInitPoint;
}
