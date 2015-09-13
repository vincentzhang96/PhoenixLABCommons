package co.phoenixlab.common.lang;

import java.util.*;
import java.util.function.*;

public class SafeNav<T> {

    /**
     * The current value being processed, possibly null
     */
    private T val;
    /**
     * The last call number of {@link #next(Function)} or {@link #next(UnaryOperator)}
     */
    private int lastNonNullDepth;

    /**
     * Private constructor
     */
    private SafeNav(T val, int lastNonNullDepth) {
        this.val = val;
        this.lastNonNullDepth = lastNonNullDepth;
    }

    /**
     * Starts a SafeNavigation chain with the given object.
     *
     * @param t   The object, possibly null, to start the chain with
     * @param <T> The type of t
     * @return A SafeNav<T> object to operate with.
     * @see SafeNav
     */
    public static <T> SafeNav<T> of(T t) {
        return new SafeNav<>(t, 0);
    }

    /**
     * Starts a SafeNavigation chain with the given Optional.
     *
     * @param optional An Optional<T> object, empty or occupied, to start the chain with
     * @param <T>      The type of t
     * @return A SafeNav<T> object to operate with.
     */
    public static <T> SafeNav<T> ofOptional(Optional<T> optional) {
        return new SafeNav<>(optional.orElse(null), 0);
    }

    /**
     * Applies the given {@code function} if the contained value is not null, or does nothing if it is null.
     * <p>
     * This is the generalized method, which will construct a new SafeNav<R> for completion of {@code function} (as in,
     * if the contained value is null, no new instance will be constructed and this method will return the current
     * SafeNav).
     *
     * @param function The function to apply to the contained value if present.
     * @param <R>      The return type of {@code function}
     * @return A SafeNav<R> object containing the result of the function, or an empty SafeNav<R> object if the contained
     * value was null.
     */
    @SuppressWarnings("unchecked")
    public <R> SafeNav<R> next(Function<T, R> function) {
        if (val == null) {
            //  We return ourselves if val is null because there's no distinction of nulls
            //  Consider it an optimization
            return (SafeNav<R>) this;
        }
        ++lastNonNullDepth;
        return new SafeNav<>(function.apply(val), lastNonNullDepth);
    }

    /**
     * Gets the final result of the chain, or null if the contained value was initially null or any calls to
     * {@code next()} produced a null result.
     *
     * @return The final result of applying the functions from previous {@code next()} calls, or null if at any point
     * the contained value was null.
     */
    public T get() {
        return val;
    }

    /**
     * Takes the final result of the chain, and if it is not null, applies the provided {@code function} to it and
     * returns the resulting value. If it is null, this method simply returns null without applying {@code function}
     *
     * @param function The function to apply if the final result is not null
     * @param <R>      The return type of the provided function
     * @return The result of calling the {@code function} with the final result, or null if the final result was null
     */
    public <R> R get(Function<T, R> function) {
        return next(function).get();
    }

    /**
     * Returns the contained value, or if not present, throws an exception provided by the given
     * {@code exceptionFactory}. The {@code exceptionFactory} takes one integer argument, which is the depth at which
     * the contained value was last present.
     *
     * @param exceptionFactory An exception factory that takes one integer argument indicating the depth at which the
     *                         contained value was last present
     * @param <E>              The type of exception to throw
     * @return The contained value
     * @throws E If the contained value is not present; the exception created by calling {@code exceptionFactory}
     */
    public <E extends Throwable> T orElseThrow(IntFunction<E> exceptionFactory) throws E {
        if (val == null) {
            throw exceptionFactory.apply(lastNonNullDepth);
        }
        return val;
    }

    /**
     * Convenience method for throwing a NullPointerException with a default message for
     * {@link #orElseThrow(IntFunction)}
     *
     * @return The contained value
     * @throws NullPointerException If the contained value is not present
     */
    public T orElseThrowNPE() throws NullPointerException {
        return orElseThrow((int i) -> new NullPointerException("Val null since next() #" + i));
    }

    /**
     * Convenience method for throwing a NoSuchElementException with a default message for
     * {@link #orElseThrow(IntFunction)}
     *
     * @return The contained value
     * @throws NoSuchElementException If the contained value is not present
     */
    public T orElseThrowNSEE() throws NoSuchElementException {
        return orElseThrow((int i) -> new NoSuchElementException("Val null since next() #" + i));
    }

    /**
     * @return The last call number of {@code next()} where the contained value was present (so far)
     */
    public int getLastNonNullDepth() {
        return lastNonNullDepth;
    }

    /**
     * Passes the current last call number of {@code next()} where the contained value was present (so far) to the
     * given consumer. This method allows for continued chaining, unlike {@link #getLastNonNullDepth()}.
     *
     * @param consumer The consumer of the current last call number
     * @return The current SafeNav<T>
     */
    public SafeNav<T> lastNonNullDepth(IntConsumer consumer) {
        consumer.accept(lastNonNullDepth);
        return this;
    }

    /**
     * Passes the contained value to the given consumer if it is present, otherwise does nothing.
     *
     * @param consumer The consumer that will accept the contained value if present
     */
    public void ifPresent(Consumer<T> consumer) {
        if (val != null) {
            consumer.accept(val);
        }
    }

    /**
     * Returns the contained value, if present, or the given default value.
     *
     * @param defaultVal The value (can be null) to return if the contained value is not present
     * @return The contained value, if present, or the default value
     */
    public T orElse(T defaultVal) {
        if (val == null) {
            return defaultVal;
        }
        return val;
    }

    /**
     * If the contained value is present, returns the result of applying {@code function} to it. If not
     * present, returns the given default value.
     *
     * @param function   The function to apply to the contained value if present
     * @param defaultVal The value to return if the contained value is not present
     * @param <R>        The return type of {@code function} and the type of the default value
     * @return The result of {@code function}, if the contained value is present, or the default value
     */
    public <R> R orElse(Function<T, R> function, R defaultVal) {
        return next(function).orElse(defaultVal);
    }

    /**
     * Converts this SafeNav to an Optional
     *
     * @return An Optional of the contained value, if present, or an empty Optional, if not
     */
    public Optional<T> toOptional() {
        return Optional.ofNullable(val);
    }

    /**
     * Integer specialization of {@link #toOptional()}
     */
    public OptionalInt toOptionalInt(ToIntFunction<T> function) {
        if (val == null) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(function.applyAsInt(val));
    }

    /**
     * Long specialization of {@link #toOptional()}
     */
    public OptionalLong toOptionalLong(ToLongFunction<T> function) {
        if (val == null) {
            return OptionalLong.empty();
        }
        return OptionalLong.of(function.applyAsLong(val));
    }

    /**
     * Double specialization of {@link #toOptional()}
     */
    public OptionalDouble toOptionalDouble(ToDoubleFunction<T> function) {
        if (val == null) {
            return OptionalDouble.empty();
        }
        return OptionalDouble.of(function.applyAsDouble(val));
    }

}
