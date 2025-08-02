package com.vargas.facturacion.repository;

import com.vargas.facturacion.model.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByTrabajoId(Long trabajoId);
}