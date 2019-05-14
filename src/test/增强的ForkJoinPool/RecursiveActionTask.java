package 增强的ForkJoinPool;

import java.util.concurrent.RecursiveAction;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/14 23:50
 */
public class RecursiveActionTask extends RecursiveAction {

    //每个小任务最多打印50个数
    private static final int THRESHOLD = 50;
    private int start,end;

    //打印从start到end任务
    public RecursiveActionTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        //当end与start之间的差小于THRESHOLD时开始打印
        if(end - start < THRESHOLD){
            for (int i = start; i < end; i++){
                System.out.println(Thread.currentThread().getName() + "的i值：" + i);
            }
        }else{//将大任务分解成小任务
            int middle = (start + end) / 2;
            RecursiveActionTask left = new RecursiveActionTask(start, middle);
            RecursiveActionTask right = new RecursiveActionTask(middle, end);
            //并行执行两个小任务
            left.fork();
            right.fork();
        }
    }
}
