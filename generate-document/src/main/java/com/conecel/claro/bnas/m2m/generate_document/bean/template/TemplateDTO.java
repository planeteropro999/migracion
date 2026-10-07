package com.conecel.claro.bnas.m2m.generate_document.bean.template;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDTO implements Serializable {
    public interface TemplateDTOValidator{}

    @Schema(description = "Identificador único del plantilla", example = "template123")
    @NotBlank(groups = {TemplateDTOValidator.class}, message = "El campo templateId es obligatorio y no puede estar vacío.")
    @NotNull(groups = {TemplateDTOValidator.class}, message = "El campo templateId es obligatorio y no puede ser nulo.")
    private String templateId;

    @Schema(description = "Nombre del plantilla", example = "EventoX")
    @NotBlank(groups = {TemplateDTOValidator.class}, message = "El campo name es obligatorio y no puede estar vacío.")
    @NotNull(groups = {TemplateDTOValidator.class}, message = "El campo name es obligatorio y no puede ser nulo.")
    private String name;

    @Schema(description = "Descripción del plantilla", example = "Descripción detallada de la plantilla.")
    @NotBlank(groups = {TemplateDTOValidator.class}, message = "El campo descripcion es obligatorio y no puede estar vacío.")
    @NotNull(groups = {TemplateDTOValidator.class}, message = "El campo descripcion es obligatorio y no puede ser nulo.")
    private String description;

    @Schema(description = "Fecha y hora de inicio del plantilla en el formato dd/MM/yyyy HH:mm:ss", example = "31/12/2024 23:59:59")
    @NotNull(groups = {TemplateDTOValidator.class}, message = "El campo fechaInicio es obligatorio y no puede ser nulo.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Guayaquil")
    private Date fechaInicio;

    @Schema(description = "Fecha y hora de fin del plantilla en el formato dd/MM/yyyy HH:mm:ss", example = "31/12/2024 23:59:59")
    @NotNull(groups = {TemplateDTOValidator.class}, message = "El campo fechaFin es obligatorio y no puede ser nulo.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Guayaquil")
    private Date fechaFin;

    @Schema(description = "Plantilla a usar en formato de xslt")
    @NotBlank(groups = {TemplateDTOValidator.class}, message = "El campo template es obligatorio y no puede estar vacío.")
    @NotNull(groups = {TemplateDTOValidator.class}, message = "El campo template es obligatorio y no puede ser nulo.")
    private String template;

    @Schema(description = "Estado de la plantilla. Debe ser 'A' para activo o 'I' para inactivo.", example = "A", allowableValues = {"A", "I"})
    @Pattern(groups = {TemplateDTOValidator.class}, regexp = "[AI]", message = "El campo estado debe ser 'A' (activo) o 'I' (inactivo).")
    @NotBlank(groups = {TemplateDTOValidator.class}, message = "El campo estado es obligatorio y no puede estar vacío.")
    @NotNull(groups = {TemplateDTOValidator.class}, message = "El campo estado es obligatorio y no puede ser nulo.")
    private String estado;
}
