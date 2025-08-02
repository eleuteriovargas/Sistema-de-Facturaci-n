package com.vargas.facturacion.service.impl;

import com.vargas.facturacion.dto.TrabajoDTO;
import com.vargas.facturacion.exception.BusinessException;
import com.vargas.facturacion.exception.ResourceNotFoundException;
import com.vargas.facturacion.mapper.TrabajoMapper;
import com.vargas.facturacion.model.entity.Cliente;
import com.vargas.facturacion.model.entity.Pago;
import com.vargas.facturacion.model.entity.Trabajo;
import com.vargas.facturacion.model.enums.EstadoPago;
import com.vargas.facturacion.repository.ClienteRepository;
import com.vargas.facturacion.repository.PagoRepository;
import com.vargas.facturacion.repository.TrabajoRepository;
import com.vargas.facturacion.service.interfaces.TrabajoService;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TrabajoServiceImpl implements TrabajoService {

    private final TrabajoRepository trabajoRepository;
    private final ClienteRepository clienteRepository;
    private final TrabajoMapper trabajoMapper;
    private final PagoRepository pagoRepository;

    @Override
    @Transactional
    public TrabajoDTO crearTrabajo(TrabajoDTO trabajoDTO) {
        if (trabajoDTO.getFechaVencimiento().isBefore(trabajoDTO.getFechaTrabajo())) {
            throw new BusinessException("La fecha de vencimiento no puede ser anterior a la fecha de trabajo");
        }

        Cliente cliente = clienteRepository.findById(trabajoDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + trabajoDTO.getClienteId()));

        Trabajo trabajo = trabajoMapper.toEntity(trabajoDTO, cliente);
        trabajo.setEstadoPago(EstadoPago.PENDIENTE);

        return trabajoMapper.toDTO(trabajoRepository.save(trabajo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrabajoDTO> listarTrabajos() {
        return trabajoRepository.findAll().stream()
                .map(trabajoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrabajoDTO> listarTrabajosFiltrados(EstadoPago estado, Long clienteId) {
        if (estado != null && clienteId != null) {
            return trabajoRepository.findByEstadoPagoAndClienteId(estado, clienteId)
                    .stream()
                    .map(trabajoMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (estado != null) {
            return trabajoRepository.findByEstadoPago(estado)
                    .stream()
                    .map(trabajoMapper::toDTO)
                    .collect(Collectors.toList());
        } else if (clienteId != null) {
            return trabajoRepository.findByClienteId(clienteId)
                    .stream()
                    .map(trabajoMapper::toDTO)
                    .collect(Collectors.toList());
        }
        return listarTrabajos();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TrabajoDTO> listarTrabajosFiltrados(EstadoPago estado, Long clienteId, Pageable pageable) {
        // Implementación pendiente si es necesaria
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrabajoDTO> listarTrabajosPorEstado(EstadoPago estado) {
        return trabajoRepository.findByEstadoPago(estado)
                .stream()
                .map(trabajoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TrabajoDTO cambiarEstadoPago(Long idTrabajo, EstadoPago nuevoEstado) {
        Trabajo trabajo = trabajoRepository.findByIdWithLock(idTrabajo)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajo no encontrado"));

        // Desactivar validación para fechas vencidas al pagar
        if (nuevoEstado == EstadoPago.PAGADO && trabajo.getFechaVencimiento().isBefore(LocalDate.now())) {
            // Guardar sin validación de fecha vencida
            trabajo.setEstadoPago(EstadoPago.PAGADO);
            trabajo.setFechaPago(LocalDate.now());

            // Registrar pago
            Pago pago = new Pago();
            pago.setMonto(trabajo.getMonto());
            pago.setFechaPago(LocalDate.now());
            pago.setTrabajo(trabajo);
            pagoRepository.save(pago);

            // Guardar sin validación
            trabajoRepository.saveAndFlush(trabajo);
            return trabajoMapper.toDTO(trabajo);
        }

        // Lógica normal para otros casos
        trabajo.setEstadoPago(nuevoEstado);
        return trabajoMapper.toDTO(trabajoRepository.save(trabajo));
    }


    @Override
    @Transactional
    public void registrarPago(Long trabajoId, BigDecimal monto) {
        Trabajo trabajo = trabajoRepository.findByIdWithLock(trabajoId)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajo no encontrado"));

        validarMontoPago(trabajo, monto);

        Pago pago = new Pago();
        pago.setMonto(monto);
        pago.setFechaPago(LocalDate.now());
        pago.setTrabajo(trabajo);
        pagoRepository.save(pago);

        actualizarEstadoSiCompletamentePagado(trabajo);
    }

    @Override
    @Transactional(readOnly = true)
    public TrabajoDTO obtenerPorId(Long id) {
        return trabajoMapper.toDTO(trabajoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajo no encontrado")));
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal obtenerTotalPagado(Long trabajoId) {
        return pagoRepository.findByTrabajoId(trabajoId).stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> obtenerPagosPorTrabajo(Long trabajoId) {
        return pagoRepository.findByTrabajoId(trabajoId);
    }

    @Override
    @Transactional
    public void registrarPagoCompletoVencido(Long trabajoId) {
        Trabajo trabajo = trabajoRepository.findByIdWithLock(trabajoId)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajo no encontrado"));

        if (trabajo.getEstadoPago() != EstadoPago.VENCIDO) {
            throw new BusinessException("Este método solo es para trabajos vencidos");
        }

        registrarPagoVencido(trabajo);
    }

    // Métodos auxiliares privados
    private void validarTransicionEstado(Trabajo trabajo, EstadoPago nuevoEstado) {
        if (trabajo.getEstadoPago() == EstadoPago.PAGADO && nuevoEstado != EstadoPago.PAGADO) {
            throw new BusinessException("No se puede modificar el estado de un trabajo ya pagado");
        }
    }

    private void validarMontoPago(Trabajo trabajo, BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("El monto del pago debe ser mayor que cero");
        }

        BigDecimal totalPagado = obtenerTotalPagado(trabajo.getId());
        BigDecimal nuevoTotal = totalPagado.add(monto);

        if (nuevoTotal.compareTo(trabajo.getMonto()) > 0) {
            throw new BusinessException("El monto excede el total adeudado");
        }
    }

    private void registrarPagoVencido(Trabajo trabajo) {
        Pago pago = new Pago();
        pago.setMonto(trabajo.getMonto());
        pago.setFechaPago(LocalDate.now());
        pago.setTrabajo(trabajo);
        pagoRepository.save(pago);

        trabajo.setEstadoPago(EstadoPago.PAGADO);
        trabajo.setFechaPago(LocalDate.now());
        trabajo.setNotas("Pago registrado después de vencimiento - " + LocalDate.now());
    }

    private void actualizarEstadoSiCompletamentePagado(Trabajo trabajo) {
        BigDecimal totalPagado = obtenerTotalPagado(trabajo.getId());
        if (totalPagado.compareTo(trabajo.getMonto()) >= 0) {
            trabajo.setEstadoPago(EstadoPago.PAGADO);
            trabajo.setFechaPago(LocalDate.now());
            trabajoRepository.save(trabajo);
        }
    }
}