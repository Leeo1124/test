package threadPool;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.leeo.threadPool.StartTaskThread;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-thread.xml" })
public class TestThreadPool{

    private static int produceTaskSleepTime = 10;

    private static int produceTaskMaxNumber = 1000;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return this.threadPoolTaskExecutor;
    }

    public void setThreadPoolTaskExecutor(
            ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @Test
    public void testThreadPoolExecutor()
    {
//        // 构造一个线程池
//        final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 600,
//            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(3),
//            new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 1; i <= TestThreadPool.produceTaskMaxNumber; i++) {

            try {
                Thread.sleep(TestThreadPool.produceTaskSleepTime);
            } catch (final InterruptedException e1) {
                e1.printStackTrace();
            }
            new Thread(new StartTaskThread(this.threadPoolTaskExecutor,i)).start();
        }

    }

}