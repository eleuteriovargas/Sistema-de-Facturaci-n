package com.vargas.facturacion.repository;

import com.vargas.facturacion.model.entity.Trabajo;
import com.vargas.facturacion.model.enums.EstadoPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
