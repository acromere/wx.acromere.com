package com.acromere.wx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles( "local" )
public class TempestApiServiceTest {

	private static final String STATION_ID = "215817";

	@Autowired
	TempestApiService tempestApiService;

	@Test
	void testFetchObservation() {
		// given
		WeatherStation station = new WeatherStation( STATION_ID );

		// when
		WeatherStation updatedStation = tempestApiService.updateStation( station );

		//System.out.println( updatedStation.toString() );

		// then
		assertThat( updatedStation.getId() ).isEqualTo( STATION_ID );
	}

}
