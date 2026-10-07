package com.conecel.claro.bnas.m2m.generate_document.service;

import com.conecel.claro.bnas.m2m.generate_document.bean.template.TemplateDTO;
import com.conecel.claro.bnas.m2m.generate_document.exception.NotFoundException;
import com.conecel.claro.bnas.m2m.generate_document.repository.dao.TemplateDAO;
import com.conecel.claro.bnas.m2m.generate_document.exception.CustomException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @version 1.0.1 Se cambio el uso de un service de mongo por uno de oracle
 * @author Luis Vargas <mailto:luis.vargas@gizlocorp.com>
 */
@Service
public class TemplateService implements Serializable {
	private final TemplateDAO templateDAO;
	public TemplateService(TemplateDAO templateDAO) {
		this.templateDAO = templateDAO;
	}

	public void create(TemplateDTO entity) throws CustomException {
		templateDAO.create(entity);
	}
	
	public List<TemplateDTO> getAll(){
		return templateDAO.getAll();
	}
	
	public Optional<TemplateDTO> get(String id) {
		return templateDAO.get(id);
	}
	
	public void delete(String id) throws NotFoundException {
		templateDAO.delete(id);
	}
}
