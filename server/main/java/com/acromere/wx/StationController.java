package com.acromere.wx;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class StationController {

	private final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );

	private final RestClient.Builder builder;

	private final ObjectMapper mapper;

	@Getter
	private final Map<String, WeatherStation> stations = new ConcurrentHashMap<>();

	public StationController( RestClient.Builder builder, ObjectMapper mapper ) {
		this.builder = builder;
		this.mapper = mapper;
	}

	@CrossOrigin( origins = "*" )
	@RequestMapping( method = RequestMethod.GET, path = "/api/station" )
	public @ResponseBody WeatherStation getStation( @RequestParam( value = "id" ) String id ) {
		return updateStation( stations.computeIfAbsent( id, this::initializeStation ) );
	}

	private WeatherStation initializeStation( String id ) {
		return new WeatherStation( id );
	}

	private WeatherStation updateStation( WeatherStation station ) {
		WeatherStation updatedStation = new NwsDataRequest( builder, mapper ).updateStation( station );

		updatedStation.updateExtendedValues();
		updatedStation.updateFlightConditions();

		return updatedStation;
	}

}
