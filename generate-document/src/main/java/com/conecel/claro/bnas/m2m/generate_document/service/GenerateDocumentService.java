package com.conecel.claro.bnas.m2m.generate_document.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.xml.transform.stream.StreamSource;

import com.conecel.claro.bnas.m2m.generate_document.bean.template.TemplateDTO;
import com.conecel.claro.bnas.m2m.generate_document.util.UsefulFunctions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONException;
import org.json.XML;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;
import org.w3c.tidy.Tidy;

import com.conecel.claro.bnas.m2m.generate_document.bean.generate.DocumentRequest;
import com.conecel.claro.bnas.m2m.generate_document.bean.generate.Item;
import com.conecel.claro.bnas.m2m.generate_document.bean.generate.ResponseGenerate;
import com.conecel.claro.bnas.m2m.generate_document.repository.dao.TemplateDAO;
import com.conecel.claro.bnas.m2m.generate_document.exception.MicroServiceException;
import com.conecel.claro.bnas.m2m.generate_document.exception.NotFoundException;

import com.conecel.claro.bnas.m2m.generate_document.util.ExcelUtil;
import com.conecel.claro.bnas.m2m.generate_document.util.MessageDirectory;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

import net.minidev.json.JSONArray;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XsltExecutable;
import net.sf.saxon.s9api.XsltTransformer;

/**
 * @version 1.0.1
 * @author Luis Vargas <mailto:luis.vargas@gizlocorp.com>
 * Se realizo un refactori para usar constantes.
 */
@Service
public class GenerateDocumentService implements Serializable {
	private final Logger log = LogManager.getLogger(GenerateDocumentService.class);
	private static final String UTF_8 = "UTF-8";
	private final TemplateDAO templateDAO;
	private final ExcelUtil excelUtil;

	public GenerateDocumentService(TemplateDAO templateDAO, ExcelUtil excelUtil) {
		this.templateDAO = templateDAO;
		this.excelUtil = excelUtil;
	}

	public ResponseGenerate createDocument(String transactionId, DocumentRequest documentRequest)
			throws NotFoundException, MicroServiceException {
		ResponseGenerate responseGenerate = new ResponseGenerate();

		Optional<TemplateDTO> optionalTemplateDTO = templateDAO.get(documentRequest.getIdTemplate());
		if (optionalTemplateDTO.isPresent()) {
			TemplateDTO templateDTO = optionalTemplateDTO.get();
			if (Strings.isNotBlank(templateDTO.getTemplate())) {
				JSONArray dataDoc = getTramaDocumento(documentRequest);
				UsefulFunctions.logRequestToFile(documentRequest.getParams(), transactionId);
				log.info("Crear document: {}, transactionId: {}", documentRequest.getFormat(), transactionId);
				switch (documentRequest.getFormat()) {
				case PDF:
					responseGenerate.setBase64(createPDF(transactionId,templateDTO.getTemplate(), dataDoc));
					break;
				case HTML:
					responseGenerate.setBase64(createDocumentBase(transactionId, templateDTO.getTemplate(), true, dataDoc));
					break;
				case TEXT:
					responseGenerate.setBase64(createTextPlane(transactionId, templateDTO.getTemplate(), dataDoc));
					break;
				case EXCEL:
					responseGenerate.setBase64(createExcel(transactionId, templateDTO.getTemplate(), dataDoc));
					break;
                default:
                    break;
				}
			} else {
				throw new NotFoundException(MessageDirectory.E_NOT_FOUND_TEMPLATE, documentRequest.getIdTemplate());
			}
		} else {
			throw new NotFoundException(MessageDirectory.E_NOT_FOUND_CONF, documentRequest.getIdTemplate());
		}

		responseGenerate.setPropertyDocument(documentRequest.getFormat());
		responseGenerate.setIdTemplate(documentRequest.getIdTemplate());
		responseGenerate.setName(documentRequest.getName());
		log.info("Fin de la creación de document, transactionId: {}", transactionId);
		return responseGenerate;
	}

	public JSONArray getTramaDocumento(DocumentRequest documentRequest) {
		if (documentRequest.getParams() == null)
			documentRequest.setParams(new ArrayList<>());
		documentRequest.getParams().add(new Item("titleDoc", documentRequest.getName()));
		documentRequest.getParams().add(new Item("typeDoc", documentRequest.getFormat().name()));
		JSONArray dataDoc = new JSONArray();
		dataDoc.addAll(documentRequest.getParams());
		log.debug("Message: Datos para uso en plantilla {}", dataDoc::toJSONString);
		return dataDoc;
	}

