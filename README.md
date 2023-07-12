# Orion Google Maps Wrapper
Java library that uses the Google Maps SDK to provide easy-to-use features.

Please check the wiki

https://github.com/orionlibs/google-maps-wrapper/wiki

You can import the library by using:  
```xml
<dependency>
    <groupId>io.github.orionlibs</groupId>
    <artifactId>google-maps-wrapper</artifactId>
    <version>1.0.0</version>
</dependency>
```

To use this service, you can do:
```java
GoogleMapsService googleMapsService = new GoogleMapsService();
Optional<String> formattedPostcode = googleMapsService.formatPostcode("w69hh");//W6 9HH
Optional<DistanceAndTravelDuration> distance = googleMapsService.getDistanceAndTravelDuration("W6 9HH", "SW7 3DP");
```
The configuration for this library is in the orion-feature-configuration.prop file.  
If you want to override some or all of the config then create a Properties object with the config you want and pass it to the GoogleMapsService constructor.