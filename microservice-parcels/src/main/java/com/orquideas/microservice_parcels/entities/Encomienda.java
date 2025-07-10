package com.orquideas.microservice_parcels.entities;

import com.orquideas.microservice_parcels.enums.Paquete;
import com.orquideas.microservice_parcels.enums.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.security.SecureRandom;

@Entity
@Table(name="encomiendas")
@Getter
@Setter
public class Encomienda
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String codigo;

    private Long userId;

    private Paquete tipo;

    private String origen;

    private String destino;

    private Double precio;

    private String dniDestino;

    private String nombreDestino;

    private String apellidoDestino;

    private State estado;

    @Column(unique = true)
    private String clave;

    public String generateCodigo(long numero) {
        return String.format("ORQ-2025-%03d", numero);
    }

    public static String generateClaveConId(long id) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            sb.append(random.nextInt(10));
        }
        sb.append(String.format("%06d", id));
        for (int i = 0; i < 5; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
