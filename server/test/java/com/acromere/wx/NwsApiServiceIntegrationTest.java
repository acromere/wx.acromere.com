package com.acromere.wx;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

// This test should generally be disabled. It can be enabled for local development.
@Disabled
@SpringBootTest
public class NwsApiServiceIntegrationTest {

	private static final String STATION_ID = "HERUT";

	@Autowired
	private NwsApiService nwsApiService;

	@Test
	void testFetchObservation() {
		// given
		WeatherStation station = new WeatherStation( STATION_ID );

		// when
		WeatherStation updatedStation = nwsApiService.updateStation( station );

		// then
		assertThat( updatedStation.getId() ).isEqualTo( STATION_ID );
	}

}
