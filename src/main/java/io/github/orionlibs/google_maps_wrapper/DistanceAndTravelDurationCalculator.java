package io.github.orionlibs.google_maps_wrapper;

import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeoApiContext.Builder;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import io.github.orionlibs.google_maps_wrapper.config.ConfigurationService;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

class DistanceAndTravelDurationCalculator extends AGoogleMapsTask
{
    private int numberOfRetries = 0;


    Optional<DistanceAndTravelDuration> run(ConfigurationService config, String postcode1, String postcode2) throws MissingApiKeyException, IOException, InterruptedException, ApiException
    {
        if(postcode1 == null || postcode1.isEmpty() || postcode2 == null || postcode2.isEmpty())
        {
            return Optional.<DistanceAndTravelDuration>empty();
        }
        String apiKey = config.getProp("orionlibs.google-maps-wrapper.google.maps.api.key");
        Utils.validateAPIKey(apiKey);
        Builder requestBuilder = new Builder();
        requestBuilder.apiKey(apiKey);
        requestBuilder.connectTimeout(Utils.validateConnectionTimeout(config), TimeUnit.SECONDS);
        requestBuilder.readTimeout(15, TimeUnit.SECONDS);
        requestBuilder.maxRetries(Utils.validateRetries(config));
        GeoApiContext geoAPIContext = requestBuilder.build();
        try
        {
            DirectionsApiRequest request = new DirectionsApiRequest(geoAPIContext);
            request.alternatives(true);
            request.destination(postcode2);
            request.optimizeWaypoints(false);
            request.origin(postcode1);
            request.mode(Utils.validateTransportationType(config));
            request.units(Utils.validateDistanceUnit(config));
            float distance = Float.MAX_VALUE;
            long travelDurationInSeconds = 0L;
            DirectionsResult apiResponse = request.await();
            DirectionsRoute[] routes = apiResponse.routes;
            if(routes != null && routes.length > 0)
            {
                DirectionsLeg[] legsOfJourney = routes[0].legs;
                float distanceTemp = legsOfJourney[0].distance.inMeters / 1609.0f;
                if(distanceTemp < distance)
                {
                    distance = distanceTemp;
                    travelDurationInSeconds = legsOfJourney[0].duration.inSeconds;
                    return Optional.<DistanceAndTravelDuration>of(DistanceAndTravelDuration.builder()
                                    .distance(distance)
                                    .travelDurationInSeconds(travelDurationInSeconds)
                                    .build());
                }
            }
        }
        finally
        {
            closeRequest(geoAPIContext);
        }
        return Optional.<DistanceAndTravelDuration>empty();
    }


    public static class FakeDistanceAndTravelDurationCalculator extends DistanceAndTravelDurationCalculator
    {
        @Override
        public Optional<DistanceAndTravelDuration> run(ConfigurationService config, String postcode1, String postcode2) throws MissingApiKeyException
        {
            if(postcode1 == null || postcode1.isEmpty() || postcode2 == null || postcode2.isEmpty())
            {
                return Optional.<DistanceAndTravelDuration>empty();
            }
            else
            {
                String apiKey = config.getProp("orionlibs.google-maps-wrapper.google.maps.api.key");
                Utils.validateAPIKey(apiKey);
                Utils.validateConnectionTimeout(config);
                Utils.validateRetries(config);
                Utils.validateTransportationType(config);
                Utils.validateDistanceUnit(config);
                return Optional.<DistanceAndTravelDuration>of(DistanceAndTravelDuration.builder()
                                .distance(24.795f)
                                .travelDurationInSeconds(6400)
                                .build());
            }
        }
    }
}