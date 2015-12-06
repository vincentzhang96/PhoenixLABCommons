package co.phoenixlab.common.localization;

import java.util.function.Predicate;

@FunctionalInterface
public interface LocalizerPluralRule extends Predicate<Number> {
}
