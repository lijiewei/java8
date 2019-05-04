package Lambda表达式;

/**
 * @Description: 调用者
 * @Author: Administrator
 * @CreateDate: 2019/5/4 20:22
 */
public class Caller {

    /**
     * 调用数组处理和具体的数组“处理行为”分离
     */
    public void dealArray(int[] array, IArrayDeal arrayDeal){
        arrayDeal.deal(array);
    }

    /**
     * 调用数字处理和具体的数组“处理行为”分离
     */
    public int dealNum(int a, int b, ICalculator calculator){
        return calculator.dealNum(a, b);
    }
}
