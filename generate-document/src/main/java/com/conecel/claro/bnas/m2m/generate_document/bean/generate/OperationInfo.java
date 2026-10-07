package com.conecel.claro.bnas.m2m.generate_document.bean.generate;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationInfo {
	@ApiModelProperty(value = "Id de la transacci�n")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String transactionId;
    
    @ApiModelProperty(value = "Id de la externalTransactionId")
    String externalTransactionId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss", timezone = "America/Guayaquil")
    @ApiModelProperty(value = "Fecha de la transacci�n")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
	private Date transactionDate;
}
