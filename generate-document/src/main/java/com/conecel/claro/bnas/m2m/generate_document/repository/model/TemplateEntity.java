package com.conecel.claro.bnas.m2m.generate_document.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @version 1.0.1
 * @author Luis Vargas <mailto:luis.vargas@gizlocorp.com>
 * Se realizo la migracion de mongo a Oracle
 */
@Data
@Entity
@Table(name = "TEMPLATE_DM")
@AllArgsConstructor
@NoArgsConstructor
public class TemplateEntity implements Serializable {

	@Id
	@Column(name = "ID_TEMPLATE", unique = true)
	private String templateId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "START_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Guayaquil")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaInicio;

	@Column(name = "END_DATE")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Guayaquil")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaFin;

	@Lob
	@Column(name = "TEMPLATE")
	private String template;

	@Column(name = "STATUS")
	private String estado;
}
