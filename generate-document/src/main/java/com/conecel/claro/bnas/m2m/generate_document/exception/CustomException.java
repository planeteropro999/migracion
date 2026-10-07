package com.conecel.claro.bnas.m2m.generate_document.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class CustomException extends BaseException {

	public CustomException() {
	}

	public CustomException(HttpStatus statusCode, String message) {
		super(statusCode, message);
	}

	public CustomException(HttpStatus statusCode, String message, Object... parameters) {
		super(statusCode, message, parameters);
	}

}
