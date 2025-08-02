package com.vargas.facturacion.service.impl;

import com.vargas.facturacion.model.entity.Cliente;
import com.vargas.facturacion.repository.ClienteRepository;
import com.vargas.facturacion.service.interfaces.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public List<Cliente> obterTodosClientes() {
        return clienteRepository.findAll();
    }
}
