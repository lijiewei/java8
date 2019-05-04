package 接口中的默认方法;

import org.junit.Test;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/4 23:41
 */
public class DefaultTest {

    @Test
    public void ComTest() {
        IOutput output = new OutputImpl();
        System.out.println(output.MAX_CACHE_LINE);
        output.out();
        IOutput.staticTest();
        output.defaultTest();
    }
}
