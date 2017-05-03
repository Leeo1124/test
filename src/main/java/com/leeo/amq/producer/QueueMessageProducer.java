package com.leeo.amq.producer;

import javax.jms.Message;
import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.leeo.amq.SendMessageObject;

@Component
public class QueueMessageProducer {

    @Autowired
    private JmsTemplate template;
    @Autowired
    @Qualifier("queueRequest")
    private Queue destination;

    public void setTemplate(JmsTemplate template) {
        this.template = template;
    }

    public void setDestination(Queue destination) {
        this.destination = destination;
    }
    
    public void send(Message message) {
        this.template.convertAndSend(this.destination, message);
    }
    
    public void send(SendMessageObject message) {
        this.template.convertAndSend(this.destination, message);
    }
    
    public void send(String message) {
        this.template.convertAndSend(this.destination, message);
    }
}
