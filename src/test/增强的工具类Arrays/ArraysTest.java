package 增强的工具类Arrays;

import org.junit.Test;

import java.util.Arrays;
import java.util.Spliterator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/4 16:27
 */
public class ArraysTest {

    @Test
    public void arraysTest() {

        int[] arr1 = new int[]{3,-4,25,16,30,18};
        //对数组arr1并发排序
        Arrays.parallelSort(arr1);
        System.out.println(Arrays.toString(arr1));

        int[] arr2 = new int[]{3,-4,25,16,30,18};
        //使用op参数指定的计算公式计算得到的结果作为新的元素
        Arrays.parallelPrefix(arr2, new IntBinaryOperator() {
            //left 代表数组中前一个索引处的元素，计算第一个元素时，left为1
            //right 代表数组中当前索引处的元素
            @Override
            public int applyAsInt(int left, int right) {
                return left * right;
            }
        });
        System.out.println(Arrays.toString(arr2));

        int[] arr3 = new int[5];
        //使用指定的生成器（generator）为所有数组元素设置值
        Arrays.parallelSetAll(arr3, new IntUnaryOperator() {
            //operand 当前元素索引
            @Override
            public int applyAsInt(int operand) {
                return operand * 5;
            }
        });
        System.out.println(Arrays.toString(arr3));
    }
}
