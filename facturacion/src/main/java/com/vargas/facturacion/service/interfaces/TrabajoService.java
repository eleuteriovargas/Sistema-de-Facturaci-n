package com.vargas.facturacion.service.interfaces;

import com.vargas.facturacion.dto.TrabajoDTO;
import com.vargas.facturacion.exception.BusinessException;
import com.vargas.facturacion.model.entity.Pago;
import com.vargas.facturacion.model.enums.EstadoPago;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface TrabajoService {
    TrabajoDTO crearTrabajo(TrabajoDTO trabajoDTO);
    List<TrabajoDTO> listarTrabajos();
    List<TrabajoDTO> listarTrabajosPorEstado(EstadoPago estado);
    TrabajoDTO cambiarEstadoPago(Long idTrabajo, EstadoPago nuevoEstado);
    List<TrabajoDTO> listarTrabajosFiltrados(EstadoPago estado, Long clienteId);
    Page<TrabajoDTO> listarTrabajosFiltrados(EstadoPago estado, Long clienteId, Pageable pageable);
    TrabajoDTO obtenerPorId(Long id);
    BigDecimal obtenerTotalPagado(Long trabajoId);

    List<TrabajoDTO> listarTrabajosVencidos();


    void registrarPago(Long trabajoId, BigDecimal monto);
    List<Pago> obtenerPagosPorTrabajo(Long trabajoId);

    void registrarPagoCompletoVencido(Long trabajoId);

    private void validarTransicionEstado(EstadoPago estadoActual, EstadoPago nuevoEstado) {
        if (estadoActual == EstadoPago.PAGADO) {
            throw new BusinessException("Los trabajos pagados no pueden modificarse");
        }

        if (estadoActual == EstadoPago.VENCIDO && nuevoEstado == EstadoPago.PENDIENTE) {
            throw new BusinessException("No se puede revertir un trabajo vencido a pendiente");
        }
    }

}