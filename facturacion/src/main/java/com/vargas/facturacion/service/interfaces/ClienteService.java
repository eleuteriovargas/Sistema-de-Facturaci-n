package com.vargas.facturacion.service.interfaces;

import com.vargas.facturacion.dto.ClienteDTO;
import com.vargas.facturacion.model.entity.Cliente;
import java.util.List;

public interface ClienteService {
    List<Cliente> obterTodosClientes(); // o obtenerTodosClientes() si usas ese nombre
    ClienteDTO crearCliente(ClienteDTO clienteDTO);
    List<ClienteDTO> listarClientes();
    ClienteDTO obtenerPorId(Long id);
    ClienteDTO actualizarCliente(Long id, ClienteDTO clienteDTO);
    void eliminarCliente(Long id);
    boolean existeCliente(Long id);

}