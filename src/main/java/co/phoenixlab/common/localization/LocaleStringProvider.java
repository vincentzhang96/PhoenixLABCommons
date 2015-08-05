package co.phoenixlab.common.localization;

import java.util.Locale;

public interface LocaleStringProvider {

    void setActiveLocale(Locale locale);

    String get(String key);

    boolean contains(String key);

}
