package 增强的ForkJoinPool;

import java.util.concurrent.RecursiveTask;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/15 0:06
 */
public class RecursiveTaskTask extends RecursiveTask<Integer> {

    //每个小任务最多累加20个数
    private static final int THRESHOLD = 20;
    private int[] arr;
    private int start,end;

    //累加从start到end数组元素
    public RecursiveTaskTask(int[] arr, int start, int end) {
        this.arr = arr;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;
        //当end与start之间的差小于THRESHOLD时开始累加
        if(end - start < THRESHOLD){
            for (int i = start; i < end; i++) {
                sum += arr[i];
            }
            return sum;
        }else{//将大任务分解成小任务
            int middle = (start + end) / 2;
            RecursiveTaskTask left = new RecursiveTaskTask(arr, start, middle);
            RecursiveTaskTask right = new RecursiveTaskTask(arr, middle, end);
            //并行执行两个小任务
            left.fork();
            right.fork();
            //把两个小任务的结果相加
            return left.join() + right.join();
        }
    }
}
