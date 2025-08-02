package com.vargas.facturacion.service.interfaces;

import com.vargas.facturacion.model.entity.Cliente;
import java.util.List;

public interface ClienteService {
    List<Cliente> obterTodosClientes(); // o obtenerTodosClientes() si usas ese nombre
}