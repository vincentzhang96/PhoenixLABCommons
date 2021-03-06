package co.phoenixlab.common.localization;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public interface Localizer {

    /**
     * Marker text returned by the localizer when a given key cannot be found.
     * Default value is {@code ##NOT_FOUND##}, can be set via the system
     * property {@code co.phoenixlab.localizer.notfound}
     */
    String LOCALE_STRING_NOT_FOUND = System.getProperty("co.phoenixlab.localizer.notfound", "##NOT_FOUND##");

    /**
     * Marker text returned by the localizer if the format string is invalid.
     * Default value is {@code ##INVALID_FORMAT##}, can be set via the system
     * property {@code co.phoenixlab.localizer.invalidformat}
     */
    String INVALID_FORMAT_STRING = System.getProperty("co.phoenixlab.localizer.invalidformat", "##INVALID_FORMAT##");

    /**
     * Marker text returned by the localizer if no plurality rule matched the given argument
     * Default value is {@code ##NO_MATCHING_PLURAL##}, can be set via the system
     * property {@code co.phoenixlab.localizer.nomatchingplural}
     */
    String NO_MATCHING_PLURAL = System.getProperty("co.phoenixlab.localizer.nomatchingplural",
            "##NO_MATCHING_PLURAL##");

    String PREFIX_FLAG_BASE = "@# ";
    int PREFIX_FLAG_LENGTH = PREFIX_FLAG_BASE.length();

    int PREFIX_FLAG_DO_NOT_LOCALIZE_BIT = 0;
    /**
     * Prefix flag to prepend to a key to indicate that the key should be taken as-is and not localized.
     *
     * @see #doNotLocalize(String)
     */
    String PREFIX_FLAG_DO_NOT_LOCALIZE = internalSetFlagBit(PREFIX_FLAG_BASE, PREFIX_FLAG_DO_NOT_LOCALIZE_BIT);

    int PREFIX_FLAG_DO_NOT_FORMAT_BIT = 1;
    /**
     * Prefix flag to prepend to a key to indicate that the localizer should not attempt to parse and replace
     * format tokens in the localization string.
     *
     * @see #doNotFormat(String)
     */
    String PREFIX_FLAG_DO_NOT_FORMAT = internalSetFlagBit(PREFIX_FLAG_BASE, PREFIX_FLAG_DO_NOT_FORMAT_BIT);

    /**
     * Gets the locale for this Localizer.
     *
     * @return This Localizer's locale
     */
    Locale getLocale();

    /**
     * Registers a provider to this Localizer.
     *
     * @param provider A LocaleStringProvider to register
     */
    void addLocaleStringProvider(LocaleStringProvider provider);

    /**
     * Unregisters a provider from this localizer.
     *
     * @param provider The LocaleStringProvider to unregister
     */
    void removeLocaleStringProvider(LocaleStringProvider provider);

    /**
     * Gets the providers registered with this Localizer.
     *
     * @return An unmodifiable Collection view of the LocaleStringProviders registered with this Localizer
     */
    Collection<LocaleStringProvider> getLocaleStringProviders();

    /**
     * Unregisters all providers from this Localizer
     */
    void removeAllLocaleStringProviders();

    /**
     * Register the provided plurality rules to this Localizer
     * @param rules A map of plurality rules. key: name, value: LocalizerPluralRule
     */
    void registerPluralityRules(Map<String, LocalizerPluralRule> rules);

    /**
     * Checks if a given key exists.
     *
     * @param key The key to test
     * @return True if there exists a localization string with the given key, false otherwise
     */
    boolean containsKey(String key);

    /**
     * Localizes a given key. This is the zero-arg specialization of {@link #localize(String, Object...)}.
     *
     * @param key The key to localize
     * @return The localized string, or {@link #LOCALE_STRING_NOT_FOUND} if no such key exists
     * @see #localize(String, Object...)
     */
    String localize(String key);

    /**
     * Localizes a given key with the given argument for formatting. This is the one-arg specialization of
     * {@link #localize(String, Object...)}.
     *
     * @param key  The key to localize
     * @param arg0 The argument to pass in for formatting
     * @return The localized string, or {@link #LOCALE_STRING_NOT_FOUND} if no such key exists
     * @see #localize(String, Object...)
     */
    String localize(String key, Object arg0);

    /**
     * Localizes a given key with the given arguments for formatting. This is the two-arg specialization of
     * {@link #localize(String, Object...)}.
     *
     * @param key  The key to localize
     * @param arg0 The first argument to pass in for formatting
     * @param arg1 The second argument to pass in for formatting
     * @return The localized string, or {@link #LOCALE_STRING_NOT_FOUND} if no such key exists
     * @see #localize(String, Object...)
     */
    String localize(String key, Object arg0, Object arg1);

    /**
     * Localizes a given key with the given argument for formatting.
     * <p>
     * The given key is used to fetch a special format string. Format strings may contain:
     * <ol>
     * <li>Other localization keys inside of single square brackets: [full.localization.key]</li>
     * <li>Relative localization keys inside of single square brackets: [.relative.localization.key] which will
     * resolve to the given key + the relative key, so given "foo.bar" as the key and "[.baz.biz]" in the
     * corresponding locale string, the string from "foo.bar.baz.biz" will be substituted.</li>
     * <li>Argument index, formatting, and pluralization controls inside curly braces (see below)</li>
     * </ol>
     * <p>
     * TODO Curly brace docs
     *
     * @param key  The key to localize
     * @param args A varargs of arguments for formatting
     * @return The localized string, or {@link #LOCALE_STRING_NOT_FOUND} if no such key exists
     */
    String localize(String key, Object... args);

    /**
     * Attempts to localize the given key, returning {@code def} if it could not be found. This is the zero-args
     * specialization for {@link #localizeOrDefault(String, String, Object...)}.
     *
     * @param key The key to localize
     * @param def The default String (does not get localized) to return
     * @return The localized string, or {@code def} if no such string exists
     * @see #localizeOrDefault(String, String, Object...)
     */
    String localizeOrDefault(String key, String def);

    /**
     * Attempts to localize the given key with the given argument for formatting, returning {@code def} if it could
     * not be found. This is the one-arg specialization for {@link #localizeOrDefault(String, String, Object...)}.
     *
     * @param key  The key to localize
     * @param def  The default String (does not get localized) to return
     * @param arg0 The argument to pass in for formatting
     * @return The localized string, or {@code def} if no such string exists
     * @see #localizeOrDefault(String, String, Object...)
     */
    String localizeOrDefault(String key, String def, Object arg0);

    /**
     * Attempts to localize the given key with the given arguments for formatting, returning {@code def} if it could
     * not be found. This is the two-arg specialization for {@link #localizeOrDefault(String, String, Object...)}.
     *
     * @param key  The key to localize
     * @param def  The default String (does not get localized) to return
     * @param arg0 The first argument to pass in for formatting
     * @param arg1 The second argument to pass in for formatting
     * @return The localized string, or {@code def} if no such string exists
     * @see #localizeOrDefault(String, String, Object...)
     */
    String localizeOrDefault(String key, String def, Object arg0, Object arg1);

    /**
     * Attempts to localize the given key with the given arguments for formatting, returning {@code def} if it could
     * not be found.
     *
     * @param key  The key to localize
     * @param def  The default String (does not get localized) to return
     * @param args A varargs of arguments for formatting
     * @return The localized string, or {@code def} if no such string exists
     * @see #localize(String, Object...)
     */
    String localizeOrDefault(String key, String def, Object... args);

    /**
     * Flags a string not to be localized by the localizer. The localizer will not attempt to perform a lookup and
     * will not perform the localization step. Other steps may still be performed, depending on the presence of other
     * control flags.
     *
     * @param key The string to prevent localization on
     * @return A key that will not be localized when passed to {@link #localize(String, Object...)} or
     * {@link #localizeOrDefault(String, String, Object...)} and their specializations
     */
    static String doNotLocalize(String key) {
        if (key.startsWith(PREFIX_FLAG_BASE)) {
            return internalSetFlagBit(key, PREFIX_FLAG_DO_NOT_LOCALIZE_BIT);
        } else {
            return PREFIX_FLAG_DO_NOT_LOCALIZE + key;
        }
    }

    /**
     * Flags a string not to be formatted by the localizer. The localizer will not attempt to parse and handle
     * formatting tokens in the localized string. Other steps may still be performed, depending on the presence
     * of other control flags.
     *
     * @param key The string to prevent localization on
     * @return A key that will not be localized when passed to {@link #localize(String, Object...)} or
     * {@link #localizeOrDefault(String, String, Object...)} and their specializations
     */
    static String doNotFormat(String key) {
        if (key.startsWith(PREFIX_FLAG_BASE)) {
            return internalSetFlagBit(key, PREFIX_FLAG_DO_NOT_FORMAT_BIT);
        } else {
            return PREFIX_FLAG_DO_NOT_FORMAT + key;
        }
    }

    /**
     * <strong>INTERNAL METHOD</strong>
     * <p>
     * Sets the bit to 1 at the given position in the flag section of a flagged key.
     *
     * @param current The existing string containing a flag section
     * @param bit     The bit to set to 1
     * @return The string with the updated flag section
     */
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

    /**
     * <strong>INTERNAL METHOD</strong>
     * <p>
     * Gets the value of the bit at the given position in the flag section of a flagged key.
     * @param key The flagged key to check from
     * @param bit The bit number to check
     * @return true if the bit is set, false if not
     */
    static boolean internalIsFlagBitSet(String key, int bit) {
        if (bit > 4) {
            throw new IllegalArgumentException("Bit must be between 0 and 4");
        }
        if (key.length() <= PREFIX_FLAG_LENGTH || !key.startsWith(PREFIX_FLAG_BASE)) {
            return false;
        }
        char[] chars = key.toCharArray();
        int bits = chars[2];
        return (bits & (1 << bit)) != 0;
    }

    /**
     * Removes all flags from a key. If the key is not flagged, then does nothing and returns the provided key.
     * @param key The key to remove all flags from
     * @return The key with all flags removed
     */
    static String stripFlags(String key) {
        if (key.startsWith(PREFIX_FLAG_BASE) && key.length() > PREFIX_FLAG_LENGTH) {
            return key.substring(PREFIX_FLAG_LENGTH + 1);
        }
        return key;
    }

}
