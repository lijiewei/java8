package 增强的包装类;

import org.junit.Test;
import org.omg.CORBA.INTERNAL;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/4 17:35
 */
public class IntegerTest {

    @Test
    public void IntegerTest() {

        byte b = -3;
        //将byte类型的-3转换为无符号整数
        System.out.println(Byte.toUnsignedInt(b));//253

        //指定使用十六进制解析无符号整数
        System.out.println(Integer.parseUnsignedInt("ab", 16));//171

        //将-12转换为无符号int型，然后转换为十六进制的字符串
        System.out.println(Integer.toUnsignedString(-12, 16));//fffffff4

        //将两个数转换为无符号整数后相除
        System.out.println(Integer.divideUnsigned(-2, 3));//1431655764

        //将两个整数转换为无符号整数后计算他们相除的余数
        System.out.println(Integer.remainderUnsigned(-2, 7));//2
    }
}
