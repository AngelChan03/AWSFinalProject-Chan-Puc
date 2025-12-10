package com.aws.awsproject.dto;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class ProfessorDTO {

    @NotNull(message = "Numero empleado is required")
    @Positive(message = "Numero empleado must be positive")
    private Integer numeroEmpleado;

    @NotBlank(message = "Nombres is required")
    private String nombres;

    @NotBlank(message = "Apellidos is required")
    private String apellidos;

    @NotNull(message = "Horas clase is required")
    @Positive(message = "Horas clase must be positive")
    private Integer horasClase;

}
