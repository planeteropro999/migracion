package com.conecel.claro.bnas.m2m.generate_document.swagger.model;

import java.io.Serializable;

import com.conecel.claro.bnas.m2m.generate_document.bean.generate.CommonHeaderResponse;
import com.conecel.claro.bnas.m2m.generate_document.bean.generate.ResponseGenerate;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateResponseMessage implements Serializable {


	@JsonInclude(Include.NON_NULL)
	private String message;
	@JsonInclude(Include.NON_NULL)
	private String code;
	@JsonInclude(Include.NON_NULL)
	private String codeCatch;
	@JsonInclude(Include.NON_NULL)
	private String description;

	@JsonInclude(Include.NON_NULL)
	private ResponseGenerate response;

	@JsonInclude(Include.NON_NULL)
	private CommonHeaderResponse commonHeaderResponse;

	/**
	 * ligthGenerateResponse constructor para la generacion del response, el cual no
	 * incluye el archivo de base64, este metodo es usuado en log4j2
	 *
	 * @return the string
	 */
	public String generateLiteRespose() {
		String responseValue = "";
		boolean isEmpty = this.response != null;
		if (this.getResponse() != null) {
			responseValue = this.getResponse().toLiteString();
		}

		return "GenerateResponseMessage [message=" + message + ", code=" + code + ", codeCatch=" + codeCatch
				+ ", description=" + description + (isEmpty ? ", response=" + responseValue : "")
				+ ", commonHeaderResponse=" + commonHeaderResponse + "]";
	}

}
