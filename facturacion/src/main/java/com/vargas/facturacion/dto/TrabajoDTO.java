package com.vargas.facturacion.dto;

import com.vargas.facturacion.model.entity.Trabajo;
import com.vargas.facturacion.model.enums.EstadoPago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TrabajoDTO {
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String descripcion;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal monto = BigDecimal.ZERO;

    private EstadoPago estadoPago = EstadoPago.PENDIENTE;

    @NotNull
    private LocalDate fechaTrabajo;

    @NotNull(groups = Trabajo.OnCreate.class)
    @FutureOrPresent(groups = Trabajo.OnCreate.class)
    private LocalDate fechaVencimiento;
    public interface OnCreate {}

    @NotNull
    private Long clienteId;

    private String clienteNombre;

    private String notas = "";
}