package io.github.orionlibs.google_maps_wrapper;

import com.google.maps.errors.ApiException;
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
    private DistanceAndTravelDurationCalculator distanceAndTravelDurationCalculator;
    private ConfigurationService config;

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
        this.config = new ConfigurationService();
        config.registerConfiguration(OrionConfiguration.loadFeatureConfiguration(null));
    }


    public GoogleMapsService(final Properties customConfig) throws IOException
    {
        this.config = new ConfigurationService();
        config.registerConfiguration(OrionConfiguration.loadFeatureConfiguration(customConfig));
    }


    GoogleMapsService(final Properties customConfig, PostcodeFormatter postcodeFormatter) throws IOException
    {
        this(customConfig);
        this.postcodeFormatter = postcodeFormatter;
    }


    GoogleMapsService(final Properties customConfig, DistanceAndTravelDurationCalculator distanceAndTravelDurationCalculator) throws IOException
    {
        this(customConfig);
        this.distanceAndTravelDurationCalculator = distanceAndTravelDurationCalculator;
    }


    GoogleMapsService(DistanceAndTravelDurationCalculator distanceAndTravelDurationCalculator) throws IOException
    {
        this();
        this.distanceAndTravelDurationCalculator = distanceAndTravelDurationCalculator;
    }


    public Optional<String> formatPostcode(String postcode) throws MissingApiKeyException, IOException, InterruptedException, ApiException
    {
        return postcodeFormatter.run(config, postcode);
    }


    public Optional<DistanceAndTravelDuration> getDistanceAndTravelDuration(String postcode1, String postcode2) throws MissingApiKeyException, IOException, InterruptedException, ApiException
    {
        return distanceAndTravelDurationCalculator.run(config, postcode1, postcode2);
    }


    static void addLogHandler(Handler handler)
    {
        log.addHandler(handler);
    }


    static void removeLogHandler(Handler handler)
    {
        log.removeHandler(handler);
    }


    /**
     * It returns the config of this instance of the service.
     * @return
     */
    public ConfigurationService getConfig()
    {
        return config;
    }
}
