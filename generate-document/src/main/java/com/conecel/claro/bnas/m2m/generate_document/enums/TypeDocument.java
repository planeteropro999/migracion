package com.conecel.claro.bnas.m2m.generate_document.enums;

import lombok.Getter;

@Getter
public enum TypeDocument {
	PDF("PDF", "pdf", "application/pdf"), WORD("WORD", "doc", ""), HTML("HTML", "html", "text/html"),
	TEXT("TEXT", "txt", "text/html"), EXCEL("EXCEL", "xlsx", "application/vnd.ms-excel");

	private final String type;
	private final String extension;
	private final String mediaType;

	TypeDocument(String type, String extension, String mediaType) {
		this.type = type;
		this.extension = extension;
		this.mediaType = mediaType;
	}
}
