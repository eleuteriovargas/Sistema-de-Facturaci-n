package com.vargas.facturacion.service.impl;

import com.vargas.facturacion.dto.TrabajoDTO;
import com.vargas.facturacion.exception.BusinessException;
import com.vargas.facturacion.exception.ResourceNotFoundException;
import com.vargas.facturacion.mapper.TrabajoMapper;
import com.vargas.facturacion.model.entity.Cliente;
import com.vargas.facturacion.model.entity.Trabajo;
import com.vargas.facturacion.model.enums.EstadoPago;
import com.vargas.facturacion.repository.ClienteRepository;
import com.vargas.facturacion.repository.TrabajoRepository;
import com.vargas.facturacion.service.interfaces.TrabajoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TrabajoServiceImpl implements TrabajoService {

    private final TrabajoRepository trabajoRepository;
    private final ClienteRepository clienteRepository;
    private final TrabajoMapper trabajoMapper;

    @Override
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
    public Page<TrabajoDTO> listarTrabajosFiltrados(EstadoPago estado, Long clienteId, Pageable pageable) {
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
    public TrabajoDTO cambiarEstadoPago(Long idTrabajo, EstadoPago nuevoEstado) {
        // Validación básica del parámetro
        if (nuevoEstado == null) {
            throw new BusinessException("El nuevo estado no puede ser nulo");
        }

        // Obtener el trabajo existente
        Trabajo trabajo = trabajoRepository.findById(idTrabajo)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajo no encontrado con ID: " + idTrabajo));

        // Validar transición de estado
        if (trabajo.getEstadoPago() == EstadoPago.PAGADO) {
            if (nuevoEstado != EstadoPago.PAGADO) {
                throw new BusinessException("No se puede modificar el estado de un trabajo ya pagado");
            }
            return trabajoMapper.toDTO(trabajo); // No hay cambios necesarios
        }

        // Aplicar el nuevo estado
        trabajo.setEstadoPago(nuevoEstado);

        // Lógica adicional para estado VENCIDO
        if (nuevoEstado == EstadoPago.VENCIDO) {
            trabajo.setNotas("Trabajo marcado como vencido el " + LocalDate.now());
        }

        // Guardar y retornar
        Trabajo trabajoActualizado = trabajoRepository.save(trabajo);
        return trabajoMapper.toDTO(trabajoActualizado);
    }
}