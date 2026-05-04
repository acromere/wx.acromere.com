package com.acromere.wx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

@Service
public class TempestApiService implements StationUpdateRequest {

	private static final String BASE_URL = "";

	private RestClient restClient;

	private ObjectMapper mapper;

	private String clientId;

	private @Value( "${security.tempest.pat:}" ) String clientSecret;

	public TempestApiService( RestClient.Builder builder, ObjectMapper mapper ) {
		this.restClient = builder.baseUrl( BASE_URL ).build();
		this.mapper = mapper;
	}

	@Override
	public WeatherStation updateStation( WeatherStation station ) {
		return station;
	}

}
