package com.leeo.threadPool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class StartTaskThread implements Runnable {

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    private int i;

    public StartTaskThread(ThreadPoolTaskExecutor threadPoolTaskExecutor, int i) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
        this.i = i;
    }

    @Override
    public synchronized void run() {
        final String task = "task@ " + this.i;
        System.out.println("创建任务并提交到线程池中：" + task);
        final FutureTask<String> futureTask = new FutureTask<String>(
                new ThreadPoolTask(task));
        this.threadPoolTaskExecutor.execute(futureTask);
        // 在这里可以做别的任何事情
        String result = null;
        try {
            // 取得结果，同时设置超时执行时间为0.1秒。同样可以用future.get()，不设置执行超时时间取得结果
            result = futureTask.get(1000, TimeUnit.MILLISECONDS);

        } catch (final InterruptedException e) {
            futureTask.cancel(true);
        } catch (final ExecutionException e) {
            futureTask.cancel(true);
        } catch (final Exception e) {
            futureTask.cancel(true);
            // 超时后，进行相应处理
        } finally {
            System.out.println("task@" + this.i + ":result=" + result);
        }

    }
}