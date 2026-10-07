package com.conecel.claro.bnas.m2m.generate_document.api;

import com.conecel.claro.bnas.m2m.generate_document.bean.general.ResponseGeneric;
import com.conecel.claro.bnas.m2m.generate_document.bean.template.TemplateDTO;
import com.conecel.claro.bnas.m2m.generate_document.common.anotation.ResponseGenericWrapper;
import com.conecel.claro.bnas.m2m.generate_document.exception.NotFoundException;
import com.conecel.claro.bnas.m2m.generate_document.service.TemplateService;
import com.conecel.claro.bnas.m2m.generate_document.util.ApplicationConstant;
import com.conecel.claro.bnas.m2m.generate_document.util.MessageDirectory;
import com.conecel.claro.bnas.m2m.generate_document.util.UsefulFunctions;
import io.swagger.annotations.*;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = "Configure DocumentTemplate")
@Validated
@RestController
@RequestMapping("/document/config/documentTemplate")
public class TemplateController {

    private static final Logger log = LogManager.getLogger(TemplateController.class);

    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @ResponseGenericWrapper
    @ApiOperation(value = "create entity documentTemplate", nickname = "createDocumentTemplate", notes = "createEntityDocument", response = ResponseGeneric.class, responseContainer = "Map", tags = {
            "Configure DocumentTemplate" })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful config documentTemplate", response = ResponseGeneric.class) })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public void configSequence(
            @ApiParam @RequestHeader(value = "operationId", required = false) String operationId,
            @ApiParam @RequestHeader(value = "transactionId", required = false) String externalTransactionId,
            @NotNull(message = "Debe enviar el request de creacion de entidad documentTemplate") @ApiParam(value = "request de creacion sequence") @Valid @RequestBody TemplateDTO requestCreate,
            HttpServletRequest request)
            throws Exception {
        long startTransactionTime = System.currentTimeMillis();
        addTransactionData(operationId, externalTransactionId, request);
        try {
            log.info("Se realiza invocacion de request= " + requestCreate);
            templateService.create(requestCreate);
        } finally {
            CloseableThreadContext.put(ApplicationConstant.TRANSACTION_TIME, (System.currentTimeMillis() - startTransactionTime) + "Ms");
        }
    }

    @ResponseGenericWrapper
    @ApiOperation(value = "get documentTemplate", nickname = "getDocumentTemplate", notes = "getDocumentTemplate", response = ResponseEntity.class, responseContainer = "Map", tags = {
            "Configure DocumentTemplate" })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful config documentTemplates", response = ResponseEntity.class) })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public TemplateDTO get(
            @ApiParam @RequestHeader(value = "operationId", required = false) String operationId,
            @ApiParam @RequestHeader(value = "transactionId", required = false) String externalTransactionId,
            @ApiParam @PathVariable(value = "id", required = false) String id,
            HttpServletRequest request) throws NotFoundException {
        long startTransactionTime = System.currentTimeMillis();
        addTransactionData(operationId, externalTransactionId, request);
        try {
            return templateService.get(id).orElseThrow(()->new NotFoundException(MessageDirectory.E_NOT_FOUND_TEMPLATE, id));
        } finally {
            CloseableThreadContext.put(ApplicationConstant.TRANSACTION_TIME, (System.currentTimeMillis() - startTransactionTime) + "Ms");
        }
    }

    @ResponseGenericWrapper
    @ApiOperation(value = "get all documentTemplates", nickname = "getAllDocumentTemplates", notes = "getAllDocumentTemplates", response = ResponseEntity.class, responseContainer = "List", tags = {
            "Configure DocumentTemplate" })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful config documentTemplates", response = ResponseEntity.class) })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TemplateDTO> getAll(
            @ApiParam @RequestHeader(value = "operationId", required = false) String operationId,
            @ApiParam @RequestHeader(value = "transactionId", required = false) String externalTransactionId,
            HttpServletRequest request) {
        long startTransactionTime = System.currentTimeMillis();
        addTransactionData(operationId, externalTransactionId, request);
        try {
            return templateService.getAll();
        } finally {
            CloseableThreadContext.put(ApplicationConstant.TRANSACTION_TIME, (System.currentTimeMillis() - startTransactionTime) + "Ms");
        }
    }

    @ResponseGenericWrapper
    @ApiOperation(value = "delete documentTemplate", nickname = "deleteDocumentTemplate", notes = "deleteDocumentTemplate", response = ResponseEntity.class, responseContainer = "Map", tags = {
            "Configure DocumentTemplate" })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful config documentTemplates", response = ResponseEntity.class) })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(
            @ApiParam @RequestHeader(value = "operationId", required = false) String operationId,
            @ApiParam @RequestHeader(value = "transactionId", required = false) String externalTransactionId,
            @ApiParam @PathVariable(value = "id") String id,
            HttpServletRequest request)  throws NotFoundException  {
        long startTransactionTime = System.currentTimeMillis();
        addTransactionData(operationId, externalTransactionId, request);
        try {
            templateService.delete(id);
        } finally {
            CloseableThreadContext.put(ApplicationConstant.TRANSACTION_TIME, (System.currentTimeMillis() - startTransactionTime) + "Ms");
        }
    }

    public void addTransactionData(String operationId, String externalTransactionId, HttpServletRequest request) {
        String transactionId = UsefulFunctions.getTransactionId();

        final CloseableThreadContext.Instance ctc = CloseableThreadContext.put(ApplicationConstant.TRANSACTION_ID, transactionId);
        ctc.put(ApplicationConstant.TRANSACTION_EXTERNAL_ID, externalTransactionId);
        ctc.put(ApplicationConstant.IP_CLIENT, request.getRemoteAddr());
        ctc.put(ApplicationConstant.IP_SERVER, UsefulFunctions.getLocalAddr());

        ThreadContext.put("code", String.valueOf(HttpStatus.OK.value()));
        ThreadContext.put("operationId", operationId);
        ThreadContext.put(ApplicationConstant.TRANSACTION_DATE, System.currentTimeMillis() + "");
    }
}
