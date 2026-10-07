package com.conecel.claro.bnas.m2m.generate_document.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BaseException extends Exception {

	private HttpStatus httpStatus;
	private Object[] paramaters;

	public BaseException() {
	}
	
	public BaseException(BaseException e) {
		super(e);
		this.paramaters = e.getParamaters();
		this.httpStatus = e.getHttpStatus();
	}

	public BaseException(String message) {
		super(message);
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public BaseException(String message, Object... parameters) {
		super(message);
		this.paramaters = parameters;
		this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
	}

	public BaseException(HttpStatus httpStatus, String message, Object... parameters) {
		super(message);
		this.paramaters = parameters;
		this.httpStatus = httpStatus;
	}

}
