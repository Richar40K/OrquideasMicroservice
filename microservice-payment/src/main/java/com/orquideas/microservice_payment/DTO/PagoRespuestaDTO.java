package com.orquideas.microservice_payment.DTO;

import com.orquideas.microservice_payment.enums.PagoEstado;
import com.orquideas.microservice_payment.enums.PagoTipo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PagoRespuestaDTO {
    private Long id;
    private PagoTipo tipo;
    private Long userId;
    private Long viajeId;
    private Integer asiento;
    private Double monto;
    private PagoEstado estado;
    private String mpPaymentId;      // Debería ser el paymentId (número, pero como String está bien para evitar problemas con Long en JS)
    private LocalDateTime fecha;
    private String detalles;
    private String mpInitPoint;
    private String mpPreferenceId;   // <-- ¡Agrega esto si quieres mostrar ambos!
}
