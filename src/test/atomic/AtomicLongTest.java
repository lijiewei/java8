package atomic;

import org.junit.Test;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/6/9 18:43
 */
public class AtomicLongTest {

    @Test
    public void threadTest() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long sum = Counter.addOne();
                    if (sum > 90) {
                        System.out.println("计数器值最终值为  "+ sum);
                    }
                }
            };
            thread.start();
        }
        Thread.sleep(10000);
    }

    @Test
    public void atomicLongTest() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    long sum = Counter2.addOne();
                    if ( sum > 90) {
                        System.out.println("计数器值最终值为  "+ sum);
                    }
                }
            };
            thread.start();
        }
        Thread.sleep(10000);
    }
}
