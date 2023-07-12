package io.github.orionlibs.google_maps_wrapper.config;

/**
 * provides access to the plugin's config
 */
public class ConfigurationService
{
    private static OrionConfiguration configurationRegistry;


    /**
     * stores a config object
     * @param configuration
     */
    public void registerConfiguration(OrionConfiguration configuration)
    {
        configurationRegistry = configuration;
    }


    /**
     * retrieves the value associated with the provided key
     * @param key
     * @return
     */
    public String getProp(String key)
    {
        return configurationRegistry.getProperty(key);
    }


    /**
     * retrieves the value associated with the provided key casted to a int
     * @param key
     * @return
     */
    public Integer getIntegerProp(String key)
    {
        return Integer.parseInt(configurationRegistry.getProperty(key));
    }


    /**
     * retrieves the value associated with the provided key casted to a long
     * @param key
     * @return
     */
    public Long getLongProp(String key)
    {
        return Long.parseLong(configurationRegistry.getProperty(key));
    }


    /**
     * remaps the given key to the given value
     * @param key
     * @param value
     */
    public void updateProp(String key, String value)
    {
        configurationRegistry.updateProp(key, value);
    }
}
