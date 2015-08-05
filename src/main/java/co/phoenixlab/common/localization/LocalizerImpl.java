package co.phoenixlab.common.localization;

import java.util.*;

import static co.phoenixlab.common.localization.Localizer.*;

public class LocalizerImpl implements Localizer {

    private static final String NULL_KEY = "nullkey";

    private final Locale locale;
    private final LinkedHashSet<LocaleStringProvider> providers;
    private final Collection<LocaleStringProvider> providersUnmodifiable;

    public LocalizerImpl(Locale locale) {
        Objects.requireNonNull(locale, "Locale cannot be null");
        this.locale = locale;
        this.providers = new LinkedHashSet<>();
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

        }
        if (!internalIsFlagBitSet(key, PREFIX_FLAG_DO_NOT_FORMAT_BIT)) {
            //  Format

        }
        return ret;
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
