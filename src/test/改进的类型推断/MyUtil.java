package 改进的类型推断;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/13 23:17
 */
public class MyUtil<E> {

    public static <T> MyUtil<T> get1(){
        return null;
    }

    public static <T> MyUtil<T> get2(T head, MyUtil<T> instal){
        return null;
    }

    E head(){
        return null;
    }
}
