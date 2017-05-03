package com.leeo.quartz;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.leeo.web.service.UserService;

public class ScheduledJob extends QuartzJobBean {

    protected static final Logger logger = LoggerFactory.getLogger(ScheduledJob.class);
    private SendEmailTask sendEmailTask;
    private UserService userService;

    @Override
    protected void executeInternal(JobExecutionContext arg0) {
        logger.info("注入UserService[{}]...", this.userService);
        logger.info("注入SendEmailTask[{}]...", this.sendEmailTask);
    }

    public void setSendEmailTask(SendEmailTask sendEmailTask) {
        this.sendEmailTask = sendEmailTask;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}