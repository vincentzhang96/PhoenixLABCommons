package co.phoenixlab.common.lang;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

/**
 * Provides a reversed view of a ListIterator, starting from the last element.
 * @param <E>
 */
public class ReverseListIterator<E> implements ListIterator<E>, Iterable<E> {

    private final ListIterator<E> parent;

    public ReverseListIterator(ListIterator<E> parent) {
        this.parent = parent;
        //  Wind the iterator to the end
        while (parent.hasNext()) {
            parent.next();
        }
    }

    @Override
    public boolean hasNext() {
        return parent.hasPrevious();
    }

    @Override
    public E next() {
        return parent.previous();
    }

    @Override
    public boolean hasPrevious() {
        return parent.hasNext();
    }

    @Override
    public E previous() {
        return parent.next();
    }

    @Override
    public int nextIndex() {
        return parent.previousIndex();
    }

    @Override
    public int previousIndex() {
        return parent.nextIndex();
    }

    @Override
    public void remove() {
        parent.remove();
    }

    @Override
    public void set(E e) {
        parent.set(e);
    }

    @Override
    public void add(E e) {
        parent.add(e);
    }

    @Override
    public Iterator<E> iterator() {
        return this;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        while (hasNext()) {
            action.accept(next());
        }
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliteratorUnknownSize(this, 0);
    }
}
