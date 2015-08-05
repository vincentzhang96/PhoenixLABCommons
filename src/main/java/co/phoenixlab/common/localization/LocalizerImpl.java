package co.phoenixlab.common.localization;

import java.util.*;

public class LocalizerImpl implements Localizer {

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
        return null;
    }

    @Override
    public String localize(String key, Object arg0) {
        return null;
    }

    @Override
    public String localize(String key, Object arg0, Object arg1) {
        return null;
    }

    @Override
    public String localize(String key, Object... args) {
        return null;
    }

    @Override
    public String localizeOrDefault(String key, String def) {
        return null;
    }

    @Override
    public String localizeOrDefault(String key, String def, Object arg0) {
        return null;
    }

    @Override
    public String localizeOrDefault(String key, String def, Object arg0, Object arg1) {
        return null;
    }

    @Override
    public String localizeOrDefault(String key, String def, Object... args) {
        return null;
    }
}
