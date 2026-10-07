package com.conecel.claro.bnas.m2m.generate_document.enums;

import lombok.Getter;

@Getter
public enum TypeValue {
	NUMBER("NUMBER", "Number"), TEXT("TEXT", "Text"), LIST("LIST", "List");

	private final String type;
	private final String value;

	TypeValue(String type, String value) {
		this.type = type;
		this.value = value;
	}
}
