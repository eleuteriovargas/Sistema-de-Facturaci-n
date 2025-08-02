package com.vargas.facturacion.controller;

import com.vargas.facturacion.dto.TrabajoDTO;
import com.vargas.facturacion.model.enums.EstadoPago;
import com.vargas.facturacion.service.interfaces.TrabajoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final TrabajoService trabajoService;

    @GetMapping("/")
    public String mostrarDashboard(Model model) {
        // Obtener trabajos pendientes no vencidos
        List<TrabajoDTO> pendientes = trabajoService.listarTrabajosPorEstado(EstadoPago.PENDIENTE)
                .stream()
                .filter(t -> t.getFechaVencimiento().isAfter(LocalDate.now()) ||
                        t.getFechaVencimiento().isEqual(LocalDate.now()))
                .collect(Collectors.toList());

        List<TrabajoDTO> vencidos = new ArrayList<>();

        // Trabajos con estado VENCIDO explícito
        vencidos.addAll(trabajoService.listarTrabajosPorEstado(EstadoPago.VENCIDO));

        // Trabajos pendientes con fecha vencida (por si no se actualizó el estado)
        vencidos.addAll(trabajoService.listarTrabajosPorEstado(EstadoPago.PENDIENTE)
                .stream()
                .filter(t -> t.getFechaVencimiento().isBefore(LocalDate.now()))
                .collect(Collectors.toList()));

        // Eliminar duplicados (por si algún trabajo aparece en ambas consultas)
        vencidos = vencidos.stream()
                .distinct()
                .collect(Collectors.toList());

        // Asegurar montos no nulos
        pendientes.forEach(t -> t.setMonto(t.getMonto() != null ? t.getMonto() : BigDecimal.ZERO));
        vencidos.forEach(t -> t.setMonto(t.getMonto() != null ? t.getMonto() : BigDecimal.ZERO));

        model.addAttribute("pendientes", pendientes);
        model.addAttribute("vencidos", vencidos);
        return "dashboard";
    }
}