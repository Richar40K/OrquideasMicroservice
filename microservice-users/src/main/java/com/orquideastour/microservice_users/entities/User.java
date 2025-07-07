package com.orquideastour.microservice_users.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.orquideastour.microservice_users.enums.Department;
import com.orquideastour.microservice_users.enums.Position;
import com.orquideastour.microservice_users.enums.State;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
@Table(name = "users")
@Getter
@Setter
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private String name;
    private String secondName;
    private String lastName;
    private Long codigo;

    @Email
    @NotBlank
    private String email;
    private String cellPhone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position puesto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Department departamento;
    private Double salario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private State estado;

    @Column(unique = true)
    private String usuario;
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


    private String generateCodigo(long numero) {
        return String.format("COL-%03d", numero);
    }
}
