package 改进的类型推断;

import org.junit.Test;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/13 23:21
 */
public class InferenceTest {

    @Test
    public void InferenceTest() {
        //通过方法赋值的目标参数来推断泛型类型为String
        MyUtil<String> install = MyUtil.get1();
        //无须使用下面语句在调用get1()方法时指定泛型的类型
        //MyUtil<String> install2 = MyUtil.<String>get1();

        //可调用get2()方法所需的参数类型来推断泛型为Integer
        MyUtil.get2(42, MyUtil.get1());
        //无须使用下面语句在调用get2()方法时指定泛型的类型
        //MyUtil.get2(42, MyUtil.<Integer>get1());
    }
}
