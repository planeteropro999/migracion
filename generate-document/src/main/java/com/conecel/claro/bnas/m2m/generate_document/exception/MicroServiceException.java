package com.conecel.claro.bnas.m2m.generate_document.exception;

import org.springframework.http.HttpStatus;

public class MicroServiceException extends BaseException {

	public MicroServiceException() {
	}

	public MicroServiceException(String message) {
		super(message);
	}

	public MicroServiceException(String message, Object... parameters) {
		super(message, parameters);
	}

	public MicroServiceException(HttpStatus httpStatus, String message, Object... paramaters) {
		super(httpStatus, message, paramaters);
	}

}
