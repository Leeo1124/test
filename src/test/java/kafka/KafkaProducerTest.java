package kafka;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml", "classpath:spring-amq.xml",
    "classpath:spring-data-jpa.xml","classpath:spring-kafka-producer.xml" })
public class KafkaProducerTest{
    @Autowired  
    private KafkaTemplate<Integer, String> kafkaTemplate;  
      
    /** 
     * 向kafka里写数据. （测试失败）
     */  
    @Test  
    public void testTemplateSend(){  
        this.kafkaTemplate.sendDefault("haha111");  
    }  
}