package ftkg.nqueens.internal;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Resizable-array implementation of the {@link Stack} interface, using {@link ArrayDeque} underneath. Not thread-safe.
 * @param <T> the type of elements held in this collection
 */
public final class DequeStack<T> implements Stack<T> {

    /**
     * The wrapped collection.
     */
    private final Deque<T> deque;

    /**
     * Constructs an empty stack with an initial capacity sufficient to hold the specified number of elements.
     * @param size lower bound on initial capacity of the stack
     */
    public DequeStack(int size) {
        this.deque = new ArrayDeque<>(size);
    }

    @Override
    public int size() {
        return this.deque.size();
    }

    @Override
    public void push(T item) {
        this.deque.addFirst(item);
    }

    @Override
    public T pop() {
        return this.deque.removeFirst();
    }

    @Override
    public Iterator<T> iterator() {
        return this.deque.iterator();
    }
}
