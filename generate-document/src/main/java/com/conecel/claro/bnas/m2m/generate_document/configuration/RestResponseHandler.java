package com.conecel.claro.bnas.m2m.generate_document.configuration;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.conecel.claro.bnas.m2m.generate_document.bean.general.ResponseGeneric;
import com.conecel.claro.bnas.m2m.generate_document.common.anotation.ResponseGenericWrapper;

@ControllerAdvice
public class RestResponseHandler implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.hasMethodAnnotation(ResponseGenericWrapper.class);
	}

	@Override
	public Object beforeBodyWrite(Object arg0, MethodParameter arg1, MediaType arg2,
			Class<? extends HttpMessageConverter<?>> arg3, ServerHttpRequest arg4, ServerHttpResponse arg5) {
		return new ResponseGeneric<>().success(arg0);
	}

}