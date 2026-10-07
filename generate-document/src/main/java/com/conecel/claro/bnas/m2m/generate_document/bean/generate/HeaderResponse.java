package com.conecel.claro.bnas.m2m.generate_document.bean.generate;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HeaderResponse implements Serializable {
	
	/**
	 * 
	 */
	private String externalTransactionDate;
	private String responseTime;
	private String internalTransactionId;
	private String externalTransactionId;
	private String token;

}
