package com.conecel.claro.bnas.m2m.generate_document.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeansConfig {
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public Gson gson() {
		return new GsonBuilder()
				.setDateFormat("dd/MM/yyyy HH:mm:ss")
				.create();
	}
}
