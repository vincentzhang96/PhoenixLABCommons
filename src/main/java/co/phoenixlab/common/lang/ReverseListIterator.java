package co.phoenixlab.common.lang;

import java.util.ListIterator;

/**
 * Provides a reversed view of a ListIterator, starting from the last element.
 * @param <E>
 */
public class ReverseListIterator<E> implements ListIterator<E> {

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
}
