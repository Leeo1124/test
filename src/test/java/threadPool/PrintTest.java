package threadPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.leeo.threadPool.PrintTask;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
"classpath:spring-task.xml" })
//"classpath:spring-context.xml",
public class PrintTest{

    private static int produceTaskSleepTime = 10;

    private static int produceTaskMaxNumber = 100;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

//    @Autowired
//    private PrintTask printTask;

    @Test
    public void testThreadPoolExecutor()
    {

        for (int i = 1; i <= PrintTest.produceTaskMaxNumber; i++) {

            try {
                Thread.sleep(PrintTest.produceTaskSleepTime);
            } catch (final InterruptedException e1) {
                e1.printStackTrace();
            }
            this.threadPoolTaskExecutor.execute(new PrintTask());
//            this.threadPoolTaskExecutor.execute(this.printTask);
        }

    }

}