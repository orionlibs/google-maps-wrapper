package io.github.orionlibs.google_maps_wrapper;

import io.github.orionlibs.google_maps_wrapper.config.ConfigurationService;
import io.github.orionlibs.google_maps_wrapper.config.OrionConfiguration;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class GoogleMapsService
{
    private final static Logger log;
    private PostcodeFormatter postcodeFormatter;

    static
    {
        log = Logger.getLogger(GoogleMapsService.class.getName());
    }

    GoogleMapsService(PostcodeFormatter postcodeFormatter) throws IOException
    {
        this();
        this.postcodeFormatter = postcodeFormatter;
    }


    public GoogleMapsService() throws IOException
    {
        ConfigurationService.registerConfiguration(OrionConfiguration.loadFeatureConfiguration(null));
    }


    public GoogleMapsService(final Properties customConfig) throws IOException
    {
        ConfigurationService.registerConfiguration(OrionConfiguration.loadFeatureConfiguration(customConfig));
    }


    GoogleMapsService(final Properties customConfig, PostcodeFormatter postcodeFormatter) throws IOException
    {
        this(customConfig);
        this.postcodeFormatter = postcodeFormatter;
    }


    public Optional<String> formatPostcode(String postcode) throws MissingApiKeyException
    {
        return postcodeFormatter.run(postcode);
    }


    static void addLogHandler(Handler handler)
    {
        log.addHandler(handler);
    }


    static void removeLogHandler(Handler handler)
    {
        log.removeHandler(handler);
    }
}
