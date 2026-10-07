package com.conecel.claro.bnas.m2m.generate_document.util;

import com.conecel.claro.bnas.m2m.generate_document.bean.generate.Item;
import com.conecel.claro.bnas.m2m.generate_document.enums.TypeValue;
import com.conecel.claro.bnas.m2m.generate_document.exception.CustomException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.net.InetAddress;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UsefulFunctions {
	private UsefulFunctions(){}

	private static final Logger logger = LogManager.getLogger(UsefulFunctions.class.getName());

	public static String getTransactionId() {
		return UUID.randomUUID().toString();
	}

	public static String getLocalAddr() {
		String ipHost = null;
		try {
			ipHost = System.getenv("IP_HOST");
		} catch (Exception e) {
			logger.error("No se pudo obtener la variable de entorno: {}", e::getMessage);
		}
		if (ipHost == null) {
			try {
				return InetAddress.getLocalHost().getHostAddress();
			} catch (Exception e) {
				logger.error("No local address: {}", e::getMessage);
				return null;
			}
		}
		return ipHost;
	}


	public static boolean isNumeric(final String str) {
		if (str == null || str.isEmpty()) {
			return false;
		}
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;

	}

	public static void validAnnotation(Object object, Class<?> group) throws CustomException {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<Object>> violations = validator.validate(object, group);
			if (!violations.isEmpty()) {
				String errors = violations.stream()
						.map(ConstraintViolation::getMessage)
						.collect(Collectors.joining(", "));
				throw new CustomException(HttpStatus.BAD_REQUEST, errors);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static void logRequestToFile(List<Item> params, String transactionId) {
		params.forEach(e -> {
			if (e.getTypeValue().equals(TypeValue.LIST) || e.getValue() instanceof List) {
				List<Object> value = (List<Object>) e.getValue();
				logger.info("key: {}, size report: {}, transactionId: {}", e.getKey(), value.size(), transactionId);
			} else {
				logger.info("key: {}, value {}, transactionId: {}", e.getKey(), e.getValue(), transactionId);
			}
		});
	}

}
