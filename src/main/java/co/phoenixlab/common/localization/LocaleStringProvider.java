package co.phoenixlab.common.localization;

import java.util.Locale;

public interface LocaleStringProvider {

    /**
     * Indicates to this provider which locale to load and use
     * @param locale The locale to load and use
     */
    void setActiveLocale(Locale locale);

    /**
     * Gets the value associated with the given key, in the active locale.
     * @param key The key to look up
     * @return The value associated with the given key, or null if no such value exists
     */
    String get(String key);

    /**
     * Gets whether or not this provider can provide the value associated with the given key
     * @param key The key to look up
     * @return True if there exists an associated value that can be provided, false if not
     */
    boolean contains(String key);

}
