package com.acromere.wx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class NwsDataRequestTest {

	private static final String STATION_ID = "HERUT";

	@Autowired
	private RestClient.Builder builder;

	@Autowired
	private ObjectMapper mapper;

	@Test
	void testFetchObservation() {
		// given
		WeatherStation station = new WeatherStation( STATION_ID );
		NwsDataRequest nwsDataRequest = new NwsDataRequest( builder, mapper );

		// when
		WeatherStation updatedStation = nwsDataRequest.updateStation( station );

		//System.out.println( updatedStation.toString() );

		// then
		assertThat( updatedStation.getId() ).isEqualTo( STATION_ID );
	}

}
