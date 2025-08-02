package com.vargas.facturacion.service.impl;

import com.vargas.facturacion.model.entity.Trabajo;
import com.vargas.facturacion.model.enums.EstadoPago;
import com.vargas.facturacion.repository.TrabajoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VencimientoService {

    private final TrabajoRepository trabajoRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Ejecutar diariamente a medianoche
    public void actualizarEstadosVencidos() {
        List<Trabajo> pendientes = trabajoRepository.findByEstadoPago(EstadoPago.PENDIENTE);
        LocalDate hoy = LocalDate.now();

        pendientes.forEach(t -> {
            if (t.getFechaVencimiento().isBefore(hoy)) {
                t.setEstadoPago(EstadoPago.VENCIDO);
                t.setNotas("Marcado como vencido automáticamente el " + hoy);
                trabajoRepository.save(t);
            }
        });
    }
}
