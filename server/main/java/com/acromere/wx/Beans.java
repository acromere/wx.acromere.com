package com.acromere.wx;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Component
public class Beans {

	private final RestClient.Builder builder;

	private final ObjectMapper mapper;

	public Beans( RestClient.Builder builder, ObjectMapper mapper ) {
		this.builder = builder;
		this.mapper = mapper;
	}

	@Bean
	public NwsApiService getNwsDataRequest() {
		return new NwsApiService( builder, mapper );
	}

	@Bean
	public TempestApiService getTempestApiService() {
		return new TempestApiService( builder, mapper );
	}



}
