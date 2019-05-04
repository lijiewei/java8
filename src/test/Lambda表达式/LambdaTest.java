package Lambda表达式;

import org.junit.Test;

import javax.swing.*;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/4 20:13
 */
public class LambdaTest {

    @Test
    public void InnerClassTest() {
        //匿名内部类的方式
        Caller caller = new Caller();
        int[] array = {3, -4, 6, 4};
        caller.dealArray(array, new IArrayDeal() {
            @Override
            public void deal(int[] target) {
                int sum = 0;
                for (int tmp : target) {
                    sum += tmp;
                }
                System.out.println("数组总和：" + sum);
            }
        });
    }

    @Test
    public void LambdaTest() {
        //Lambda表达式的方式,不需要重新方法名字和返回值类型
        Caller caller = new Caller();
        int[] array = {3, -4, 6, 4};
        caller.dealArray(array, targetArray -> {
            int sum = 0;
            for (int tmp : targetArray) {
                sum += tmp;
            }
            System.out.println("数组总和：" + sum);
        });
    }

    @Test
    public void InnerClassTest1() {
        //匿名内部类的方式
        Caller caller = new Caller();
        int sum = caller.dealNum(2, 3, new ICalculator() {
            @Override
            public int dealNum(int a, int b) {
                return a + b;
            }
        });
        System.out.println(sum);
    }

    @Test
    public void LambdaTest1() {
        //Lambda表达式的方式,不需要重新方法名字和返回值类型
        Caller caller = new Caller();
        int sum = caller.dealNum(2, 3, (a, b) -> a + b);
        System.out.println(sum);
    }

    @Test
    public void LambdaTest2() {
        Runnable r = () -> {
            for (int i = 0 ;i < 100 ; i++){
                System.out.println(i);
            }
        };
    }

    @Test
    public void LambdaTest3() {
          //1.引用类方法
        IConverter converter = from -> Integer.valueOf(from);
        Integer val = converter.convert("99");
        System.out.println(val);

        IConverter converter1 = Integer::valueOf;
        Integer val2 = converter1.convert("99");
        System.out.println(val2);
    }

    @Test
    public void LambdaTest4() {
        //2.引用特定对象的实例方法
        IConverter converter = from -> "fkit.org".indexOf(from);
        Integer val = converter.convert("it");
        System.out.println(val);

        IConverter converter1 = "fkit.org" :: indexOf;
        Integer val2 = converter1.convert("it");
        System.out.println(val2);
    }

    @Test
    public void LambdaTest5() {
        //3.引用某类对象的实例方法
        ISubStr subStr = (a, b, c) -> a.substring(b, c);
        String val = subStr.test("Java I Love you", 2, 9);
        System.out.println(val);

        ISubStr subStr2 = String :: substring;
        String val2 = subStr2.test("Java I Love you", 2, 9);
        System.out.println(val2);
    }

    @Test
    public void LambdaTest6() {
        //4.引用构造器
        IHome home = (String a) -> new JFrame(a);
        JFrame val = home.win("我的窗口");
        System.out.println(val);

        IHome home2 = JFrame::new;
        JFrame val2 = home2.win("我的窗口");
        System.out.println(val2);
    }

    private int age = 12;
    private static String name = "Lambda表达式与匿名内部类的联系和区别";
    @Test
    public void LambdaTest7() {
        test();
    }
    private void test(){
        String book = "我在里面了";
        IDisPlayable dis = () -> {
            System.out.println("book局部变量："+ book);
            System.out.println("外部类的age实例变量："+age);
            System.out.println("外部类的namg类变量为"+name);
        };
        dis.display();
        //调用dis对象从接口中继承的add()方法
        System.out.println(dis.add(2, 3));
    }

}
