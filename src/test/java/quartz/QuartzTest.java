package quartz;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context.xml",
"classpath:spring-quartz.xml" })
public class QuartzTest{


    @Test
    public void testExecutor()
    {

    }

}