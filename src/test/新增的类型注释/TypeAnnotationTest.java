package 新增的类型注释;

import org.junit.Test;

import javax.swing.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/14 0:19
 */

/**定义类时使用类型注解*/
@NotNull
public class TypeAnnotationTest implements @NotNull/**implements时使用类型注解*/ Serializable {
    //方法形参中使用类型注解
    @Test
    public void Test(@NotNull String name) throws /**throws时使用类型注解*/ @NotNull Exception {
           Object obj = "ljw.com";
           //强制类型转换时使用类型注解
        String str = (@NotNull String)obj;
        //创建对象时使用类型注解
        Object win = new @NotNull String();
    }
    //泛型中使用类型注解
    public void foo(List<@NotNull String> info){}
}
