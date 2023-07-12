package io.github.orionlibs.google_maps_wrapper;

import com.google.maps.FindPlaceFromTextRequest;
import com.google.maps.FindPlaceFromTextRequest.InputType;
import com.google.maps.GeoApiContext;
import com.google.maps.GeoApiContext.Builder;
import com.google.maps.PlaceDetailsRequest;
import com.google.maps.PlaceDetailsRequest.FieldMask;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.InvalidRequestException;
import com.google.maps.model.AddressComponent;
import com.google.maps.model.FindPlaceFromText;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResult;
import io.github.orionlibs.google_maps_wrapper.config.ConfigurationService;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class PostcodeFormatter extends AGoogleMapsTask
{
    private int numberOfRetries = 0;


    public Optional<String> run(String postcode) throws MissingApiKeyException
    {
        String apiKey = ConfigurationService.getProp("orionlibs.google-maps-wrapper.google.maps.api.key");
        if(apiKey == null || apiKey.isEmpty())
        {
            throw new MissingApiKeyException();
        }
        Builder requestBuilder = new Builder();
        requestBuilder.apiKey(apiKey);
        requestBuilder.connectTimeout(15, TimeUnit.SECONDS);
        requestBuilder.readTimeout(15, TimeUnit.SECONDS);
        requestBuilder.maxRetries(2);
        GeoApiContext geoAPIContext = requestBuilder.build();
        String placeIDOfPostcode = "";
        FindPlaceFromTextRequest placesAPIRequest = PlacesApi.findPlaceFromText(geoAPIContext, postcode, InputType.TEXT_QUERY);
        try
        {
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
        }
        catch(InvalidRequestException e)
        {
            /*LoggingService.logError(null,
                            null,
                            GoogleMapsErrorType.GoogleMaps.get(),
                            GoogleMapsErrors.ErrorWithGoogleMaps,
                            e);*/
            return processAPICall(postcode, geoAPIContext);
        }
        catch(ApiException e)
        {
            /*LoggingService.logError(null,
                            null,
                            GoogleMapsErrorType.GoogleMaps.get(),
                            GoogleMapsErrors.ErrorWithGoogleMaps,
                            e);*/
            return processAPICall(postcode, geoAPIContext);
        }
        catch(InterruptedException e)
        {
            /*LoggingService.logError(null,
                            null,
                            GoogleMapsErrorType.GoogleMaps.get(),
                            GoogleMapsErrors.ErrorWithGoogleMaps,
                            e);*/
            return processAPICall(postcode, geoAPIContext);
        }
        catch(IOException e)
        {
            /*LoggingService.logError(null,
                            null,
                            GoogleMapsErrorType.GoogleMaps.get(),
                            GoogleMapsErrors.ErrorWithGoogleMaps,
                            e);*/
            return processAPICall(postcode, geoAPIContext);
        }
        catch(Exception e)
        {
            /*LoggingService.logError(null,
                            null,
                            GoogleMapsErrorType.GoogleMaps.get(),
                            GoogleMapsErrors.ErrorWithGoogleMaps,
                            e);*/
            return processAPICall(postcode, geoAPIContext);
        }
        if(placeIDOfPostcode != null && !placeIDOfPostcode.isEmpty())
        {
            PlaceDetailsRequest request = new PlaceDetailsRequest(geoAPIContext);
            request = request.placeId(placeIDOfPostcode);
            request = request.fields(FieldMask.ADDRESS_COMPONENT);
            try
            {
                PlaceDetails response = request.await();
                String postcodeWithoutSpace = postcode.replace(" ", "");
                for(AddressComponent addressComponent : response.addressComponents)
                {
                    String addressComponentWithoutSpace = addressComponent.shortName.replace(" ", "");
                    if(addressComponentWithoutSpace.equalsIgnoreCase(postcodeWithoutSpace))
                    {
                        return Optional.<String>of(addressComponent.shortName);
                    }
                }
            }
            catch(InvalidRequestException e)
            {
                /*LoggingService.logError(null,
                                null,
                                GoogleMapsErrorType.GoogleMaps.get(),
                                GoogleMapsErrors.ErrorWithGoogleMaps,
                                e);*/
                return processAPICall(postcode, geoAPIContext);
            }
            catch(ApiException e)
            {
                /*LoggingService.logError(null,
                                null,
                                GoogleMapsErrorType.GoogleMaps.get(),
                                GoogleMapsErrors.ErrorWithGoogleMaps,
                                e);*/
                return processAPICall(postcode, geoAPIContext);
            }
            catch(InterruptedException e)
            {
                /*LoggingService.logError(null,
                                null,
                                GoogleMapsErrorType.GoogleMaps.get(),
                                GoogleMapsErrors.ErrorWithGoogleMaps,
                                e);*/
                return processAPICall(postcode, geoAPIContext);
            }
            catch(IOException e)
            {
                /*LoggingService.logError(null,
                                null,
                                GoogleMapsErrorType.GoogleMaps.get(),
                                GoogleMapsErrors.ErrorWithGoogleMaps,
                                e);*/
                return processAPICall(postcode, geoAPIContext);
            }
            finally
            {
                closeRequest(geoAPIContext);
            }
        }
        else
        {
            closeRequest(geoAPIContext);
            return Optional.<String>empty();
        }
        closeRequest(geoAPIContext);
        return Optional.<String>empty();
    }


    private Optional<String> processAPICall(String postcode, GeoApiContext geoAPIContext) throws MissingApiKeyException
    {
        closeRequest(geoAPIContext);
        if(numberOfRetries == 0)
        {
            try
            {
                Thread.sleep(3000);
                numberOfRetries = 1;
                return run(postcode);
            }
            catch(InterruptedException e)
            {
                return Optional.<String>empty();
            }
        }
        else
        {
            return Optional.<String>empty();
        }
    }


    public static class FakePostcodeFormatter extends PostcodeFormatter
    {
        @Override
        public Optional<String> run(String postcode)
        {
            if(postcode == null || postcode.isEmpty())
            {
                return Optional.<String>empty();
            }
            else
            {
                return Optional.<String>of(postcode.substring(0, postcode.length() - 3).toUpperCase());
            }
        }
    }
}