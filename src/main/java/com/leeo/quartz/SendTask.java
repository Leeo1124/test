package com.leeo.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SendTask {

    private static Logger logger = LoggerFactory.getLogger(SendTask.class);

    public void execute() {
//        for(int i=0;i<65;i++){
//            System.out.println("---:"+i);
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
        logger.info("执行'{}'定时任务.-----" + System.currentTimeMillis(), "SendTask");
    }
    
}
