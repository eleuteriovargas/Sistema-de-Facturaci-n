package com.vargas.facturacion.model.entity;

import com.vargas.facturacion.model.enums.EstadoPago;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "trabajos")
public class Trabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String descripcion;

    @NotNull(message = "El monto no puede ser nulo")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor que 0")
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estadoPago = EstadoPago.PENDIENTE;

    @NotNull(message = "La fecha de trabajo es requerida")
    @Column(nullable = false)
    private LocalDate fechaTrabajo;

    @NotNull(message = "La fecha de vencimiento es requerida")
    @FutureOrPresent(message = "La fecha de vencimiento debe ser hoy o en el futuro", groups = OnCreate.class)
    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(length = 500)
    private String notas;

    private LocalDate fechaPago;

    @OneToMany(mappedBy = "trabajo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();

    // Método para marcar como pagado
    public void marcarComoPagado() {
        this.estadoPago = EstadoPago.PAGADO;
        this.fechaPago = LocalDate.now();
    }

    // Método para marcar como vencido
    public void marcarComoVencido() {
        if (this.fechaVencimiento.isBefore(LocalDate.now())) {
            this.estadoPago = EstadoPago.VENCIDO;
        }
    }

    // Método para verificar si está vencido
    public boolean estaVencido() {
        return this.fechaVencimiento.isBefore(LocalDate.now())
                && this.estadoPago != EstadoPago.PAGADO;
    }

    // Método para agregar pago
    public void agregarPago(Pago pago) {
        this.pagos.add(pago);
        pago.setTrabajo(this);

        // Verificar si está completamente pagado
        if (calcularTotalPagado().compareTo(this.monto) >= 0) {
            marcarComoPagado();
        }
    }

    // Método para calcular total pagado
    public BigDecimal calcularTotalPagado() {
        return this.pagos.stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public interface OnCreate {}
}