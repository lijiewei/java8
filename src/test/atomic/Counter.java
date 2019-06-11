package atomic;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/6/9 18:42
 */
public class Counter {
    private static long counter = 0;

    public static long addOne() {
        return ++counter;
    }
}
