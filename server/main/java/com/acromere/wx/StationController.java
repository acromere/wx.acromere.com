package com.acromere.wx;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class StationController {

	private final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );

	@Getter
	private final Map<String, WeatherStation> stations = new ConcurrentHashMap<>();

	@CrossOrigin( origins = "*" )
	@RequestMapping( method = RequestMethod.GET, path = "/station" )
	public @ResponseBody WeatherStation getStation( @RequestParam( value = "id" ) String id ) {
		return stations.computeIfAbsent( id, this::initializeStation );
	}

	private WeatherStation initializeStation( String id ) {
		log.info( "Initialising station with id: {}", id );

		WeatherStation station = new WeatherStation( id );

		// TODO Fill out the station information

		return station;
	}

}
