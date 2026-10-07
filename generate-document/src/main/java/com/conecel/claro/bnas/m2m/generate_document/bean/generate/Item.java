package com.conecel.claro.bnas.m2m.generate_document.bean.generate;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.conecel.claro.bnas.m2m.generate_document.enums.TypeValue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item implements Serializable {

	@NotEmpty(message = "Key requerido")
	private String key;
	private TypeValue typeValue = TypeValue.TEXT;
	@NotNull(message = "Value requerido")
	private Object value;

	public Item(String key, Object value) {
		this.key = key;
		this.value = value;
	}

}
