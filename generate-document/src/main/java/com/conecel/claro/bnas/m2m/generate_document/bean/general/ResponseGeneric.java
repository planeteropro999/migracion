package com.conecel.claro.bnas.m2m.generate_document.bean.general;

import java.io.Serializable;
import java.util.Date;

import com.conecel.claro.bnas.m2m.generate_document.util.ApplicationConstant;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;

import com.conecel.claro.bnas.m2m.generate_document.bean.generate.CommonHeaderResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ResponseGeneric<T> implements Serializable {

	CommonHeaderResponse commonHeaderResponse;
	String code;
	String message;
	String description;
	@JsonInclude(value = Include.NON_NULL)
	T response;

	public void addOperationInfo(String transactionId, String externalTransactionId, Date transactionDate) {
		if (this.commonHeaderResponse == null)
			this.commonHeaderResponse = new CommonHeaderResponse();
		this.commonHeaderResponse.addOperationInfo(transactionId, externalTransactionId, transactionDate);
	}

	public ResponseGeneric<T> success() {
		init("Succesful Transaction", "200", null, null);
		return this;
	}

	public ResponseGeneric<T> success(String message) {
		init(message, "200", null, null);
		return this;
	}

	public ResponseGeneric<T> success(T response) {
		init("Sucessful Response", "200", null, response);
		return this;
	}

	private void init(String message, String code, String description, T response) {
		this.code = code;
		if (message != null)
			this.message = message;
		if (response != null)
			this.response = response;
		if (description != null)
			this.description = description;
		String transactionId = ThreadContext.get(ApplicationConstant.TRANSACTION_ID);
		String externalTransactionId = ThreadContext.get(ApplicationConstant.TRANSACTION_EXTERNAL_ID);
		String transactionDate = ThreadContext.get(ApplicationConstant.TRANSACTION_DATE);
		if (transactionDate != null) {
			Date fecha = new Date(Long.parseLong(transactionDate));
			this.addOperationInfo(transactionId, externalTransactionId, fecha);
		}
	}

	public ResponseGeneric<T> error(String message, HttpStatus httpStatus) {
		init(message, httpStatus.value() + "", httpStatus.getReasonPhrase(), null);
		return this;
	}

	public ResponseGeneric<T> error(String message, String description, HttpStatus httpStatus) {
		init(message, httpStatus.value() + "", description, null);
		return this;
	}
}
