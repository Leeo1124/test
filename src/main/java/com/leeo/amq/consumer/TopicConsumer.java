package com.leeo.amq.consumer;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//import com.framework.core.util.ResourceUtil;
//import com.oasis.entity.InterfaceThread;
//import com.oasis.monitor.ModelEnum;
//import com.oasis.monitor.MonitorEvent;
//import com.oasis.monitor.MonitorEventPublisher;

@Component
public class TopicConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(TopicConsumer.class);

    public void receive(final String message) {
//        try {
//            InterfaceThread interfaceThread = (InterfaceThread) ResourceUtil
//                .getBean("interfaceThread");
//            interfaceThread.newLoanRequest(message);
//        } catch (Exception e) {
//            ThreadLendRequestConsumer.logger.error("接收Thread进件处理错误:", e);
//            MonitorEventPublisher.getInstance().publish(
//                new MonitorEvent(ModelEnum.MQLendRequest, e, null, message));
//            throw new RuntimeException(e);
//        }
        logger.info("接收到一个Topic消息,消息内容是：" + message);
    }
    
    public void receive(final TextMessage message) {
        logger.info("接收到一个Topic消息,消息内容是：" + message.toString());
    }
    
    public void receive(final Message message) {
        logger.info("接收到一个Topic消息,消息内容是：" + message.toString());
    }
}
