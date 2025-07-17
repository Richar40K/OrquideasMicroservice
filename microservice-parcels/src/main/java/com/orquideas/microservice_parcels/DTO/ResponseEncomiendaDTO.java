package com.orquideas.microservice_parcels.DTO;


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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
