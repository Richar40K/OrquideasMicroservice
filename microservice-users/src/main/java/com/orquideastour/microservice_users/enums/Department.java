package com.orquideastour.microservice_users.enums;

public enum Department {
    ADMINISTRACION("Administración"),
    VENTAS("Ventas"),
    ALMACEN("Almacén"),
    TRANSPORTE("Transporte"),
    ATENCION_CLIENTE("Atención al Cliente");

    private final String displayName;

    Department(String displayName) {
        this.displayName = displayName;
    }
    @Override
    public String toString() { return displayName; }

    public static Department fromString(String text) {
        for (Department d : values()) {
            if (d.displayName.equalsIgnoreCase(text)) {
                return d;
            }
        }
        throw new IllegalArgumentException("Departamento desconocido: " + text);
    }
}
