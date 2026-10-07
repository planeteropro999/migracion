package com.conecel.claro.bnas.m2m.generate_document.bean.generate;

import java.io.Serializable;

import com.conecel.claro.bnas.m2m.generate_document.enums.TypeDocument;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGenerate implements Serializable {


	@JsonInclude(Include.NON_NULL)
	private String base64;
	@JsonInclude(Include.NON_NULL)
	private String idTemplate;
	@JsonInclude(Include.NON_NULL)
	private String format;
	@JsonInclude(Include.NON_NULL)
	private String extension;
	@JsonInclude(Include.NON_NULL)
	private String name;

	@JsonInclude(Include.NON_NULL)
	private String mediaType;

	public String toLiteString() {
		return "ResponseGenerate [idTemplate=" + idTemplate + ", format=" + format + ", extension=" + extension
				+ ", name=" + name + ", mediaType=" + mediaType + "]";
	}
	
	public void setPropertyDocument(TypeDocument type) {
		this.format=type.getType();
		this.extension=type.getExtension();
		this.mediaType=type.getMediaType();
	}

}
