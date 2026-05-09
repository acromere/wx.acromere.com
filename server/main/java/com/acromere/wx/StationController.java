package com.acromere.wx;

import jakarta.annotation.security.PermitAll;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@PermitAll
@RestController
public class StationController {

	private final Logger log = LoggerFactory.getLogger( MethodHandles.lookup().lookupClass() );

	private final NwsApiService nwsApiService;

	private final TempestApiService tempestApiService;

	@Getter
	private final Map<String, WeatherStation> stations = new ConcurrentHashMap<>();

	public StationController( NwsApiService nwsApiService, TempestApiService tempestApiService ) {
		this.nwsApiService = nwsApiService;
		this.tempestApiService = tempestApiService;
	}

	@CrossOrigin( origins = "*" )
	@RequestMapping( method = RequestMethod.GET, path = "/api/station/nws" )
	public @ResponseBody WeatherStation getNwsStation( @RequestParam( value = "id" ) String id ) {
		return updateStation( id, nwsApiService );
	}

	@CrossOrigin( origins = "*" )
	@RequestMapping( method = RequestMethod.GET, path = "/api/station/tempest" )
	public @ResponseBody WeatherStation getTempestStation( @RequestParam( value = "id" ) String id ) {
		return updateStation( id, tempestApiService );
	}

	private WeatherStation updateStation( String id, StationApiService request ) {
		WeatherStation computedStation = stations.computeIfAbsent( id, WeatherStation::new );
		WeatherStation station = request.updateStation( computedStation );

		station.updateExtendedValues();
		station.updateFlightConditions();

		return station;
	}

}
