package com.leeo.amq.producer;

import javax.jms.Message;
import javax.jms.Topic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class TopicMessageProducer {
    
    @Autowired
    private JmsTemplate template;
    @Autowired
    @Qualifier("topicRequest")
	private Topic destination;

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	public void setDestination(Topic destination) {
		this.destination = destination;
	}

	public void send(Message message) {
	    this.template.convertAndSend(this.destination, message);
	}
	
	public void send(String message) {
	    this.template.convertAndSend(this.destination, message);
	}
}
