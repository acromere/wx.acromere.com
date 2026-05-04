package com.acromere.wx;

import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

public class TempestDataRequest implements StationUpdateRequest{

	private static final String BASE_URL = "";

	private RestClient restClient;

	private ObjectMapper mapper;

	public TempestDataRequest( RestClient.Builder builder, ObjectMapper mapper ) {
		this.restClient = builder.baseUrl( BASE_URL ).build();
		this.mapper = mapper;
	}

	@Override
	public WeatherStation updateStation( WeatherStation station ) {
		return station;
	}

}
