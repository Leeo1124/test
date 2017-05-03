package com.leeo.amq.producer;

import java.io.Serializable;

import javax.jms.Destination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageProducerSerivce {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(Destination destination, Serializable message) {
        this.jmsTemplate.convertAndSend(destination, message);
    }
}
