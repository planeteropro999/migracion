package com.conecel.claro.bnas.m2m.generate_document.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.conecel.claro.bnas.m2m.generate_document.exception.CustomException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.conecel.claro.bnas.m2m.generate_document.bean.general.ResponseGeneric;
import com.conecel.claro.bnas.m2m.generate_document.exception.ConversionException;
import com.conecel.claro.bnas.m2m.generate_document.exception.MicroServiceException;
import com.conecel.claro.bnas.m2m.generate_document.exception.NotFoundException;
import com.google.common.base.Strings;

import lombok.extern.log4j.Log4j2;

@Log4j2
@ControllerAdvice
public class GlobalControllerExceptionHandler {

	private final MessageSource messageSource;

	public GlobalControllerExceptionHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public ResponseGeneric handleIllegal(IllegalArgumentException e) {
		ResponseGeneric response = new ResponseGeneric();
		response.error(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		log.error(response.getMessage());
		return response;
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ResponseGeneric handleException(HttpMessageNotReadableException exception) {
		ResponseGeneric response = new ResponseGeneric();
		response.error("Error al invocar servicio, el request enviado no es válido o no es correcto",
				exception.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
		log.error("Error en invocacion del servicio El request enviado no es válido {}", exception::getMessage);
		return response;
	}

	/**
	 * Intercepta los errores por body request list - method params
	 */
	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public ResponseGeneric handleConstraintViolationException(ConstraintViolationException e) {
		List<String> errors = new ArrayList<>();
		for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
			errors.add(violation.getMessage());
		}
		ResponseGeneric response = new ResponseGeneric();
		response.error(errors.toString().replace("[", "").replace("]", ""), HttpStatus.BAD_REQUEST);
		log.error(response.getMessage());
		return response;
	}

	/**
	 * Intercepta los errores captados por javax.validation en el requestBody
	 */
	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.BAD_REQUEST) // 400
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseGeneric handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
		List<String> errors = new ArrayList<>();
		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			errors.add(fieldError.getDefaultMessage());
		}
		ResponseGeneric response = new ResponseGeneric();
		response.error(errors.toString().replace("[", "").replace("]", ""), HttpStatus.BAD_REQUEST);
		log.error(response.getMessage());
		return response;
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(NotFoundException.class)
	@ResponseBody
	public ResponseEntity<Object> handleNotFoundException(NotFoundException e, Locale locale) {
		String message = getMessage(e.getLocalizedMessage(), e.getParamaters(), locale);
		ResponseGeneric response = new ResponseGeneric();
		response.error(message, e.getHttpStatus());
		return new ResponseEntity<>(response, e.getHttpStatus());
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(MicroServiceException.class)
	@ResponseBody
	public ResponseEntity<Object> handleNotFoundException(MicroServiceException e, Locale locale) {
		String message = getMessage(e.getLocalizedMessage(), e.getParamaters(), locale);
		ResponseGeneric response = new ResponseGeneric();
		response.error(message, e.getHttpStatus());
		return new ResponseEntity<>(response, e.getHttpStatus());
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(ConversionException.class)
	@ResponseBody
	public ResponseEntity<Object> handleConvertionException(ConversionException e, Locale locale) {
		String message = getMessage(e.getLocalizedMessage(), e.getParamaters(), locale);
		ResponseGeneric response = new ResponseGeneric();
		response.error(message, e.getHttpStatus());
		return new ResponseEntity<>(response, e.getHttpStatus());
	}

	@SuppressWarnings("rawtypes")
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseGeneric handleOtherException(Exception e) {
		ResponseGeneric response = new ResponseGeneric();
		response.error(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		log.error(response.getMessage());
		return response;
	}

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(CustomException.class)
	@ResponseBody
	public ResponseEntity<Object> handleNotFoundException(CustomException e) {
		log.error(e.toString());
		String message = "";
		try {
			message = messageSource.getMessage(e.getLocalizedMessage(), e.getParamaters(), e.getLocalizedMessage(),
					Locale.getDefault());
			if (!StringUtils.hasText(message))
				message = e.getLocalizedMessage();
		} catch (Exception generalException) {
			message = message != null ? message.concat(generalException.getMessage()) : null;
		}
		ResponseGeneric genericResponse = new ResponseGeneric().error(message, e.getHttpStatus());
		return new ResponseEntity<>(genericResponse,e.getHttpStatus());
	}


	public String getMessage(String localizedMessage, Object[] params, Locale locale) {
		String message = "";
		try {
			message = messageSource.getMessage(localizedMessage, params, locale);
			if (Strings.isNullOrEmpty(message))
				message = localizedMessage;
		} catch (Exception generalException) {
			message = message.concat(generalException.getMessage());
		}
		log.error(message);
		return message;
	}

}
