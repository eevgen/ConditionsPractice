package org.example;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Thread.*;
import static java.util.concurrent.TimeUnit.*;
import static java.util.stream.Stream.*;

public class Main {


    public static void main(String[] args){

        LinkedList<Integer> linkedList = new LinkedList<Integer>();
        Process process = new Process();

        Thread puttingThread = new Thread(() -> {
            try {
                while (!currentThread().isInterrupted()) {
                    process.addingNumbers(linkedList);
                    SECONDS.sleep(2);
                }
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
        });
        Thread gettingThread = new Thread(() -> {
            try {
                while (!currentThread().isInterrupted()) {
                    process.gettingNumbers(linkedList);
                    SECONDS.sleep(2);
                }
            } catch (InterruptedException e) {
                currentThread().interrupt();
            }
        });

        Thread stoppingThread = new Thread(() -> {
            try {
                SECONDS.sleep(10);
                gettingThread.interrupt();
                puttingThread.interrupt();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        stoppingThread.setDaemon(true);

        startThreads(puttingThread, gettingThread, stoppingThread);
    }

    public static void startThreads(Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }

    }