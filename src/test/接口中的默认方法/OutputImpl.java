package 接口中的默认方法;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/4 23:45
 */
public class OutputImpl implements IOutput {

    @Override
    public void out() {
        System.out.println("实现类的out抽象实列方法");
    }

    @Override
    public void getData(String msg) {
        System.out.println("抽象实列方法，传入参数为："+msg);
    }
}
