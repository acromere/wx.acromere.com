package com.acromere.wx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;

import java.lang.invoke.MethodHandles;
import java.time.Instant;

/**
 * Represents a request for weather data from the National Weather Service
 * (NWS). The NWS provides an <a href="https://www.weather.gov/documentation/services-web-api">API Web Service</a>
 * to request weather data:
 * <p>
 * <a href="https://www.weather.gov/documentation/services-web-api">weather.gov API</a>
 *
 */
public class NwsDataRequest {

	private final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );

	private final ObjectMapper mapper;

	private final RestClient restClient;

	public static final String BASE_URL = "https://api.weather.gov";

	public static final String STATION_OBSERVATION = "/stations/{stationId}/observations/latest";

	public NwsDataRequest( RestClient.Builder builder, ObjectMapper mapper ) {
		this.restClient = builder.baseUrl( BASE_URL ).build();
		this.mapper = mapper;
	}

	public WeatherStation updateStation( WeatherStation station ) {
		String data = fetchObservation( station.getId() );

		System.out.println( data );

		// Parse the weather data
		JsonNode root = mapper.readTree( data );
		JsonNode geometry = root.get( "geometry" );
		JsonNode properties = root.get( "properties" );
		ArrayNode coordinates = geometry.withArray( "coordinates" );
		coordinates.get( 0 ).asDouble();

		String id = properties.get( "stationId" ).asString( "" );
		String name = properties.get( "stationName" ).asString( "" );
		String timestamp = properties.get( "timestamp" ).asString( "" );
		double temperature = getTemperature( properties.get( "temperature" ) );
		double dewPoint = getTemperature( properties.get( "dewpoint" ) );
		double windDirection = getAngle( properties.get( "windDirection" ) );
		double windSpeed = getSpeed( properties.get( "windSpeed" ) );
		double windGust = getSpeed( properties.get( "windGust" ) );
		double humidity = getHumidity( properties.get( "relativeHumidity" ) );
		double pressure = getPressure( properties.get( "barometricPressure" ) );

		double longitude = coordinates.get( 0 ).asDouble();
		double latitude = coordinates.get( 1 ).asDouble();
		double elevation = getDistance( properties.get( "elevation" ) );

		if( !station.getId().equals( id ) ) log.warn( "Station id mismatch: {} != {}", station.getId(), id );
		station.setName( name );
		station.setTimestamp( Instant.parse( timestamp ).toEpochMilli() );
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

		station.setLatitude( latitude );
		station.setLongitude( longitude );
		station.setElevation( elevation );

		return station;
	}

	String fetchObservation( String stationId ) {
		return restClient
			.get()
			.uri( STATION_OBSERVATION, stationId )
			.header( "Accept", "application/geo+json" )
			.header( "User-Agent", "(wx.acromere.com, contact@acromere.com)" )
			.retrieve()
			.body( String.class );
	}

	double getTemperature( JsonNode node ) {
		String quality = getQuality( node );
		if( "Z".equals( quality ) ) return Double.NaN;

		String unit = node.get( "unitCode" ).asString();
		double value = node.get( "value" ).asDouble();

		double scale;
		if( unit.endsWith( "degC" ) ) {
			scale = 1.0;
		} else if( unit.endsWith( "degF" ) ) {
			scale = 9.0 / 5.0;
		} else {
			scale = 1.0;
		}

		return value * scale;
	}

	double getHumidity( JsonNode node ) {
		String quality = getQuality( node );
		if( "Z".equals( quality ) ) return Double.NaN;

		String unit = node.get( "unitCode" ).asString();
		double value = node.get( "value" ).asDouble();

		return value;
	}

	double getPressure( JsonNode node ) {
		String quality = getQuality( node );
		if( "Z".equals( quality ) ) return Double.NaN;

		String unit = node.get( "unitCode" ).asString();
		double value = node.get( "value" ).asDouble();

		return value;
	}

	double getSpeed( JsonNode node ) {
		String quality = getQuality( node );
		if( "Z".equals( quality ) ) return Double.NaN;

		String unit = node.get( "unitCode" ).asString();
		double value = node.get( "value" ).asDouble();

		double scale;
		if( unit.endsWith( "km_h-1" ) ) {
			scale = 1.0;
		} else if( unit.endsWith( "mi_h-1" ) ) {
			scale = 0.621371;
		} else {
			scale = 0.0;
		}
		return value * scale;
	}

	double getDistance( JsonNode node ) {
		String quality = getQuality( node );
		if( "Z".equals( quality ) ) return Double.NaN;

		String unit = node.get( "unitCode" ).asString();
		double value = node.get( "value" ).asDouble();

		double scale;
		if( unit.endsWith( "m" ) ) {
			scale = 1.0;
		} else if( unit.endsWith( "ft" ) ) {
			scale = 0.3048;
		} else {
			scale = 0.0;
		}

		return value * scale;
	}

	double getAngle( JsonNode node ) {
		String quality = getQuality( node );
		if( "Z".equals( quality ) ) return Double.NaN;

		String unit = node.get( "unitCode" ).asString();
		double value = node.get( "value" ).asDouble();

		return value;
	}

	String getQuality( JsonNode node ) {
		JsonNode qualityNode = node.get( "qualityControl" );
		return qualityNode == null ? "V" : qualityNode.asString();
	}

}
