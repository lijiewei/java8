package StampedLock锁;

import org.junit.Test;

import java.util.concurrent.locks.StampedLock;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/6/3 22:50
 */
public class StampedLockTest {

    private double x, y;//内部定义表示坐标点
    private final StampedLock lock  = new StampedLock();//定义了StampedLock锁,

    @Test
    public void stampedLockTest() {
        write(1,2);
        System.out.println(read());

    }

    private double read(){
        //尝试获取乐观锁，返回时间戳即标记位
        long stamp  = lock.tryOptimisticRead();
        double currentX = x, currentY = y;
        //这里可能会出现了写操作，因此要进行判断
        if(lock.validate(stamp)){//有效，未发生写操作
            return currentX * currentX + currentY * currentY;
        }else{
            //被强占，可以重新获取乐观锁，或者获取悲观锁
            stamp = lock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                // 释放悲观锁
                lock.unlockRead(stamp);
            }
            return currentX * currentX + currentY * currentY;
        }
    }

    private void write(double newX, double newY){
        //获取悲观锁
        long stamp  = lock.readLock();
        //转换为写锁，失败就一直转换
        try {
            while(true){
                long wrStamp = lock.tryConvertToWriteLock(stamp);
                //转换成功，设置新值，退出循环
                if(wrStamp != 0 ){
                    stamp = wrStamp;
                    x = newX;
                    y = newY;
                    break;
                }else{
                    //释放读锁，获取写锁，循环
                    lock.unlockRead(stamp);
                    stamp = lock.writeLock();
                }
            }
        } finally {
            //第一次转换成功释放读锁，否则释放写锁
            lock.unlock(stamp);
        }
    }
}
