public interface Deque<T> {
    boolean isEmpty();

    int size();

    void printDeque();

    void addFirst(T item);

    void addLast(T item);

    T removeFirst();

    T removeLast();

    T get(int index);
}
