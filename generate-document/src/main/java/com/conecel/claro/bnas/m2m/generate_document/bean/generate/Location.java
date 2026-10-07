package com.conecel.claro.bnas.m2m.generate_document.bean.generate;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location implements Serializable {


	@NotEmpty(message = "Latitud de la coordenada")
	private String latitude;
	@NotEmpty(message = "Longitud de la coordenada")
	private String longitude;
}
