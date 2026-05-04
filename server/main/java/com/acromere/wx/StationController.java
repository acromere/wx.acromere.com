package com.acromere.wx;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class StationController {

    private final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RestClient.Builder builder;

    private final ObjectMapper mapper;

    @Getter
    private final Map<String, WeatherStation> stations = new ConcurrentHashMap<>();

    public StationController(RestClient.Builder builder, ObjectMapper mapper) {
        this.builder = builder;
        this.mapper = mapper;
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.GET, path = "/api/station")
    public @ResponseBody WeatherStation getStation(@RequestParam(value = "id") String id) {
        return updateStation(id, new NwsDataRequest(builder, mapper));
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.GET, path = "/api/station/nws")
    public @ResponseBody WeatherStation getNoaaStation(@RequestParam(value = "id") String id) {
        return updateStation(id, new NwsDataRequest(builder, mapper));
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(method = RequestMethod.GET, path = "/api/station/tempest")
    public @ResponseBody WeatherStation getTempestStation(@RequestParam(value = "id") String id) {
        return updateStation(id, new TempestDataRequest(builder, mapper));
    }

    private WeatherStation updateStation(String id, StationUpdateRequest request) {
        WeatherStation computedStation = stations.computeIfAbsent(id, WeatherStation::new);
        WeatherStation station = request.updateStation(computedStation);

        station.updateExtendedValues();
        station.updateFlightConditions();

        return station;
    }
}
