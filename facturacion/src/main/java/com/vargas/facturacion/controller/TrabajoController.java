package com.vargas.facturacion.controller;

import com.vargas.facturacion.dto.TrabajoDTO;
import com.vargas.facturacion.exception.BusinessException;
import com.vargas.facturacion.model.entity.Trabajo;
import com.vargas.facturacion.model.enums.EstadoPago;
import com.vargas.facturacion.repository.ClienteRepository;
import com.vargas.facturacion.service.interfaces.ClienteService;
import com.vargas.facturacion.service.interfaces.TrabajoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/trabajos")
@RequiredArgsConstructor
@Slf4j
public class TrabajoController {

    private final TrabajoService trabajoService;
    private final ClienteRepository clienteRepository;
    private final ClienteService clienteService;


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
        TrabajoDTO trabajo = new TrabajoDTO();
        trabajo.setEstadoPago(EstadoPago.PENDIENTE); // Establecer valor por defecto
        trabajo.setFechaTrabajo(LocalDate.now());
        trabajo.setFechaVencimiento(LocalDate.now().plusDays(7));


        model.addAttribute("trabajo", trabajo);
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
            TrabajoDTO trabajoActual = trabajoService.obtenerPorId(id);

            // Mostrar mensaje especial si ya está pagado
            if (trabajoActual.getEstadoPago() == EstadoPago.PAGADO) {
                redirectAttributes.addFlashAttribute("warning",
                        "Este trabajo ya está pagado y no puede modificarse");
                return "redirect:/trabajos";
            }

            TrabajoDTO trabajoActualizado = trabajoService.cambiarEstadoPago(id, estado);

            if (trabajoActualizado.getEstadoPago() == EstadoPago.PAGADO) {
                redirectAttributes.addFlashAttribute("success",
                        "Trabajo marcado como pagado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("success",
                        "Estado actualizado a " + estado.getDescripcion());
            }
        } catch (BusinessException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            log.warn("Intento de modificar trabajo pagado - ID: {}", id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al actualizar estado: " + e.getMessage());
            log.error("Error al cambiar estado del trabajo ID: {}", id, e);
        }

        return "redirect:/trabajos";
    }

    // Agregar método
    @PostMapping("/{id}/pagos")
    public String registrarPago(
            @PathVariable Long id,
            @RequestParam @DecimalMin("0.01") BigDecimal monto,
            RedirectAttributes redirectAttributes) {

        try {
            trabajoService.registrarPago(id, monto);
            redirectAttributes.addFlashAttribute("success", "Pago registrado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/trabajos";
    }

    @PostMapping("/{id}/pagar")
    public String registrarPagoCompleto(
            @PathVariable Long id,
            @RequestParam(required = false) BigDecimal monto,
            RedirectAttributes redirectAttributes) {

        try {
            if (monto == null) {
                // Si no se especifica monto, pagar el total
                trabajoService.cambiarEstadoPago(id, EstadoPago.PAGADO);
            } else {
                // Lógica para pagos parciales
                trabajoService.registrarPago(id, monto);
            }

            redirectAttributes.addFlashAttribute("success", "Pago registrado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al registrar pago: " + e.getMessage());
        }
        return "redirect:/trabajos";
    }

}