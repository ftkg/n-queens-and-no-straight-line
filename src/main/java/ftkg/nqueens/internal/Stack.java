package ftkg.nqueens.internal;

/**
 * A semantically correct stack abstraction to replace legacy {@link java.util.Stack} and the recommended
 * {@link java.util.Deque}, which is a general purpose double ended queue.
 * @param <T> the type of elements in this stack.
 */
public interface Stack<T> extends Iterable<T> {
    /**
     * Pushes an item onto the top of this stack.
     * @param item the item to be pushed onto this stack.
     */
    void push(T item);

    /**
     * Removes the object at the top of this stack and returns that object as the value of this function.
     * @return The object at the top of this stack.
     */
    T pop();

    /**
     * Returns the number of elements in this stack.
     * @return the number of elements in this stack.
     */
    int size();
}
