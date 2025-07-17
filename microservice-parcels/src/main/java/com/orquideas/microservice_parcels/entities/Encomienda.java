package com.orquideas.microservice_parcels.entities;

import com.orquideas.microservice_parcels.enums.Paquete;
import com.orquideas.microservice_parcels.enums.State;
import jakarta.persistence.*;


import java.security.SecureRandom;

@Entity
@Table(name="encomiendas")

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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Paquete getTipo() {
        return tipo;
    }

    public void setTipo(Paquete tipo) {
        this.tipo = tipo;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDniDestino() {
        return dniDestino;
    }

    public void setDniDestino(String dniDestino) {
        this.dniDestino = dniDestino;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }

    public String getApellidoDestino() {
        return apellidoDestino;
    }

    public void setApellidoDestino(String apellidoDestino) {
        this.apellidoDestino = apellidoDestino;
    }

    public State getEstado() {
        return estado;
    }

    public void setEstado(State estado) {
        this.estado = estado;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
