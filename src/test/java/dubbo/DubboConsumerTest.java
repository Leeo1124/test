package dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.leeo.dubbo.TestRegistryService;

/**
 * 像调用本地一样调用服务的具体实现
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml",
    "classpath:spring-task.xml",
    "classpath:spring-hibernate.xml",
    "classpath:spring-dubbo-consumer.xml",
    "classpath:spring-cxf.xml",
    "classpath:spring-amq.xml"})
public class DubboConsumerTest {

    @Autowired
    @Qualifier("testRegistryConsumerService")
    private TestRegistryService testRegistryService;
    
    @Test
    public void callService(){
        String name = this.testRegistryService.hello("zz");
        System.out.println("xx==" + name);
    }
}

