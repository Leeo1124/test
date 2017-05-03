package transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.leeo.transactional.trans.TransServicesA;

/**
 * 事务测试
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml",
    "classpath:spring-task.xml",
    "classpath:spring-hibernate.xml",
    "classpath:spring-quartz.xml" })
public class TransTest1 {

    @Autowired
    public TransServicesA transServicesA;

    @Test
    public void test() {
        this.transServicesA.saveA();
    }
}
