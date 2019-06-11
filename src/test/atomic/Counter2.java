package atomic;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/6/9 18:59
 */
public class Counter2 {

    private static AtomicLong counter = new AtomicLong(0);

    public static long addOne() {
        return counter.incrementAndGet();
    }
}
