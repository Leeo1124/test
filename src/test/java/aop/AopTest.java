package aop;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.leeo.annotation.AnnotationAopService;
import com.leeo.aop.service.AspectService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml", "classpath:spring-task.xml",
    "classpath:spring-hibernate.xml", "classpath:spring-quartz.xml",
    "classpath:spring-amq.xml" })
public class AopTest {
    
    @Autowired
    private AspectService aspectService;
    @Autowired
    private AnnotationAopService annotationAopService;

    @Test
    public void testAop() {
//        this.aspectService.save("saveTest");
        this.aspectService.delete("deleteTest");
    }
    
    @Test
    public void testAopException() {
        this.aspectService.update("updateTest");
    }
    
    @Test
    public void testAnnotationAop() {
        this.annotationAopService.save();
    }
}
