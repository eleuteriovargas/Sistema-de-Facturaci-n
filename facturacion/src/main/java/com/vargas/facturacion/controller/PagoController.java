package com.vargas.facturacion.controller;

import com.vargas.facturacion.dto.TrabajoDTO;
import com.vargas.facturacion.service.interfaces.TrabajoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@Controller
@RequiredArgsConstructor
public class PagoController {

    private final TrabajoService trabajoService;

    @GetMapping("/trabajos/{id}/pagar")
    public String mostrarFormularioPago(
            @PathVariable Long id,
            Model model) {

        TrabajoDTO trabajo = trabajoService.obtenerPorId(id);
        BigDecimal pagado = trabajoService.obtenerTotalPagado(id);

        model.addAttribute("trabajo", trabajo);
        model.addAttribute("pagado", pagado);
        return "trabajos/pagar";
    }
}
