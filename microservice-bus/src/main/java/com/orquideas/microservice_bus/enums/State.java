package com.orquideas.microservice_bus.enums;

public enum State {

    ACTIVO("Activo"),
    INACTIVO("Inactivo"),
    MANTENIMIENTO("Mantenimiento"),
    FUERA_DE_SERVICIO("Fuera de servicio");

    private final String label;

    State(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
