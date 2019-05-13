package 改进的线程池;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/14 0:31
 */
public class ThreadPoolTest {

    @Test
    public void ThreadPoolTest() {
        //创建一个ExecutorService对象
        ExecutorService executorService = Executors.newWorkStealingPool();
        //创建Runnable实现类的实列
        Runnable target = () -> {
            for (int i = 0; i < 100; i++){
                System.out.println(Thread.currentThread().getName() + "的i值位：" + i);
            }
        };
        //提交Runnable实列
        executorService.submit(target);
        executorService.submit(target);
        //关闭线程池
        executorService.shutdown();
    }
}
