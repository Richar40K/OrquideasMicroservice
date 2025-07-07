package com.orquideastour.microservice_users.enums;

public enum Position
{
    GERENTE_GENERAL("Gerente General"),
    EJECUTIVA_VENTAS("Ejecutiva de Ventas"),
    SUPERVISOR_ALMACEN("Supervisor de Almacén"),
    CHOFER("Chofer"),
    ATENCION_CLIENTE("Atención al Cliente"),
    ASISTENTE_ADMINISTRATIVO("Asistente Administrativo");

    private final String displayName;

    Position(String displayName) {
        this.displayName = displayName;
    }
    @Override
    public String toString() {
        return displayName;
    }
    public static Position fromString(String text) {
        for (Position p : Position.values()) {
            if (p.displayName.equalsIgnoreCase(text)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Puesto desconocido: " + text);
    }
}
