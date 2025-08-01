package com.vargas.facturacion.service.interfaces;

import com.vargas.facturacion.dto.TrabajoDTO;
import com.vargas.facturacion.model.enums.EstadoPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrabajoService {
    TrabajoDTO crearTrabajo(TrabajoDTO trabajoDTO);
    List<TrabajoDTO> listarTrabajos();
    List<TrabajoDTO> listarTrabajosPorEstado(EstadoPago estado);
    TrabajoDTO cambiarEstadoPago(Long idTrabajo, EstadoPago nuevoEstado);
    List<TrabajoDTO> listarTrabajosFiltrados(EstadoPago estado, Long clienteId);
    Page<TrabajoDTO> listarTrabajosFiltrados(EstadoPago estado, Long clienteId, Pageable pageable);

}