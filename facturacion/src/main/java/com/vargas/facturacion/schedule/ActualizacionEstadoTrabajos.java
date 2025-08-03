package com.vargas.facturacion.schedule;

import com.vargas.facturacion.model.enums.EstadoPago;
import com.vargas.facturacion.repository.TrabajoRepository;
import com.vargas.facturacion.service.interfaces.TrabajoService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class ActualizacionEstadoTrabajos {

    private final TrabajoService trabajoService;
    private final TrabajoRepository trabajoRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Se ejecuta cada día a medianoche
    public void actualizarEstadosVencidos() {
        trabajoRepository.findByEstadoPagoAndFechaVencimientoBefore(
                        EstadoPago.PENDIENTE,
                        LocalDate.now())
                .forEach(trabajo -> {
                    trabajo.setEstadoPago(EstadoPago.VENCIDO);
                    trabajoRepository.save(trabajo);
                });
    }
}
