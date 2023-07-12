package io.github.orionlibs.google_maps_wrapper;

import com.google.maps.FindPlaceFromTextRequest;
import com.google.maps.FindPlaceFromTextRequest.InputType;
import com.google.maps.GeoApiContext;
import com.google.maps.GeoApiContext.Builder;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlaceDetailsRequest.FieldMask;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.FindPlaceFromText;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;
import io.github.orionlibs.google_maps_wrapper.config.ConfigurationService;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

class PostcodeFormatter extends AGoogleMapsTask
{
    private int numberOfRetries = 0;


    Optional<String> run(ConfigurationService config, String postcode) throws MissingApiKeyException, IOException, InterruptedException, ApiException
    {
        if(postcode == null || postcode.isEmpty())
        {
            return Optional.<String>empty();
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
            String placeIDOfPostcode = "";
            FindPlaceFromTextRequest placesAPIRequest = PlacesApi.findPlaceFromText(geoAPIContext, postcode, InputType.TEXT_QUERY);
            FindPlaceFromText response = placesAPIRequest.await();
            PlacesSearchResult[] results = response.candidates;
            if(results != null && results.length > 0)
            {
                for(PlacesSearchResult result : results)
                {
                    placeIDOfPostcode = result.placeId;
                    break;
                }
            }
            if(placeIDOfPostcode != null && !placeIDOfPostcode.isEmpty())
            {
                PlaceDetailsRequest request = new PlaceDetailsRequest(geoAPIContext);
                request = request.placeId(placeIDOfPostcode);
                request = request.fields(FieldMask.ADDRESS_COMPONENT);
                PlaceDetails apiResponse = request.await();
                String postcodeWithoutSpace = postcode.replace(" ", "");
                for(AddressComponent addressComponent : apiResponse.addressComponents)
                {
                    String addressComponentWithoutSpace = addressComponent.shortName.replace(" ", "");
                    if(addressComponentWithoutSpace.equalsIgnoreCase(postcodeWithoutSpace))
                    {
                        return Optional.<String>of(addressComponent.shortName);
                    }
                }
            }
            else
            {
                return Optional.<String>empty();
            }
        }
        finally
        {
            closeRequest(geoAPIContext);
        }
        return Optional.<String>empty();
    }


    public static class FakePostcodeFormatter extends PostcodeFormatter
    {
        @Override
        public Optional<String> run(ConfigurationService config, String postcode) throws MissingApiKeyException
        {
            if(postcode == null || postcode.isEmpty())
            {
                return Optional.<String>empty();
            }
            else
            {
                String apiKey = config.getProp("orionlibs.google-maps-wrapper.google.maps.api.key");
                Utils.validateAPIKey(apiKey);
                return Optional.<String>of(postcode.substring(0, postcode.length() - 3).toUpperCase());
            }
        }
    }
}