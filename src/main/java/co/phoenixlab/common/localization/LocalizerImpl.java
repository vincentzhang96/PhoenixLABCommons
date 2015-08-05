package co.phoenixlab.common.localization;

import co.phoenixlab.common.lang.ReverseListIterator;

import java.util.*;

import static co.phoenixlab.common.localization.Localizer.internalIsFlagBitSet;
import static co.phoenixlab.common.localization.Localizer.stripFlags;

public class LocalizerImpl implements Localizer {

    private static final String NULL_KEY = "nullkey";

    private final Locale locale;
    private final List<LocaleStringProvider> providers;
    private final Collection<LocaleStringProvider> providersUnmodifiable;

    public LocalizerImpl(Locale locale) {
        Objects.requireNonNull(locale, "Locale cannot be null");
        this.locale = locale;
        this.providers = new ArrayList<>();
        this.providersUnmodifiable = Collections.unmodifiableCollection(providers);
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void addLocaleStringProvider(LocaleStringProvider provider) {
        Objects.requireNonNull(provider, "Provider cannot be null");
        provider.setActiveLocale(locale);
        providers.add(provider);
    }

    @Override
    public void removeLocaleStringProvider(LocaleStringProvider provider) {
        Objects.requireNonNull(provider, "Provider cannot be null");
        providers.remove(provider);
    }

    @Override
    public Collection<LocaleStringProvider> getLocaleStringProviders() {
        return providersUnmodifiable;
    }

    @Override
    public void removeAllLocaleStringProviders() {
        providers.clear();
    }

    @Override
    public boolean containsKey(String key) {
        return providers.stream().
                anyMatch(p -> p.contains(key));
    }

    @Override
    public String localize(String key) {
        if (key == null) {
            return NULL_KEY;
        }

        return null;
    }

    @Override
    public String localize(String key, Object arg0) {
        if (key == null) {
            return NULL_KEY;
        }

        return null;
    }

    @Override
    public String localize(String key, Object arg0, Object arg1) {
        if (key == null) {
            return NULL_KEY;
        }

        return null;
    }

    @Override
    public String localize(String key, Object... args) {
        if (key == null) {
            return NULL_KEY;
        }
        String cleanKey = stripFlags(key);
        String ret = cleanKey;
        if (!internalIsFlagBitSet(key, PREFIX_FLAG_DO_NOT_LOCALIZE_BIT)) {
            //  Localize
            ret = lookup(cleanKey);
            if (ret == null) {
                return LOCALE_STRING_NOT_FOUND;
            }
        }
        if (!internalIsFlagBitSet(key, PREFIX_FLAG_DO_NOT_FORMAT_BIT)) {
            //  Format

        }
        return ret;
    }

    /**
     * Finds the value of the given key from the providers. This method iterates through the providers in a stack-like
     * fashion; that is, the last provider added is checked first, second last provider checked second, and so on.
     * This allows for proper overriding/priority of providers.
     *
     * @param key The key to look up
     * @return The value associated with the given key, or null if no provider could provide the requested value
     */
    private String lookup(String key) {
        ListIterator<LocaleStringProvider> iterator = new ReverseListIterator<>(providers.listIterator());
        LocaleStringProvider provider;
        while (iterator.hasNext()) {
            provider = iterator.next();
            String val = provider.get(key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    @Override
    public String localizeOrDefault(String key, String def) {
        if (key == null) {
            return def;
        }

        return null;
    }

    @Override
    public String localizeOrDefault(String key, String def, Object arg0) {
        if (key == null) {
            return def;
        }

        return null;
    }

    @Override
    public String localizeOrDefault(String key, String def, Object arg0, Object arg1) {
        if (key == null) {
            return def;
        }

        return null;
    }

    @Override
    public String localizeOrDefault(String key, String def, Object... args) {
        if (key == null) {
            return def;
        }

        return null;
    }
}
