package utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个线程打印 1~52，另一个线程打印字母A-Z。打印顺序为12A34B56C……5152Z。
 * 
 */
public class ThreadCommunicationTest {

    private final Lock lock = new ReentrantLock();

    private final Condition conditionA = lock.newCondition();
    private final Condition conditionB = lock.newCondition();

    private static char currentThread = 'A';

    public static void main(String[] args) {
        ThreadCommunicationTest test = new ThreadCommunicationTest();
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(test.new RunnableA());
        service.execute(test.new RunnableB());
        service.shutdown();
    }

    private class RunnableA implements Runnable {
        public void run() {
            for (int i = 1; i <= 52; i++) {
                lock.lock();
                try {
                    while (currentThread != 'A') {
                        try {
                            conditionA.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(i);
                    if (i % 2 == 0) {
                        currentThread = 'B';
                        conditionB.signal();
                    }
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private class RunnableB implements Runnable {
        @Override
        public void run() {
            for (char c = 'A'; c <= 'Z'; c++) {
                lock.lock();
                try {
                    while (currentThread != 'B') {
                        try {
                            conditionB.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(c);
                    currentThread = 'A';
                    conditionA.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}