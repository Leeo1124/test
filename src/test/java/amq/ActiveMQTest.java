package amq;

import java.math.BigDecimal;
import java.util.Date;

import javax.jms.Destination;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.leeo.amq.SendMessageObject;
import com.leeo.amq.producer.QueueMessageProducer;
import com.leeo.amq.producer.TopicMessageProducer;
import com.leeo.amq.producer.MessageProducerSerivce;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml",
    "classpath:spring-task.xml",
    "classpath:spring-hibernate.xml",
    "classpath:spring-quartz.xml",
    "classpath:spring-amq.xml"}) 
public class ActiveMQTest {  
   
    @Autowired  
    private TopicMessageProducer topicMessageProducer; 
    @Autowired 
    private QueueMessageProducer queueMessageProducer;
    
    @Autowired
    private MessageProducerSerivce messageProducerSerivce;
    @Autowired
    @Qualifier("queueRequest")
    private Destination queueDestination;
    @Autowired
    @Qualifier("topicRequest")
    private Destination topicDestination;
      
    @Test  
    public void sendQueueByString() {  
        String message = "你好，生产者！这是Queue消息.";
        this.queueMessageProducer.send(message);
//        this.messageProducerSerivce.send(this.queueDestination, message);
    } 
    
    @Test  
    public void sendQueueByOject() {  
        SendMessageObject message = new SendMessageObject();
        message.setId(112);
        message.setContent("Object消息");
        message.setCost(new BigDecimal("12.485"));
        message.setCreatTime(new Date());
//        this.queueMessageProducer.send(message);
        this.messageProducerSerivce.send(this.queueDestination, message);
    }
    
    @Test  
    public void sendTopicByString() { 
        String message = "你好，生产者！这是Topic消息.";
        this.topicMessageProducer.send(message);
//        this.messageProducerSerivce.send(this.topicDestination, message);
    }
    
    @Test  
    public void sendTopicByOject() {  
        SendMessageObject message = new SendMessageObject();
        message.setId(112);
        message.setContent("Object消息");
        message.setCost(new BigDecimal("12.485"));
        message.setCreatTime(new Date());
//        this.topicMessageProducer.send(message);
        this.messageProducerSerivce.send(this.topicDestination, message);
    }
} 