package com.vargas.facturacion.mapper;

import com.vargas.facturacion.dto.TrabajoDTO;
import com.vargas.facturacion.model.entity.Cliente;
import com.vargas.facturacion.model.entity.Trabajo;
import org.springframework.stereotype.Component;

@Component
public class TrabajoMapper {

    public TrabajoDTO toDTO(Trabajo trabajo) {
        if (trabajo == null) {
            return null;
        }

        TrabajoDTO dto = new TrabajoDTO();
        dto.setId(trabajo.getId());
        dto.setDescripcion(trabajo.getDescripcion());
        dto.setMonto(trabajo.getMonto());
        dto.setEstadoPago(trabajo.getEstadoPago());
        dto.setFechaTrabajo(trabajo.getFechaTrabajo());
        dto.setFechaVencimiento(trabajo.getFechaVencimiento());
        dto.setNotas(trabajo.getNotas());

        if (trabajo.getCliente() != null) {
            dto.setClienteId(trabajo.getCliente().getId());
            dto.setClienteNombre(trabajo.getCliente().getNombre());
        }

        return dto;
    }

    public Trabajo toEntity(TrabajoDTO dto, Cliente cliente) {
        if (dto == null) {
            return null;
        }

        Trabajo trabajo = new Trabajo();
        trabajo.setId(dto.getId());
        trabajo.setDescripcion(dto.getDescripcion());
        trabajo.setMonto(dto.getMonto());
        trabajo.setEstadoPago(dto.getEstadoPago());
        trabajo.setFechaTrabajo(dto.getFechaTrabajo());
        trabajo.setFechaVencimiento(dto.getFechaVencimiento());
        trabajo.setCliente(cliente);
        trabajo.setNotas(dto.getNotas());

        return trabajo;
    }
}