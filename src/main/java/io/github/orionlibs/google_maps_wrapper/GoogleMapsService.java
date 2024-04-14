package io.github.orionlibs.google_maps_wrapper;

import com.google.maps.errors.ApiException;
import io.github.orionlibs.google_maps_wrapper.config.ConfigurationService;
import io.github.orionlibs.google_maps_wrapper.config.OrionConfiguration;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

public class GoogleMapsService
{
    private PostcodeFormatter postcodeFormatter;
    private DistanceAndTravelDurationCalculator distanceAndTravelDurationCalculator;
    private ConfigurationService config;


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


    /**
     * It returns the config of this instance of the service.
     * @return
     */
    public ConfigurationService getConfig()
    {
        return config;
    }
}
