package com.vargas.facturacion.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClienteDTO {
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe ser un email válido")
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
}