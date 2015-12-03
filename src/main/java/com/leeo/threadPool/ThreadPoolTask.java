package com.leeo.threadPool;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class ThreadPoolTask implements Callable<String>, Serializable {

    private static final long serialVersionUID = 0;

    // 保存任务所需要的数据
    private Object threadPoolTaskData;

    private static int consumeTaskSleepTime = 2000;

    public ThreadPoolTask(Object tasks) {
        this.threadPoolTaskData = tasks;
    }

    @Override
    public synchronized String call() {
        // 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
        System.out.println("开始执行任务：" + this.threadPoolTaskData);
        System.out.println("------------: " + Thread.currentThread());
        String result = "";
        // //便于观察，等待一段时间
        try {
//            long r = 5/0;
            for ( int  i= 0 ; i< 100000000 ; i++){

            }
            result = "OK";
        } catch (final Exception e) {
            e.printStackTrace();
            result = "ERROR";
        }
        this.threadPoolTaskData = null;
        return result;
    }
}