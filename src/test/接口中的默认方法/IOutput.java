package 接口中的默认方法;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/4 23:43
 */
public interface IOutput {
    /** 静态常量. */
    public static final int  MAX_CACHE_LINE = 50;

    /** 抽象实列方法. */
    void out();

    /** 抽象实列方法. */
    void getData(String msg);

    /**
     * 类方法
     * @return String
     */
    static String staticTest(){
        return "接口里的类方法";
    }

    /**
     * 默认方法
     * @param msgs
     * @return
     */
    default void print(String... msgs){
        for (String msg : msgs){
            System.out.println(msg);
        }
    }

    /**
     * 默认方法
     * @return
     */
    default void defaultTest(){
        System.out.println("默认的test()方法");
    }

}
