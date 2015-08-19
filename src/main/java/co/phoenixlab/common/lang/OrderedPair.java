package co.phoenixlab.common.lang;

import java.util.Objects;

public class OrderedPair<T, U> {

    private final T t;
    private final U u;

    public OrderedPair(T t, U u) {
        this.t = t;
        this.u = u;
    }

    public T getT() {
        return t;
    }

    public U getU() {
        return u;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderedPair<?, ?> that = (OrderedPair<?, ?>) o;
        return Objects.equals(t, that.t) &&
                Objects.equals(u, that.u);
    }

    @Override
    public int hashCode() {
        return Objects.hash(t, u);
    }

    /**
     * Creates an ordered pair with the given elements. Equivalent to {@link #OrderedPair(Object, Object)}.
     */
    public static <T, U> OrderedPair<T, U> pair(T t, U u) {
        return new OrderedPair<>(t, u);
    }

    /**
     * Creates an ordered pair of an object and itself. Equivalent to calling {@link #OrderedPair(Object, Object)} with
     * the same object for both arguments.
     */
    public static <T> OrderedPair<T, T> twin(T t) {
        return new OrderedPair<>(t, t);
    }

}
