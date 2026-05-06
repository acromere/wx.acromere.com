package com.acromere.wx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest()
@ExtendWith( MockitoExtension.class )
public class NwsApiServiceTest {

	private static final String STATION_ID = "KATL";

	@MockitoBean
	private RestClient restClient;

	@Autowired
	private ObjectMapper mapper;

	@MockitoBean
	private NwsApiService nwsApiService;

	@BeforeEach
	public void setup() {
		ReflectionTestUtils.setField( nwsApiService, "restClient", restClient );
		ReflectionTestUtils.setField( nwsApiService, "mapper", mapper );
	}

	@Test
	void testUpdateStation() throws Exception {
		// given
		WeatherStation initialStation = new WeatherStation( STATION_ID );

		InputStream testDataInputStream = getClass().getResourceAsStream( "/nws.observation.geo.json" );
		assertThat( testDataInputStream ).isNotNull();
		String testData = new String( testDataInputStream.readAllBytes(), StandardCharsets.UTF_8 );

		// Mock fetchObservation
		when( nwsApiService.fetchObservation( eq( STATION_ID ) ) ).thenReturn( testData );
		when( nwsApiService.updateStation( any() ) ).thenCallRealMethod();

		// when
		WeatherStation station = nwsApiService.updateStation( initialStation );

		// then
		verify( nwsApiService, times( 1 ) ).fetchObservation( eq( STATION_ID ) );
		assertThat( station.getId() ).isEqualTo( STATION_ID );
		assertThat( station.getName() ).isEqualTo( "Atlanta, Hartsfield - Jackson Atlanta International Airport" );
	}
}