	public String createDocumentBase(String transactionId, String plantilla, boolean base64, JSONArray dataDoc) throws MicroServiceException {
		log.info("Creando documento base, transactionId: {}", transactionId);
		String result;
		StringWriter writer;

		plantilla = XML.unescape(plantilla);
		String xml;
		try {
			xml = XML.toString(dataDoc, "data");
		} catch (JSONException e1) {
			log.error("Error al convertir el JSON en XML: {}, transactionId: {}", e1.getMessage(), transactionId);
			throw new MicroServiceException(MessageDirectory.E_CONVERTER_PARAMETER);
		}
		Processor processor = new Processor(false);
		XsltExecutable xsltExec;
		try {
			xsltExec = processor.newXsltCompiler().compile(new StreamSource(new StringReader(plantilla)));

			XsltTransformer transformer = xsltExec.load();

			transformer.setSource(new StreamSource(new StringReader(xml)));

			writer = new StringWriter();
			transformer.setDestination(processor.newSerializer(writer));
			transformer.transform();
			result = writer.toString();

			if (base64)
				result = Base64.getEncoder().encodeToString(result.getBytes(StandardCharsets.UTF_8));
			else
				return result;

		} catch (SaxonApiException e) {
			log.debug("Ocurrio un error transformando parametros con la plantilla configurada: {}, transactionId: {}", e.getMessage(), transactionId);
			throw new MicroServiceException(MessageDirectory.E_CONVERTER_DOCUMENT);
		}

		try {
			// always is true
			writer.close();
		} catch (Exception e) {
			log.error("Error al cerrar variable writer: {}, transactionId: {}", e.getMessage(), transactionId);
			throw new MicroServiceException(MessageDirectory.E_DOCUMENT_CLOSE_WRITE);
		}

		return result;

	}

	public String createTextPlane(String transactionId, String plantillaXslt, JSONArray dataDoc) throws MicroServiceException {
		String result;

		String html = createDocumentBase(transactionId,plantillaXslt, false, dataDoc);

        result = Jsoup.parse(html).text();
        result = Base64.getEncoder().encodeToString(result.getBytes(StandardCharsets.UTF_8));

        return result;
	}

	public String createExcel(String transactionId, String plantillaXslt, JSONArray dataDoc) throws MicroServiceException {
		String html = createDocumentBase(transactionId, plantillaXslt, false, dataDoc);
		try {
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			Workbook workbook = excelUtil.htmlToExcel(html);
			workbook.write(b);
			return Base64.getEncoder().encodeToString(b.toByteArray());
		} catch (IOException e) {
			log.debug("Error en objecto de salida del renderizado: {}, transactionId: {}", e.getMessage(), transactionId);
			throw new MicroServiceException(MessageDirectory.E_DOCUMENT_OUT_RENDER);
		}
	}

	public String createPDF(String transactionId, String plantillaXslt, JSONArray dataDoc) throws MicroServiceException {
		ByteArrayOutputStream os;
		String processedHtml;
		String result;
		String html = createDocumentBase(transactionId, plantillaXslt, false, dataDoc);
		// try {
		processedHtml = convertToXhtml(html);
		/*
		 * os = new ByteArrayOutputStream(); ITextRenderer renderer = new
		 * ITextRenderer(); renderer.setDocumentFromString(processedHtml, null);
		 * renderer.layout(); renderer.createPDF(os); renderer.finishPDF(); result =
		 * Base64.getEncoder().encodeToString(os.toByteArray()); result =
		 * result.replace("\n", "");
		 */

		/*
		 * } catch (DocumentException | IOException e) {
		 * log.debug("Error en objecto de salida del renderizado: {}", e::getMessage);
		 * throw new MicroServiceException(MessageDirectory.E_DOCUMENT_OUT_RENDER); }
		 */

		try {
			os = new ByteArrayOutputStream();
			PdfRendererBuilder builder = new PdfRendererBuilder();
			builder.withHtmlContent(processedHtml, "");
			builder.toStream(os);
			builder.run();
			result = Base64.getEncoder().encodeToString(os.toByteArray());
			result = result.replace("\n", "");
		} catch (Exception e) {
			log.info("Error en objecto de salida del renderizado: {}, transactionId: {}", e.getMessage(), transactionId);
			throw new MicroServiceException(MessageDirectory.E_DOCUMENT_OUT_RENDER);
		}

		return result;
	}

	private String convertToXhtml(String html) throws MicroServiceException {
		ByteArrayInputStream inputStream;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			Tidy tidy = new Tidy();
			tidy.setInputEncoding(UTF_8);
			tidy.setOutputEncoding(UTF_8);
			tidy.setXHTML(true);
			tidy.setQuiet(true);
			tidy.setShowWarnings(false);
			inputStream = new ByteArrayInputStream(html.getBytes(StandardCharsets.UTF_8));
			tidy.parseDOM(inputStream, outputStream);
			return outputStream.toString(UTF_8);
		} catch (UnsupportedEncodingException e) {
			log.info("Ocurrio un error transformando el documento html a xhtml: {}", e.getMessage());
			throw new MicroServiceException(MessageDirectory.E_CONVERTER_DOCUMENT_PROCESS_TEMPLADE);
		}
	}

}
