package Lambda表达式;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/4 22:18
 */
@FunctionalInterface
public interface IDisPlayable {
    //定义一个抽象方法和默认方法
    void display();
    default int add(int a, int b){
        return a + b;
    }
}
