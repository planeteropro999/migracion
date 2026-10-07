package com.conecel.claro.bnas.m2m.generate_document.util;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author Gizlo
 *
 */
@Component
public class JsonUtil {

	private final Gson gson;

	public JsonUtil(Gson gson) {
		this.gson = gson;
	}

	/**
	 * @since  1.0 Convierte los json a un tipo lista
	 * @author Luis Vargas <mailto:luis.vargas@gizlocorp.com>
	 */
	public <T> List<T> convertJsonToList(final String json){
		Type personaListType = new TypeToken<List<T>>() {}.getType();
		return gson.fromJson(json, personaListType);
	}

}
