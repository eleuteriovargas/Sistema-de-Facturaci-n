package com.vargas.facturacion.repository;

import com.vargas.facturacion.model.entity.Trabajo;
import com.vargas.facturacion.model.enums.EstadoPago;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Lock;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrabajoRepository extends JpaRepository<Trabajo, Long> {
    // Consultas derivadas
    List<Trabajo> findByEstadoPago(EstadoPago estadoPago);
    List<Trabajo> findByEstadoPagoAndClienteId(EstadoPago estado, Long clienteId);
    List<Trabajo> findByClienteId(Long clienteId);
    Page<Trabajo> findByEstadoPago(EstadoPago estado, Pageable pageable);
    

    // Consulta con @Query opcional
    @Query("SELECT t FROM Trabajo t WHERE " +
            "(:estado IS NULL OR t.estadoPago = :estado) AND " +
            "(:clienteId IS NULL OR t.cliente.id = :clienteId)")
    List<Trabajo> findFiltrados(@Param("estado") EstadoPago estado,
                                @Param("clienteId") Long clienteId);

    @Modifying
    @Query("UPDATE Trabajo t SET t.estadoPago = :estado, t.fechaPago = :fechaPago WHERE t.id = :id")
    void actualizarEstadoYPago(@Param("id") Long id,
                               @Param("estado") EstadoPago estado,
                               @Param("fechaPago") LocalDate fechaPago);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Trabajo t WHERE t.id = :id")
    Optional<Trabajo> findByIdWithLock(@Param("id") Long id);

    @Query("SELECT t FROM Trabajo t WHERE t.estadoPago = :estado AND t.fechaVencimiento < CURRENT_DATE")
    List<Trabajo> findVencidos(@Param("estado") EstadoPago estado);

    @Query("SELECT t FROM Trabajo t WHERE t.estadoPago = :estado AND t.fechaVencimiento < :fecha")
    List<Trabajo> findByEstadoPagoAndFechaVencimientoBefore(
            @Param("estado") EstadoPago estado,
            @Param("fecha") LocalDate fecha);
}
