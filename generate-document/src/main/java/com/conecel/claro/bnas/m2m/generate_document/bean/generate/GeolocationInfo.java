package com.conecel.claro.bnas.m2m.generate_document.bean.generate;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeolocationInfo implements Serializable {
	

	private String accuracy;
	private Location location;
}
