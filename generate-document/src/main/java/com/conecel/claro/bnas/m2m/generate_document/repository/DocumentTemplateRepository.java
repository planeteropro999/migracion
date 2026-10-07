package com.conecel.claro.bnas.m2m.generate_document.repository;

import com.conecel.claro.bnas.m2m.generate_document.repository.model.TemplateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DocumentTemplateRepository extends JpaRepository<TemplateEntity, String> {
    Optional<TemplateEntity> findByTemplateId(String templateId);
}
