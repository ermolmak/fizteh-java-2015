package ru.fizteh.fivt.students.ermolmak.threads.blockingqueue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BlockingQueue<T> {
    private final int maxSize;
    private Queue<T> queue;

    public BlockingQueue(int maxSize) {
        this.maxSize = maxSize;
        queue = new LinkedList<>();
    }

    public synchronized void offer(List<T> e) throws InterruptedException {
        while (queue.size() + e.size() > maxSize) {
            wait();
        }
        queue.addAll(e);
        notifyAll();
    }

    public synchronized List<T> take(int n) throws InterruptedException {
        while (queue.size() < n) {
            wait();
        }
        List<T> result = new ArrayList<>();
        for (int i = 0; i < n; ++i) {
            result.add(queue.remove());
        }
        notifyAll();
        return result;
    }
}
