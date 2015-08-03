package co.phoenixlab.common.localization;

public interface LocaleStringProvider {

    String get(String key);

    boolean contains(String key);

}
