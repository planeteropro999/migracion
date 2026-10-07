package com.conecel.claro.bnas.m2m.generate_document.repository.dao;

import com.conecel.claro.bnas.m2m.generate_document.bean.template.TemplateDTO;
import com.conecel.claro.bnas.m2m.generate_document.bean.template.TemplateDTO.TemplateDTOValidator;
import com.conecel.claro.bnas.m2m.generate_document.exception.CustomException;
import com.conecel.claro.bnas.m2m.generate_document.exception.NotFoundException;
import com.conecel.claro.bnas.m2m.generate_document.repository.DocumentTemplateRepository;
import com.conecel.claro.bnas.m2m.generate_document.util.JsonUtil;
import com.conecel.claro.bnas.m2m.generate_document.util.MessageDirectory;
import com.conecel.claro.bnas.m2m.generate_document.util.UsefulFunctions;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.conecel.claro.bnas.m2m.generate_document.repository.model.TemplateEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * @version 1.0.1
 * @author Luis Vargas <mailto:luis.vargas@gizlocorp.com>
 */
@Component
public class TemplateDAO {
	private final DocumentTemplateRepository documentTemplateRepository;
	private static final Logger log = LogManager.getLogger(TemplateDAO.class);
	private final Gson gson;
	private final JsonUtil jsonUtil;
	private final Function<TemplateDTO, TemplateEntity> mapDtoToEntity;
	private final Function<TemplateEntity, TemplateDTO> mapEntityToDto;
	public TemplateDAO(DocumentTemplateRepository documentTemplateRepository, Gson gson, JsonUtil jsonUtil) {
		this.documentTemplateRepository = documentTemplateRepository;
		this.gson = gson;
		this.jsonUtil = jsonUtil;
		this.mapDtoToEntity = e -> this.gson.fromJson(this.gson.toJson(e), TemplateEntity.class);
		this.mapEntityToDto = e -> this.gson.fromJson(this.gson.toJson(e), TemplateDTO.class);
	}

	public List<TemplateDTO> getAll(){
		List<TemplateEntity> entities = documentTemplateRepository.findAll();
		List<TemplateDTO> result = jsonUtil.convertJsonToList(gson.toJson(entities));
		log.info("Cantidad de registros obtenidos: {}", result.size());
		return result;
	}

	public Optional<TemplateDTO> get(String templateId) {
		Optional<TemplateEntity> optionalTemplate = documentTemplateRepository.findByTemplateId(templateId);
		if(!optionalTemplate.isPresent()){
			log.warn("No se encontró la plantilla con ID: {}", templateId);
			return Optional.empty();
		}
		TemplateDTO dto = this.mapEntityToDto.apply(optionalTemplate.get());
		log.info("Plantilla encontrada con ID: {}", templateId);
		return Optional.of(dto);
	}

	public void create(TemplateDTO request) throws CustomException {
		UsefulFunctions.validAnnotation(request, TemplateDTOValidator.class);
		log.info("Creando nueva plantilla con ID: {}", request.getTemplateId());
		TemplateEntity entity = mapDtoToEntity.apply(request);
		documentTemplateRepository.save(entity);
		log.info("Plantilla creada con ID: {}", request.getTemplateId());
	}

	public void delete(String id) throws NotFoundException {
		Optional<TemplateEntity> optionalTemplate = documentTemplateRepository.findByTemplateId(id);
		if (!optionalTemplate.isPresent()) {
			log.warn("No se encontró la plantilla para eliminar con ID: {}", id);
			throw new NotFoundException(MessageDirectory.E_NOT_FOUND_TEMPLATE, id);
		}
		documentTemplateRepository.delete(optionalTemplate.get());
		log.info("Plantilla eliminada con ID: {}", id);
	}
}
