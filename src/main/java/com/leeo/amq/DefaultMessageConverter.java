package com.leeo.amq;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;

import net.sf.json.util.JSONUtils;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import com.leeo.json.JsonDateFormat;

@Component
public class DefaultMessageConverter implements MessageConverter {
    /**
     * loggerger for this class
     */
    private static final Logger logger = LoggerFactory
        .getLogger(DefaultMessageConverter.class);

    static {
        //格式化JSONObject.toBean的时候Date类型为当前Date的状态。
        JSONUtils.getMorpherRegistry().registerMorpher(
            new JsonDateFormat(new String[] { "yyyy-MM-dd HH:mm:ss",
                "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" }, (Date) null), true);
    }

    @SuppressWarnings("unused")
    @Override
    public Message toMessage(final Object obj, final Session session)
            throws JMSException {
        logger.debug("toMessage(Object, Session) - start");

        //如果传递的是字符串类型
        if (obj instanceof String) {
            return session.createTextMessage(String.valueOf(obj));
        }

        // check Type
        ActiveMQObjectMessage objMsg = (ActiveMQObjectMessage) session
            .createObjectMessage();
        HashMap<String, byte[]> map = new HashMap<String, byte[]>();

        try {
            // POJO must implements Seralizable
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            map.put("POJO", bos.toByteArray());
            objMsg.setObjectProperty("Map", map);

        } catch (IOException e) {
            logger.error("toMessage(Object, Session)", e);
        }
        return objMsg;
    }

    @Override
    public Object fromMessage(Message msg) throws JMSException {
        logger.debug("fromMessage(Message) - start");

        //接收到消息后，判断消息内容。如果是查询或返回结果成功。则调用查询接口。获取数据

        //如果传递的是字符串类型
        if (msg instanceof TextMessage) {
            return ((TextMessage) msg).getText();
        }

        //如果传递的是对象类型
        if (msg instanceof ObjectMessage) {
            HashMap<String, byte[]> map = (HashMap<String, byte[]>) ((ObjectMessage) msg)
                .getObjectProperty("Map");
            try {
                // POJO must implements Seralizable
                ByteArrayInputStream bis = new ByteArrayInputStream(
                    map.get("POJO"));
                ObjectInputStream ois = new ObjectInputStream(bis);
                Object returnObject = ois.readObject();
                return returnObject;
            } catch (IOException e) {
                logger.error("fromMessage(Message)", e);

            } catch (ClassNotFoundException e) {
                logger.error("fromMessage(Message)", e);
            }

            return null;
        } else {
            throw new JMSException("Msg:[" + msg + "] is not Map");
        }
    }
}
