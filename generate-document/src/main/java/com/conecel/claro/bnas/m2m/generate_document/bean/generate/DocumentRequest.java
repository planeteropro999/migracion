package com.conecel.claro.bnas.m2m.generate_document.bean.generate;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.conecel.claro.bnas.m2m.generate_document.enums.TypeDocument;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentRequest implements Serializable {


	@NotEmpty(message = "Id de plantilla es requerido")
	private String idTemplate;
	@NotNull(message = "Formato es requerido")
	private TypeDocument format;
	@NotEmpty(message = "Name es requerido")
	private String name;
	@Valid
	List<Item> params;
	CommonHeaderRequest commonHeaderRequest;

	/**
	 * Metodo para la creación de un objeto sin valores en los params este método es
	 * usado en logger
	 *
	 * @return the string
	 */
	public String toLiteRequest() {
		String parameterValue = "";
		boolean vacio = this.getParams() != null && !this.getParams().isEmpty();
		if (vacio)
			parameterValue = params.stream().map(item -> String.format("%s", item.getKey()))
					.collect(Collectors.joining("; ")) + "; numero de parametros recibidos " + this.getParams().size();
		return "DocumentRequest [idTemplate=" + idTemplate + ", format=" + format + ", name=" + name
				+ ", commonHeaderRequest=" + commonHeaderRequest + (vacio ? ",params=" + parameterValue : "") + "]";
	}

}
