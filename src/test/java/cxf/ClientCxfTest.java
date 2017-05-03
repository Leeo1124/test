package cxf;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.leeo.cxf.CxfService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml",
    "classpath:spring-task.xml",
    "classpath:spring-hibernate.xml",
    "classpath:spring-quartz.xml",
    "classpath:spring-cxf.xml",
    "classpath:spring-amq.xml"}) 
public class ClientCxfTest {

    @Autowired
    @Qualifier("clientService")
    private CxfService clientService;
    
    @Test
    public void testClient(){
        String result = this.clientService.sayHello("are you OK.");
        System.out.println("----:"+result);
    }

}
