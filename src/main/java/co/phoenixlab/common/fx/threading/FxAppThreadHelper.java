package co.phoenixlab.common.fx.threading;

import javafx.application.Platform;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.LongConsumer;

import static javafx.application.Platform.*;

/**
 * Provides utility methods for working with the JavaFX Application Thread
 */
public class FxAppThreadHelper {

    private FxAppThreadHelper() {}

    /**
     * Check if the current calling thread is the FX Application thread
     * @return true if it is, false if it isn't
     * @see Platform#isFxApplicationThread()
     */
    public static boolean isAppThread() {
        return isFxApplicationThread();
    }

    /**
     * Queues up a {@code Runnable} or nullary method on the FX Application thread to be run some time in the future.
     * @param runnable The {@code Runnable} to run later
     * @see Platform#runLater(Runnable)
     */
    public static void runOnAppThread(final Runnable runnable) {
        runLater(runnable);
    }

    /**
     * Queues up a {@code Consumer} to be run with the given parameter {@code t} some time in the future. {@code t}
     * should be immutable or thread safe.
     * @param consumer The method to be run later with the given parameter
     * @param t The single parameter for the provided method
     * @param <T> The type of the parameter {@code t}
     * @see Platform#runLater(Runnable)
     */
    public static <T> void runOnAppThread(final Consumer<T> consumer, final T t) {
        runLater(() -> consumer.accept(t));
    }

    /**
     * Queues up a {@code Consumer} to be run with the given parameter {@code l} some time in the future.
     * <p>
     * This is a primitive specialization of {@link #runOnAppThread(Consumer, Object)} for integral types
     * @param consumer The method to be run later with the given parameter
     * @param l The single integral parameter for the provided method
     * @see Platform#runLater(Runnable)
     */
    public static void runOnAppThread(final LongConsumer consumer, final long l) {
        runLater(() -> consumer.accept(l));
    }

    /**
     * Queues up a {@code Consumer} to be run with the given parameter {@code d} some time in the future.
     * <p>
     * This is a primitive specialization of {@link #runOnAppThread(Consumer, Object)} for real types
     * @param consumer The method to be run later with the given parameter
     * @param d The single real parameter for the provided method
     * @see Platform#runLater(Runnable)
     */
    public static void runOnAppThread(final DoubleConsumer consumer, final double d) {
        runLater(() -> consumer.accept(d));
    }

    /**
     * Queues up a {@code BiConsumer} to be run with the given parameters {@code t} and {@code u} some time in the future.
     * The parameters should be immutable or thread safe.
     * @param consumer The method to be run later with the given parameter
     * @param t The first parameter for the method
     * @param u The second parameter for the method
     * @see Platform#runLater(Runnable)
     */
    public static <T, U> void runOnAppThread(final BiConsumer<T, U> consumer, final T t, final U u) {
        runLater(() -> consumer.accept(t, u));
    }

}

