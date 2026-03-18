package com.acromere.wx;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.web.client.RestClient;

@SpringBootTest
public class NwsDataRequestTest {

    private static final String STATION_ID = "KATL";

    @Autowired
    private RestClient.Builder builder;

    @Test
    void testFetchObservation() {
        NwsDataRequest nwsDataRequest = new NwsDataRequest(builder);
        String observationData = nwsDataRequest.fetchObservation(STATION_ID);
        System.out.println(observationData);
    }

}
