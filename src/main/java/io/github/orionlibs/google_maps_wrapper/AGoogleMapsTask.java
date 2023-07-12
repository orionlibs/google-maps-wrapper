package io.github.orionlibs.google_maps_wrapper;

import com.google.maps.GeoApiContext;
import java.io.IOException;

public abstract class AGoogleMapsTask
{
    protected void closeRequest(GeoApiContext geoAPIContext)
    {
        try
        {
            if(geoAPIContext != null)
            {
                geoAPIContext.close();
                geoAPIContext.shutdown();
            }
            geoAPIContext = null;
        }
        catch(IOException e)
        {
            /*LoggingService.logError(null,
                            null,
                            GoogleMapsErrorType.GoogleMaps.get(),
                            GoogleMapsErrors.ErrorWithGoogleMaps,
                            e);*/
        }
    }
}