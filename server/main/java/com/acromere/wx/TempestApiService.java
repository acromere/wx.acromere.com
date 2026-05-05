package com.acromere.wx;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

/**
 * The <a href="https://weatherflow.github.io/Tempest/api/">Tempest Weather Station API</a> service.
 */
@Service
public class TempestApiService implements StationUpdateRequest {

	public static final String BASE_URL = "https://swd.weatherflow.com/swd/rest";

	public static final String STATION_OBSERVATION = BASE_URL + "/observations/station/{stationId}?token={accessToken}";

	private final RestClient restClient;

	private final ObjectMapper mapper;

	private @Value( "${security.tempest.token:}" ) String accessToken;

	public TempestApiService( RestClient.Builder builder, ObjectMapper mapper ) {
		this.restClient = builder.baseUrl( BASE_URL ).build();
		this.mapper = mapper;
	}

	@Override
	public WeatherStation updateStation( WeatherStation station ) {
		String data = fetchObservation( station.getId() );

		System.out.println( data );

		// Parse the weather data
		JsonNode root = mapper.readTree( data );

		return station;
	}

	String fetchObservation( String stationId ) {
		return restClient
			.get()
			.uri( STATION_OBSERVATION, stationId, accessToken )
			.header( "Accept", "application/json" )
			.header( "User-Agent", "(wx.acromere.com, contact@acromere.com)" )
			.retrieve()
			.body( String.class );
	}
}
