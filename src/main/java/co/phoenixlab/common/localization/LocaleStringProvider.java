package co.phoenixlab.common.localization;

@FunctionalInterface
public interface LocaleStringProvider {

    String get(String key);

}
