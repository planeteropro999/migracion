package com.conecel.claro.bnas.m2m.generate_document;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
public class GenerateApplication {

    public static void main(String[] args) {
        new SpringApplication(GenerateApplication.class).run(args);
    }
    
    @Bean
    public MessageSource messageSource() {
       ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
       messageSource.setBasename("classpath:messages");
       messageSource.setDefaultEncoding("UTF-8");
       messageSource.setCacheSeconds(86400); //refresh cache once 24 hour
       return messageSource;
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
       LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
       bean.setValidationMessageSource(messageSource());
       return bean;
    }

}
