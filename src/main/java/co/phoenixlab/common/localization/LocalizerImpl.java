package co.phoenixlab.common.localization;

import java.util.Collection;
import java.util.Locale;

public class LocalizerImpl implements Localizer {

    private final Locale locale;

    public LocalizerImpl(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public void addLocaleStringProvider(LocaleStringProvider provider) {

    }

    @Override
    public void removeLocaleStringProvider(LocaleStringProvider provider) {

    }

    @Override
    public Collection<LocaleStringProvider> getLocaleStringProviders() {
        return null;
    }

    @Override
    public void removeAllLocaleStringProviders() {

    }

    @Override
    public boolean containsKey(String key) {
        return false;
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
