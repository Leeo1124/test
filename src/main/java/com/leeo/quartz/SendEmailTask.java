package com.leeo.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SendEmailTask {

    private static Logger logger = LoggerFactory.getLogger(SendEmailTask.class);

    public void execute() {
        logger.info("执行'{}'定时任务.-----" + System.currentTimeMillis(), "SendEmailTask");
    }
    
}
