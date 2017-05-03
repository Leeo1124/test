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
public class ThreadCommunicationTest2 {

    private final Lock lock = new ReentrantLock();

    private final Condition conditionA = lock.newCondition();
    private final Condition conditionB = lock.newCondition();

    private static char currentThread = 'A';

    public static void main(String[] args) {
        ThreadCommunicationTest2 test = new ThreadCommunicationTest2();
        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(test.new RunnableA());
        service.execute(test.new RunnableB());
        service.shutdown();
    }

    private class RunnableA implements Runnable {
        public void run() {
            String[] str = {"1","2","3","4","5"};
            for (String s : str) {
                lock.lock();
                try {
                    while (currentThread != 'A') {
                        try {
                            conditionA.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print(s);
                    currentThread = 'B';
                    conditionB.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private class RunnableB implements Runnable {
        @Override
        public void run() {
            String[] str = {"a","b","c","d","e"};
            for (String s : str) {
                lock.lock();
                try {
                    while (currentThread != 'B') {
                        try {
                            conditionB.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.print(s);
                    currentThread = 'A';
                    conditionA.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}