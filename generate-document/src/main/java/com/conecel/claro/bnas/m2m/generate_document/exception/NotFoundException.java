package com.conecel.claro.bnas.m2m.generate_document.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class NotFoundException extends BaseException {

	public NotFoundException() {
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Object... paramaters) {
		super(HttpStatus.NOT_FOUND,message, paramaters);
	}

	public NotFoundException(HttpStatus httpStatus, String message, Object... paramaters) {
		super(httpStatus, message, paramaters);
	}
}
