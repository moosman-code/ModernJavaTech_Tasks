package bg.sofia.uni.fmi.mjt.sentimentanalyzer;

import java.util.LinkedList;
import java.util.Queue;

public class CustomBlockingQueue<T> {

    private final Queue<T> queue;
    private final int capacity;

    public CustomBlockingQueue(int capacity) {
        this.queue = new LinkedList<>();
        this.capacity = capacity;
    }

    public CustomBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    public boolean isEmpty() {
        synchronized (queue) {
            return queue.isEmpty();
        }
    }

    public boolean isFull() {
        synchronized (queue) {
            return queue.size() == capacity;
        }
    }

    public void add(T value) throws InterruptedException {
        synchronized (queue) {
            while (isFull()) {
                queue.wait();
            }

            queue.add(value);
            queue.notifyAll();
        }
    }

    public T take() throws InterruptedException {
        synchronized (queue) {
            while (queue.isEmpty()) {
                queue.wait();
            }

            queue.notifyAll();
            return queue.poll();
        }
    }
}
