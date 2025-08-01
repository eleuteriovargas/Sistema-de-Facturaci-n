package com.vargas.facturacion.model.enums;

public enum EstadoPago {
    PENDIENTE("Pendiente"),
    PAGADO("Pagado"),
    VENCIDO("Vencido");

    private final String descripcion;

    EstadoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
