package 改进的匿名内部类;

import org.junit.Test;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/4 18:40
 */
public class InternalClassTest {

    interface A {
        void test();
    }
    @Test
    public void InternalClassTest() {
            //Java8开始，匿名内部类、局部内部类允许范围非final的局部变量
            //默认使用了final修饰,可以不写
            int age = 8;
            //age = 2; 报错
            A a = new A() {
                @Override
                public void test() {
                    System.out.println(age);
                }
            };
            //调用方法
            a.test();
    }
}
