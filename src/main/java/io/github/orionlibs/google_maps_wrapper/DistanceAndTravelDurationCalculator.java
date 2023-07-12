package io.github.orionlibs.google_maps_wrapper;

import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.GeoApiContext.Builder;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
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
        long connectionTimeout = Utils.validateConnectionTimeout(config);
        int retries = Utils.validateRetries(config);
        Builder requestBuilder = new Builder();
        requestBuilder.apiKey(apiKey);
        requestBuilder.connectTimeout(connectionTimeout, TimeUnit.SECONDS);
        requestBuilder.readTimeout(15, TimeUnit.SECONDS);
        requestBuilder.maxRetries(retries);
        GeoApiContext geoAPIContext = requestBuilder.build();
        try
        {
            DirectionsApiRequest request = new DirectionsApiRequest(geoAPIContext);
            request.alternatives(true);
            request.destination(postcode2);
            request.optimizeWaypoints(false);
            request.origin(postcode1);
            request.mode(TravelMode.DRIVING);
            request.units(Unit.IMPERIAL);
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
                return Optional.<DistanceAndTravelDuration>of(DistanceAndTravelDuration.builder()
                                .distance(24.795f)
                                .travelDurationInSeconds(6400)
                                .build());
            }
        }
    }
}