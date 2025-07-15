package com.orquideas.microservice_payment.entities;

import com.orquideas.microservice_payment.enums.PagoEstado;
import com.orquideas.microservice_payment.enums.PagoTipo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "pagos")
@Getter
@Setter
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PagoTipo tipo;

    private Long userId;
    private Long viajeId;
    private Integer asiento;
    private Long encomiendaId;
    private Double monto;

    @Enumerated(EnumType.STRING)
    private PagoEstado estado;

    private String mpPreferenceId;
    private Long mpPaymentId; // Asegúrate que coincide con el tipo en BD (bigint)

    private LocalDateTime fecha;
    private String detalles;
}