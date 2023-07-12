package io.github.orionlibs.google_maps_wrapper;

import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import io.github.orionlibs.google_maps_wrapper.config.ConfigurationService;

class Utils
{
    static void validateAPIKey(String apiKey) throws MissingApiKeyException
    {
        if(apiKey == null || apiKey.isEmpty())
        {
            throw new MissingApiKeyException();
        }
    }


    static long validateConnectionTimeout(ConfigurationService config)
    {
        Long connectionTimeout = config.getLongProp("orionlibs.google-maps-wrapper.connection.timeout.in.seconds");
        if(connectionTimeout != null)
        {
            if(connectionTimeout.longValue() < 0L)
            {
                connectionTimeout = 15L;
            }
        }
        else
        {
            connectionTimeout = 15L;
        }
        return connectionTimeout;
    }


    static int validateRetries(ConfigurationService config)
    {
        Integer retries = config.getIntegerProp("orionlibs.google-maps-wrapper.connection.retries");
        if(retries != null)
        {
            if(retries.intValue() < 0L)
            {
                retries = 0;
            }
        }
        else
        {
            retries = 0;
        }
        return retries;
    }


    static TravelMode validateTransportationType(ConfigurationService config)
    {
        String transportationTypeTemp = config.getProp("orionlibs.google-maps-wrapper.transportation.type");
        TravelMode transportationType = TravelMode.valueOf(transportationTypeTemp);
        return transportationType != null ? transportationType : TravelMode.DRIVING;
    }


    static Unit validateDistanceUnit(ConfigurationService config)
    {
        String distanceUnitTemp = config.getProp("orionlibs.google-maps-wrapper.distance.unit");
        Unit distanceUnit = Unit.valueOf(distanceUnitTemp);
        return distanceUnit != null ? distanceUnit : Unit.IMPERIAL;
    }
}
