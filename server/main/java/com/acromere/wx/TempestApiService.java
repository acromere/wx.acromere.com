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

/**
 * The <a href="https://weatherflow.github.io/Tempest/api/">Tempest Weather Station API</a> service.
 */
@Service
public class TempestApiService implements StationApiService {

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
		// Fetch the weather data
		String data = fetchObservation( station.getId() );

		// Parse the weather data
		JsonNode root = mapper.readTree( data );
		ArrayNode obs = root.withArray( "obs" );
		JsonNode observation = obs.get( 0 );
		JsonNode units = root.get( "station_units" );

		// Units
		String directionUnit = units.get( "units_direction" ).asString();
		String distanceUnit = units.get( "units_distance" ).asString();
		String humidityUnit = "%";
		String pressureUnit = units.get( "units_pressure" ).asString();
		String precipitationUnit = units.get( "units_precip" ).asString();
		String temperatureUnit = units.get( "units_temp" ).asString();
		String speedUnit = units.get( "units_wind" ).asString();
		String elevationUnit = "m";

		// Static metrics
		String id = root.get( "station_id" ).asString( "" );
		String name = root.get( "station_name" ).asString( "" );
		double latitude = root.get( "latitude" ).asDouble();
		double longitude = root.get( "longitude" ).asDouble();
		double elevation = getElevation( root.get( "elevation" ), elevationUnit );

		// Dynamic metrics
		long timestamp = observation.get( "timestamp" ).asLong( 0 );
		double temperature = getTemperature( observation.get( "air_temperature" ), temperatureUnit );
		double dewPoint = getTemperature( observation.get( "dew_point" ), temperatureUnit );
		double windDirection = getDirection( observation.get( "wind_direction" ), directionUnit );
		double windSpeed = getSpeed( observation.get( "wind_avg" ), speedUnit );
		double windGust = getSpeed( observation.get( "wind_gust" ), speedUnit );
		double humidity = getHumidity( observation.get( "relative_humidity" ), humidityUnit );
		double pressure = getPressure( observation.get( "sea_level_pressure" ), pressureUnit );

		// Double-check the station ids
		if( !station.getId().equals( id ) ) log.warn( "Station id mismatch: {} != {}", station.getId(), id );

		// Store the static metrics
		station.setName( name );
		station.setLatitude( latitude );
		station.setLongitude( longitude );
		station.setElevation( elevation );

		// Store the dymanic metrics
		station.setTimestamp( timestamp * 1000 );
		station.setTemperature( temperature );
		station.setTemperatureUnit( Unit.DEG_C );
		station.setDewPoint( dewPoint );
		station.setWindDirection( windDirection );
		station.setWindDirectionUnit( Unit.DEGREE );
		station.setWindSpeed( windSpeed );
		station.setWindSpeedUnit( Unit.KPH );
		station.setWindGust( windGust );
		station.setHumidity( humidity );
		station.setHumidityUnit( Unit.PERCENT );
		station.setPressure( pressure );
		station.setPressureUnit( Unit.PASCAL );

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

	private double getTemperature( JsonNode node, String unit ) {
		double value = node.asDouble();

		double scale;
		if( unit.equals( "c" ) ) {
			scale = 1.0;
		} else if( unit.endsWith( "f" ) ) {
			scale = 9.0 / 5.0;
		} else {
			scale = Double.NaN;
		}

		return value * scale;
	}

	private double getSpeed( JsonNode node, String unit ) {
		double value = node.asDouble();

		double scale;
		if( unit.endsWith( "kph" ) ) {
			scale = 1.0;
		} else if( unit.endsWith( "mph" ) ) {
			scale = 0.621371;
		} else {
			scale = Double.NaN;
		}
		return value * scale;
	}

	private double getPressure( JsonNode node, String unit ) {
		double value = node.asDouble();

		double scale;
		if( unit.endsWith( "pa" ) ) {
			scale = 1.0;
		} else if( unit.endsWith( "mb" ) ) {
			scale = 100.0;
		} else {
			scale = Double.NaN;
		}

		return value * scale;
	}

	private double getHumidity( JsonNode node, String unit ) {
		return node.asDouble();
	}

	private double getDirection( JsonNode node, String unit ) {
		return node.asDouble();
	}

	private double getElevation( JsonNode node, String unit ) {
		double value = node.asDouble();

		double scale;
		if( unit.endsWith( "m" ) ) {
			scale = 1.0;
		} else if( unit.endsWith( "ft" ) ) {
			scale = 0.3048;
		} else {
			scale = Double.NaN;
		}

		return value * scale;
	}

}
