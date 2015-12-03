package com.leeo.threadPool;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class PrintTask implements Runnable, Serializable {

    private static final long serialVersionUID = 0;

    public PrintTask() {

    }

    @Override
    public synchronized void run() {
        // 处理一个任务，这里的处理方式太简单了，仅仅是一个打印语句
        System.out.println("开始执行任务：" + Thread.currentThread());
        //便于观察，等待一段时间
        try {
            for ( int  i= 0 ; i< 100000000 ; i++){

            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

}