package com.acromere.wx;

import org.springframework.web.client.RestClient;

/**
 * Represents a request for weather data from the National Weather Service (NWS).
 * <p>
 * https://www.weather.gov/documentation/services-web-api
 *
 */
public class NwsDataRequest {

    private final RestClient restClient;

    public static final String BASE_URL = "https://api.weather.gov";

    public static final String STATION_OBSERVATION = "/stations/{stationId}/observations/latest";

    public NwsDataRequest(RestClient.Builder builder) {
        this.restClient = builder.baseUrl(BASE_URL).build();
    }

    public String fetchObservation(String stationId) {
        return restClient.get()
                .uri(STATION_OBSERVATION, stationId)
                .retrieve()
                .body(String.class);
    }

}
