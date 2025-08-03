package com.vargas.facturacion.mapper;

import com.vargas.facturacion.dto.ClienteDTO;
import com.vargas.facturacion.model.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }

        Cliente entity = new Cliente();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());
        entity.setDireccion(dto.getDireccion());
        return entity;
    }

    public ClienteDTO toDTO(Cliente entity) {
        if (entity == null) {
            return null;
        }

        ClienteDTO dto = new ClienteDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setEmail(entity.getEmail());
        dto.setTelefono(entity.getTelefono());
        dto.setDireccion(entity.getDireccion());
        return dto;
    }

    public void actualizarDesdeDTO(ClienteDTO dto, Cliente entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setNombre(dto.getNombre());
        entity.setEmail(dto.getEmail());
        entity.setTelefono(dto.getTelefono());
        entity.setDireccion(dto.getDireccion());
    }

}