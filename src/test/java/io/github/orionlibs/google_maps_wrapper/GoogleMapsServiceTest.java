package io.github.orionlibs.google_maps_wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.orionlibs.google_maps_wrapper.log.ListLogHandler;
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
    void setUp()
    {
        listLogHandler = new ListLogHandler();
        GoogleMapsService.addLogHandler(listLogHandler);
    }


    @AfterEach
    public void teardown()
    {
        GoogleMapsService.removeLogHandler(listLogHandler);
    }


    @Test
    void test_getFormattedPostcode() throws Exception
    {
        assertEquals("SW1A 1AA", googleMapsService.getFormattedPostcode("SW1A1AA"));
        //assertTrue(true);
        /*ConfigurationService.updateProp("orionlibs.prop", "false");
        mockMvc.perform(get("/")).andExpect(status().isOk());
        assertTrue(listLogHandler.getLogRecords().stream()
                        .anyMatch(record -> record.getMessage().contains("hello world")));
        ConfigurationService.updateProp("orionlibs.prop", "true");*/
    }
}
