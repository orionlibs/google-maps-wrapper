package io.github.orionlibs.google_maps_wrapper;

public class MissingApiKeyException extends Exception
{
    private static final String DefaultErrorMessage = "There is no Google Maps API key. Please create the GoogleMapsService by passing a Properties customConfig object with your config.";


    public MissingApiKeyException()
    {
        super(DefaultErrorMessage);
    }


    public MissingApiKeyException(String message)
    {
        super(message);
    }


    public MissingApiKeyException(String errorMessage, Object... arguments)
    {
        super(String.format(errorMessage, arguments));
    }


    public MissingApiKeyException(Throwable cause, String errorMessage, Object... arguments)
    {
        super(String.format(errorMessage, arguments), cause);
    }


    public MissingApiKeyException(Throwable cause)
    {
        super(DefaultErrorMessage, cause);
    }
}