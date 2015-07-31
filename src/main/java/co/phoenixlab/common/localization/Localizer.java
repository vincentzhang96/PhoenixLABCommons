package co.phoenixlab.common.localization;

import java.util.Locale;

public interface Localizer {

    /**
     * Marker text returned by the localizer when a given key cannot be found
     */
    String LOCALE_STRING_NOT_FOUND = System.getProperty("co.phoenixlab.localizer.notFound", "##NOT_FOUND##");

    String PREFIX_FLAG_BASE = "@# ";
    int PREFIX_FLAG_LENGTH = PREFIX_FLAG_BASE.length();

    int PREFIX_FLAG_DO_NOT_LOCALIZE_BIT = 0;
    /**
     * Prefix flag to prepend to a key to indicate that the key should be taken as-is and not localized.
     * @see #doNotLocalize(String)
     */
    String PREFIX_FLAG_DO_NOT_LOCALIZE = internalSetFlagBit(PREFIX_FLAG_BASE, PREFIX_FLAG_DO_NOT_LOCALIZE_BIT);

    Locale getLocale();

    void addLocaleStringProvider(LocaleStringProvider provider);

    void removeLocaleStringProvider(LocaleStringProvider provider);

    boolean containsKey(String key);

    String localize(String key);

    String localize(String key, Object arg0);

    String localize(String key, Object arg0, Object arg1);

    String localize(String key, Object... args);

    String localizeOrDefault(String key, String def);

    String localizeOrDefault(String key, String def, Object arg0);

    String localizeOrDefault(String key, String def, Object arg0, Object arg1);

    String localizeOrDefault(String key, String def, Object... args);

    static String doNotLocalize(String key) {
        if (key.startsWith(PREFIX_FLAG_BASE)) {
            return internalSetFlagBit(key, PREFIX_FLAG_DO_NOT_LOCALIZE_BIT);
        } else {
            return PREFIX_FLAG_DO_NOT_LOCALIZE + key;
        }
    }

    static String internalSetFlagBit(String current, int bit) {
        if (bit > 4) {
            throw new IllegalArgumentException("Bit must be between 0 and 4");
        }
        char[] chars = current.toCharArray();
        int bits = chars[2];
        bits = bits | 1 << bit;
        chars[2] = (char) bits;
        return new String(chars);
    }

}
