package com.acromere.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * The <a href="https://weatherflow.github.io/Tempest/api/">Tempest Weather Station API</a> service.
 */
@Service
public class TempestApiService implements StationUpdateRequest {

	public static final String BASE_URL = "https://swd.weatherflow.com/swd/rest";

	public static final String STATION_OBSERVATION = BASE_URL + "/observations/station/{stationId}?token={accessToken}";

	private final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );

	private final RestClient restClient;

	private final ObjectMapper mapper;

	@Value( "${security.tempest.token}" )
	private String accessToken;

	public TempestApiService( RestClient.Builder builder, ObjectMapper mapper ) {
		this.restClient = builder.baseUrl( BASE_URL ).build();
		this.mapper = mapper;
	}

	@Override
	public WeatherStation updateStation( WeatherStation station ) {
		System.out.println( "Updating station " + station.getId() );
		System.out.println( "Access token: " + accessToken );

		String data = fetchObservation( station.getId() );

		//System.out.println( data );

		// Parse the weather data
		JsonNode root = mapper.readTree( data );
		ArrayNode obs = root.withArray( "obs" );
		JsonNode observation = obs.get( 0 );

		String id = root.get( "station_id" ).asString( "" );
		String name = root.get( "station_name" ).asString( "" );

		// Timestamp is in local time
		String timestamp = observation.get( "timestamp" ).asString( "" );
		String timezone = root.get( "timezone" ).asString( "" );

		//		double temperature = getTemperature( observation.get( "temperature" ) );
		//		double dewPoint = getTemperature( observation.get( "dewpoint" ) );
		//		double windDirection = getAngle( observation.get( "windDirection" ) );
		//		double windSpeed = getSpeed( observation.get( "windSpeed" ) );
		//		double windGust = getSpeed( observation.get( "windGust" ) );
		//		double humidity = getHumidity( observation.get( "relativeHumidity" ) );
		//		double pressure = getPressure( observation.get( "barometricPressure" ) );
		//
		//		double longitude = coordinates.get( 0 ).asDouble();
		//		double latitude = coordinates.get( 1 ).asDouble();
		//		double elevation = getDistance( observation.get( "elevation" ) );

		if( !station.getId().equals( id ) ) log.warn( "Station id mismatch: {} != {}", station.getId(), id );
		station.setName( name );

		// Timestamp
		Instant localDateTime = Instant.ofEpochSecond( Long.parseLong( timestamp ) );
		ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant( localDateTime, ZoneId.of( timezone ) );
		station.setTimestamp( zonedDateTime.toInstant().toEpochMilli() );

		//		station.setTemperature( temperature );
		//		station.setTemperatureUnit( Unit.DEG_C );
		//		station.setDewPoint( dewPoint );
		//		station.setWindDirection( windDirection );
		//		station.setWindDirectionUnit( Unit.DEGREE );
		//		station.setWindSpeed( windSpeed );
		//		station.setWindSpeedUnit( Unit.KPH );
		//		station.setWindGust( windGust );
		//		station.setHumidity( humidity );
		//		station.setHumidityUnit( Unit.PERCENT );
		//		station.setPressure( pressure );
		//		station.setPressureUnit( Unit.PASCAL );
		//
		//		station.setLatitude( latitude );
		//		station.setLongitude( longitude );
		//		station.setElevation( elevation );

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
