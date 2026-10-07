package com.conecel.claro.bnas.m2m.generate_document.bean.generate;

import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonHeaderResponse {
	@NotNull(message = "Resultado de la invocaci�n del servicio")
	private OperationInfo operationInfo;

	public void addOperationInfo(String transactionId, String externalTransactionId, Date transactionDate) {
		operationInfo = new OperationInfo(transactionId, externalTransactionId, transactionDate);
	}
}
