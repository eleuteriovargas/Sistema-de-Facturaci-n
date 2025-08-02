package com.vargas.facturacion.controller;

import com.vargas.facturacion.dto.TrabajoDTO;
import com.vargas.facturacion.model.enums.EstadoPago;
import com.vargas.facturacion.service.interfaces.TrabajoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final TrabajoService trabajoService;

    @GetMapping("/")
    public String mostrarDashboard(Model model) {
        List<TrabajoDTO> pendientes = trabajoService.listarTrabajosPorEstado(EstadoPago.PENDIENTE);

        // Asegurar que los montos no sean nulos
        pendientes.forEach(t -> {
            if (t.getMonto() == null) {
                t.setMonto(BigDecimal.ZERO);
            }
        });

        model.addAttribute("pendientes", pendientes);
        return "dashboard";
    }
}