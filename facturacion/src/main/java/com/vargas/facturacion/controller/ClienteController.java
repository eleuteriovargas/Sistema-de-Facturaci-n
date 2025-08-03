package com.vargas.facturacion.controller;

import com.vargas.facturacion.dto.ClienteDTO;
import com.vargas.facturacion.service.interfaces.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String listarClientes(Model model) {
        List<ClienteDTO> clientes = clienteService.listarClientes();
        model.addAttribute("clientes", clientes);
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("cliente", new ClienteDTO());
        return "clientes/formulario";
    }

    @PostMapping
    public String crearCliente(@Valid @ModelAttribute("cliente") ClienteDTO clienteDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "clientes/formulario";
        }

        try {
            clienteService.crearCliente(clienteDTO);
            redirectAttributes.addFlashAttribute("success", "Cliente creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear cliente: " + e.getMessage());
            return "clientes/formulario";
        }

        return "redirect:/clientes";
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            ClienteDTO cliente = clienteService.obtenerPorId(id);
            model.addAttribute("cliente", cliente);
            return "clientes/formulario";
        } catch (Exception e) {
            model.addAttribute("error", "Cliente no encontrado con ID: " + id);
            return "redirect:/clientes";
        }
    }

    @PostMapping("/{id}")
    public String actualizarCliente(@PathVariable Long id,
                                    @Valid @ModelAttribute("cliente") ClienteDTO clienteDTO,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {

        // Debug: Verificar datos recibidos
        System.out.println("Datos recibidos en controlador: " + clienteDTO);

        if (result.hasErrors()) {
            return "clientes/formulario";
        }

        try {
            // Asignar ID por si acaso
            clienteDTO.setId(id);

            // Actualizar y obtener resultado
            ClienteDTO updated = clienteService.actualizarCliente(id, clienteDTO);
            System.out.println("Datos devueltos por servicio: " + updated);

            redirectAttributes.addFlashAttribute("success", "Cliente actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
            return "clientes/formulario";
        }

        return "redirect:/clientes";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarCliente(@PathVariable Long id,
                                  RedirectAttributes redirectAttributes) {
        try {
            if (!clienteService.existeCliente(id)) {
                redirectAttributes.addFlashAttribute("error", "Cliente no encontrado con ID: " + id);
            } else {
                clienteService.eliminarCliente(id);
                redirectAttributes.addFlashAttribute("success", "Cliente eliminado exitosamente");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar cliente: " + e.getMessage());
        }
        return "redirect:/clientes";
    }
}