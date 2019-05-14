package 增强的ForkJoinPool;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.*;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/14 23:49
 */
public class ForkJoinPoolTest {

    @Test
    public void RecursiveActionTest() throws InterruptedException {
        //创建一个通用池
        ForkJoinPool pool = new ForkJoinPool();
        //提交可分解的printTask任务
        pool.submit(new RecursiveActionTask(0, 300));
        pool.awaitTermination(2, TimeUnit.SECONDS);
        pool.shutdown();
    }

    @Test
    public void RecursiveTaskTaskTest() throws InterruptedException, ExecutionException {
        int[] arr = new int[100];
        Random rand = new Random();
        int total = 0;
        for (int i = 0, len = arr.length; i < len; i++) {
            int tmp = rand.nextInt(20);
            total += (arr[i] = tmp);
        }
        System.out.println("total:" + total);

        //创建一个通用池
        ForkJoinPool pool = new ForkJoinPool();
        //提交可分解的printTask任务
        Future<Integer> future = pool.submit(new RecursiveTaskTask(arr, 0, arr.length));
        System.out.println(future.get());
        pool.shutdown();
    }
}
