package io.github.orionlibs.google_maps_wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.orionlibs.google_maps_wrapper.config.ConfigurationService;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@TestInstance(Lifecycle.PER_METHOD)
@Execution(ExecutionMode.CONCURRENT)
public class DistanceAndTravelDurationCalculatorTest
{
    private ConfigurationService config;
    private GoogleMapsService googleMapsService;


    @BeforeEach
    void setUp() throws IOException
    {
        googleMapsService = new GoogleMapsService(new DistanceAndTravelDurationCalculator.FakeDistanceAndTravelDurationCalculator());
        config = googleMapsService.getConfig();
    }


    @Test
    void test_getDistanceAndTravelDuration() throws Exception
    {
        assertFalse(googleMapsService.getDistanceAndTravelDuration(null, "W6 9HH").isPresent());
        assertFalse(googleMapsService.getDistanceAndTravelDuration("W6 9HH", null).isPresent());
        assertFalse(googleMapsService.getDistanceAndTravelDuration(null, null).isPresent());
        DistanceAndTravelDuration expected = DistanceAndTravelDuration.builder()
                        .distance(24.795f)
                        .travelDurationInSeconds(6400)
                        .build();
        assertEquals(expected, googleMapsService.getDistanceAndTravelDuration("W6 9HH", "SW7 3DP").get());
    }


    @Test
    void test_getDistanceAndTravelDuration_missingAPIKey() throws Exception
    {
        config.updateProp("orionlibs.google-maps-wrapper.google.maps.api.key", "");
        Exception exception = assertThrows(MissingApiKeyException.class, () -> {
            googleMapsService.getDistanceAndTravelDuration("W6 9HH", "SW7 3DP");
        });
        config.updateProp("orionlibs.google-maps-wrapper.google.maps.api.key", "OrionFakeAPIKey");
    }
}
