package io.github.orionlibs.google_maps_wrapper;

import io.github.orionlibs.google_maps_wrapper.config.ConfigurationService;
import io.github.orionlibs.google_maps_wrapper.config.OrionConfiguration;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Logger;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GoogleMapsService
{
    private final static Logger log;
    private OrionConfiguration featureConfiguration;

    static
    {
        log = Logger.getLogger(GoogleMapsService.class.getName());
    }

    public GoogleMapsService(final Properties customConfig) throws IOException
    {
        this.featureConfiguration = OrionConfiguration.loadFeatureConfiguration(customConfig);
        ConfigurationService.registerConfiguration(featureConfiguration);
    }


    public String getFormattedPostcode(String postcode)
    {
        return new GetFormattedPostcodeTask().run(postcode);
    }


    static void addLogHandler(Handler handler)
    {
        log.addHandler(handler);
    }


    static void removeLogHandler(Handler handler)
    {
        log.removeHandler(handler);
    }


    public static void test()
    {
        log.info("hello world");
    }
}
