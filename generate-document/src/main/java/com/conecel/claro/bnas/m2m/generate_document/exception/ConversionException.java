package com.conecel.claro.bnas.m2m.generate_document.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ConversionException extends BaseException {

	public ConversionException() {
	}

	public ConversionException(String message) {
		super(message);
	}

	public ConversionException(String message, Object... parameters) {
		super(message, parameters);
	}

	public ConversionException(HttpStatus httpStatus, String message, Object... parameters) {
		super(httpStatus, message, parameters);
	}
}
