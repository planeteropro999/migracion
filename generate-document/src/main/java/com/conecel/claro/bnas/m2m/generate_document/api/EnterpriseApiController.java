package com.conecel.claro.bnas.m2m.generate_document.api;

import com.conecel.claro.bnas.m2m.generate_document.bean.generate.DocumentRequest;
import com.conecel.claro.bnas.m2m.generate_document.bean.generate.ResponseGenerate;
import com.conecel.claro.bnas.m2m.generate_document.common.anotation.ResponseGenericWrapper;
import com.conecel.claro.bnas.m2m.generate_document.service.GenerateDocumentService;
import com.conecel.claro.bnas.m2m.generate_document.swagger.model.GenerateResponseMessage;
import com.conecel.claro.bnas.m2m.generate_document.util.ApplicationConstant;
import com.conecel.claro.bnas.m2m.generate_document.util.UsefulFunctions;
import io.swagger.annotations.*;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@javax.annotation.Generated(value = "com.conecel.claro.bnas.wfm.generate_document.codegen.languages.SpringCodegen", date = "2019-02-12T14:27:01.801Z")

@Api(value = "Document", description = "Create Document", tags = "Document")
@Log4j2
@RestController
@Validated
@RequestMapping(path = "/document")
public class EnterpriseApiController {

	private final GenerateDocumentService generateDocumentService;

	public EnterpriseApiController(GenerateDocumentService generateDocumentService) {
		this.generateDocumentService = generateDocumentService;
	}

	/**
	 * Create document. Metodo para la creacion de documentos PDF,HTML,TEXT,DOC.
	 *
	 * @param operationId           the operation id
	 * @param transactionId         the transaction id
	 * @param externalTransactionId the external transaction id
	 * @param documentRequest       the document request
	 * @return the response entity
	 */
	@ResponseGenericWrapper
	@ApiOperation(value = "create Document", nickname = "createDocument", notes = "Create Document", response = GenerateResponseMessage.class, responseContainer = "List", tags = {
			"Document", })
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "successful created", response = ResponseEntity.class, responseContainer = "List") })

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseGenerate createDocument(
			@ApiParam(value = "", required = false) @RequestHeader(value = "operationId", required = false) String operationId,
			@ApiParam(value = "", required = false) @RequestHeader(value = "transactionId", required = false) String externalTransactionId,
			@NotNull(message = "request no puede ser nulo") @Valid @RequestBody DocumentRequest documentRequest,
			HttpServletRequest request)
			throws HttpServerErrorException, HttpClientErrorException, IOException, Exception {

		long startTransactionTime = System.currentTimeMillis();
		String transactionId = UsefulFunctions.getTransactionId();
		String transactionIdProcess = StringUtils.hasText(externalTransactionId) ? externalTransactionId: transactionId;

		final CloseableThreadContext.Instance ctc = CloseableThreadContext.put(ApplicationConstant.TRANSACTION_ID, transactionId);
		ctc.put(ApplicationConstant.TRANSACTION_EXTERNAL_ID, externalTransactionId);
		ctc.put(ApplicationConstant.IP_CLIENT, request.getRemoteAddr());
		ctc.put(ApplicationConstant.IP_SERVER, UsefulFunctions.getLocalAddr());

		ThreadContext.put(ApplicationConstant.TRANSACTION_ID, transactionId);
		ThreadContext.put(ApplicationConstant.TRANSACTION_EXTERNAL_ID, externalTransactionId);
		ThreadContext.put(ApplicationConstant.TRANSACTION_DATE, System.currentTimeMillis() + "");

		try {
			log.debug("Se realiza invocacion de request= " + documentRequest.toLiteRequest());
			return generateDocumentService.createDocument(transactionIdProcess, documentRequest);
		} finally {
			ctc.put(ApplicationConstant.TRANSACTION_TIME, (System.currentTimeMillis() - startTransactionTime) + "Ms");
		}
	}
}
