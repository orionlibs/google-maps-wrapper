package io.github.orionlibs.google_maps_wrapper;

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
        return connectionTimeout != null ? connectionTimeout : 15L;
    }


    static int validateRetries(ConfigurationService config)
    {
        Integer retries = config.getIntegerProp("orionlibs.google-maps-wrapper.connection.retries");
        return retries != null ? retries : 2;
    }
}
