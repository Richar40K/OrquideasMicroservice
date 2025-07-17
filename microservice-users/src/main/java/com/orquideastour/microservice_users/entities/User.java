package com.orquideastour.microservice_users.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orquideastour.microservice_users.enums.Department;
import com.orquideastour.microservice_users.enums.Position;
import com.orquideastour.microservice_users.enums.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private String secondName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String dni;
    @Column(unique = true)
    private String codigo;

    private String direccion;

    private String nameEmergency;
    private String phoneEmergency;

    @Email
    @NotBlank
    private String email;
    private String cellPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Position puesto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private Department departamento;
    private Double salario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private State estado;

    @Column(unique = true, nullable = true)
    private String username;
    @NotBlank
    private String password;
    @JsonIgnoreProperties({"handler", "hibernateLazyInitializer"})
    @ManyToMany
    @JoinTable(
            name="user_roles",
            joinColumns = {@JoinColumn(name="user_id")},inverseJoinColumns = {@JoinColumn(name="role_id")},
            uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id","role_id"})}
    )
    private List<Role> roles;

    private Boolean enabled;
    @Transient
    @JsonProperty
    private Boolean admin;

    public Boolean isEnabled() {
        return enabled;
    }


    public Boolean isAdmin() {
        return admin;
    }


    public String generateCodigo(long numero) {
        return String.format("COL-%03d", numero);
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNameEmergency() {
        return nameEmergency;
    }

    public void setNameEmergency(String nameEmergency) {
        this.nameEmergency = nameEmergency;
    }

    public String getPhoneEmergency() {
        return phoneEmergency;
    }

    public void setPhoneEmergency(String phoneEmergency) {
        this.phoneEmergency = phoneEmergency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public Position getPuesto() {
        return puesto;
    }

    public void setPuesto(Position puesto) {
        this.puesto = puesto;
    }

    public Department getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Department departamento) {
        this.departamento = departamento;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    public State getEstado() {
        return estado;
    }

    public void setEstado(State estado) {
        this.estado = estado;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
