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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest()
@ExtendWith( MockitoExtension.class )
public class TempestApiServiceTest {

	private static final String STATION_ID = "215817";

	@MockitoBean
	private RestClient restClient;

	@Autowired
	private ObjectMapper mapper;

	@MockitoBean
	private TempestApiService tempestApiService;

	@BeforeEach
	public void setup() {
		ReflectionTestUtils.setField(tempestApiService, "restClient", restClient);
		ReflectionTestUtils.setField(tempestApiService, "mapper", mapper);
	}

	@Test
	void testUpdateStation() throws Exception {
		// given
		WeatherStation initialStation = new WeatherStation( STATION_ID );

		InputStream testDataInputStream = getClass().getResourceAsStream( "/tempest.observation.json" );
		assertThat( testDataInputStream ).isNotNull();
		String testData = new String( testDataInputStream.readAllBytes(), StandardCharsets.UTF_8 );

		// Mock fetchObservation
		when( tempestApiService.fetchObservation( eq( STATION_ID ) ) ).thenReturn( testData );
		when( tempestApiService.updateStation( any() ) ).thenCallRealMethod();

		// when
		WeatherStation station = tempestApiService.updateStation( initialStation );

		// then
		verify( tempestApiService, times( 1 ) ).fetchObservation( eq( STATION_ID ) );
		assertThat( station.getId() ).isEqualTo( STATION_ID );
		assertThat( station.getName() ).isEqualTo( "Bluewing Way" );
	}

}
