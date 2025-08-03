package com.vargas.facturacion.service.impl;

import com.vargas.facturacion.dto.ClienteDTO;
import com.vargas.facturacion.exception.BusinessException;
import com.vargas.facturacion.exception.ResourceNotFoundException;
import com.vargas.facturacion.mapper.ClienteMapper;
import com.vargas.facturacion.model.entity.Cliente;
import com.vargas.facturacion.repository.ClienteRepository;
import com.vargas.facturacion.service.interfaces.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;



    @Override
    public List<Cliente> obterTodosClientes() {
        return List.of();
    }

    @Override
    @Transactional
    public ClienteDTO crearCliente(ClienteDTO clienteDTO) {
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new BusinessException("El email ya está registrado");
        }

        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        return clienteMapper.toDTO(clienteRepository.save(cliente));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClienteDTO> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ClienteDTO obtenerPorId(Long id) {
        return clienteMapper.toDTO(clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado")));
    }

    @Override
    @Transactional
    public ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO) {
        // Verificar existencia del cliente
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        // Validar unicidad del email (solo si cambió)
        if (!clienteExistente.getEmail().equals(clienteDTO.getEmail()) &&
                clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new BusinessException("El email ya está registrado");
        }

        // Actualizar campos
        clienteExistente.setNombre(clienteDTO.getNombre());
        clienteExistente.setEmail(clienteDTO.getEmail());
        clienteExistente.setTelefono(clienteDTO.getTelefono());
        clienteExistente.setDireccion(clienteDTO.getDireccion());

        // No necesitamos llamar a save() explícitamente gracias a @Transactional
        // Pero lo hacemos para asegurar la persistencia inmediata
        Cliente clienteActualizado = clienteRepository.saveAndFlush(clienteExistente);

        return clienteMapper.toDTO(clienteActualizado);
    }

    @Override
    @Transactional
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        if (!cliente.getTrabajos().isEmpty()) {
            throw new BusinessException("No se puede eliminar un cliente con trabajos asociados");
        }

        clienteRepository.delete(cliente);
    }

    @Override
    public boolean existeCliente(Long id) {
        return clienteRepository.existsById(id);
    }
}