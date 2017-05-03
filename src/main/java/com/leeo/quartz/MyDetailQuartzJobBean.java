package com.leeo.quartz;

import java.lang.reflect.Method;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class MyDetailQuartzJobBean extends QuartzJobBean {

    protected static final Logger logger = LoggerFactory.getLogger(MyDetailQuartzJobBean.class);

    private String targetObject;
    private String targetMethod;
    private ApplicationContext ctx;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        try {
            logger.info("--:"+context.getMergedJobDataMap().get("targetObject"));
            logger.info("--:"+context.getMergedJobDataMap().get("targetMethod"));
            logger.info("execute [" + this.targetObject + "] at once>>>>>>");
            Object target = this.ctx.getBean(this.targetObject);
            Method method = target.getClass().getMethod(this.targetMethod, new Class[] {});
            method.invoke(target, new Object[] {});
        } catch (Exception e) {
            logger.error("执行"+this.targetObject+"方法"+this.targetMethod+"失败，失败信息：", e);
            throw new JobExecutionException(e);
        }

    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

}