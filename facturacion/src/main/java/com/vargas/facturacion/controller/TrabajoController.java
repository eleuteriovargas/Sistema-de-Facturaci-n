package com.vargas.facturacion.controller;

import com.vargas.facturacion.dto.TrabajoDTO;
import com.vargas.facturacion.model.enums.EstadoPago;
import com.vargas.facturacion.repository.ClienteRepository;
import com.vargas.facturacion.service.interfaces.TrabajoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/trabajos")
@RequiredArgsConstructor
public class TrabajoController {

    private final TrabajoService trabajoService;
    private final ClienteRepository clienteRepository;

    @GetMapping
    public String listarTrabajos(
            @RequestParam(required = false) EstadoPago estado,
            @RequestParam(required = false) Long clienteId,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {

        List<TrabajoDTO> trabajos = trabajoService.listarTrabajosFiltrados(estado, clienteId);
        model.addAttribute("trabajos", trabajos);
        model.addAttribute("estadosPago", EstadoPago.values());
        model.addAttribute("clientes", clienteRepository.findAll());
        return "trabajos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        if (!model.containsAttribute("trabajo")) {
            model.addAttribute("trabajo", new TrabajoDTO());
        }
        model.addAttribute("clientes", clienteRepository.findAll());
        return "trabajos/formulario";
    }

    @PostMapping
    public String crearTrabajo(
            @Valid @ModelAttribute("trabajo") TrabajoDTO trabajoDTO,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            return "trabajos/formulario";
        }

        try {
            trabajoService.crearTrabajo(trabajoDTO);
            redirectAttributes.addFlashAttribute("success", "Trabajo creado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trabajos";
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(
            @PathVariable Long id,
            @RequestParam EstadoPago estado,
            RedirectAttributes redirectAttributes) {

        try {
            trabajoService.cambiarEstadoPago(id, estado);
            redirectAttributes.addFlashAttribute("success", "Estado actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trabajos";
    }
}