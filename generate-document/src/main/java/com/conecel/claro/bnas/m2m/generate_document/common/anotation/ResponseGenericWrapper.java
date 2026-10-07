package com.conecel.claro.bnas.m2m.generate_document.common.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author luis.penaherrera@gizlocorp.com
 * @version 1.0
 * -Method annotation 
 * -Indica que la respuesta del metodo debe ser empaquetada en un objeto en la clase {@link ResponseGeneric}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseGenericWrapper {

}