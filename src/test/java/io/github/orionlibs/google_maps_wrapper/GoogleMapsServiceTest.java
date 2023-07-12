package io.github.orionlibs.google_maps_wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.github.orionlibs.google_maps_wrapper.log.ListLogHandler;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@TestInstance(Lifecycle.PER_METHOD)
@Execution(ExecutionMode.CONCURRENT)
public class GoogleMapsServiceTest
{
    private ListLogHandler listLogHandler;
    private GoogleMapsService googleMapsService;


    @BeforeEach
    void setUp() throws IOException
    {
        listLogHandler = new ListLogHandler();
        GoogleMapsService.addLogHandler(listLogHandler);
        googleMapsService = new GoogleMapsService(new PostcodeFormatter.FakePostcodeFormatter());
    }


    @AfterEach
    public void teardown()
    {
        GoogleMapsService.removeLogHandler(listLogHandler);
    }


    @Test
    void test_formatPostcode() throws Exception
    {
        assertFalse(googleMapsService.formatPostcode(null).isPresent());
        assertFalse(googleMapsService.formatPostcode("").isPresent());
        assertEquals("SW1A", googleMapsService.formatPostcode("sW1a1Aa").get());
        assertEquals("W6", googleMapsService.formatPostcode("w69hh").get());
    }
}
