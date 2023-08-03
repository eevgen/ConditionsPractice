package org.example;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;

public class Process{

    private Condition condition;
    private int number = 0;
    private final Lock lock = new ReentrantLock();

    public Process() {
        this.condition = lock.newCondition();
    }

    public int createIntegers() {
        lock.lock();
        try {
            return number++;
        } finally {
            lock.unlock();
        }
    }

    public void addingNumbers(LinkedList<Integer> linkedList) {
        lock.lock();
        try {
            while (linkedList.size() >= 90) {
                condition.await();
            }
            int i = this.createIntegers();
            linkedList.add(i);
            System.out.printf("%s add number %d to list\n", currentThread().getName(), i);
            condition.signal();
        } catch (InterruptedException e) {
            currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
    public void gettingNumbers(LinkedList<Integer> linkedList) {
        lock.lock();
        try {
            while (linkedList.isEmpty()) {
                condition.await();
            }
            int i = linkedList.getLast();
            linkedList.remove(linkedList.getLast());
            System.out.printf("%s get number %d from list\n", currentThread().getName(), i);
            condition.signal();
        } catch (InterruptedException e) {
            currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }

}
