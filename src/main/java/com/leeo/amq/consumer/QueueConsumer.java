package com.leeo.amq.consumer;

import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.leeo.amq.SendMessageObject;

@Component
public class QueueConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(QueueConsumer.class);

    public void receive(final String message) {
//        try {
//            HermesInterface hermesInter = (HermesInterface) ResourceUtil
//                .getBean("hermesInterface");
//            hermesInter.saveHermesInfoByJson(message);
//        } catch (Exception e) {
//            HermesQueueConsumer.logger.error("接收Hermes消息处理错误:", e);
//            throw new RuntimeException(e);
//        }
        logger.info("接收到一个Queue消息,消息内容是：" + message);
    }

    public void receive(final TextMessage message) {
//        try {
//            HermesInterface hermesInter = (HermesInterface) ResourceUtil
//                .getBean("hermesInterface");
//            hermesInter.saveHermesInfoByJson(message.getText());
//        } catch (Exception e) {
//            HermesQueueConsumer.logger.error("接收Hermes消息处理错误:", e);
//            throw new RuntimeException(e);
//        }
        logger.info("接收到一个Queue消息,消息内容是：" + message.toString());
    }

    public void receive(final Message message) {
//        try {
//            HermesInterface hermesInter = (HermesInterface) ResourceUtil
//                .getBean("hermesInterface");
//            hermesInter.saveHermesInfoByJson(message.toString());
//        } catch (Exception e) {
//            HermesQueueConsumer.logger.error("接收Hermes消息处理错误:", e);
//            throw new RuntimeException(e);
//        }
        logger.info("接收到一个Queue消息,消息内容是：" + message.toString());
    }
    
    public void receive(final SendMessageObject message) {
        logger.info("接收到一个Queue消息,消息内容是：" + message.toString());
    }
}
