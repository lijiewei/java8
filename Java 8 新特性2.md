## Java 8 新特性2



本文介绍以下新特性：

- java.util.concurrent

  - java.util.concurrent.locks.StampedLock

  - java.util.concurrent.ConcurrentHashMap
  - java.util.concurrent.ForkJoinPool
  - java.util.concurrent.atomic
    - DoubleAccumulator
    - DoubleAdder
    - LongAccumulator
    - LongAdder

- 新生代

  

------

#### java.util.concurrent.locks.StampedLock

改进的读写锁StampedLock在大多数场景中它可以替代传统的ReentrantReadWriteLock(可重入读写锁)

悲观锁：每次拿数据的时候就去锁上（读和写---互斥，写和写----互斥）。
乐观锁：每次去拿数据的时候，都没锁上，而是判断标记位是否有被修改，如果有修改就再去读一次（读和写---不互斥，写和写----互斥）。

为读写操作提供了三种锁模式：

- writeLock(悲观锁)

- readLock(悲观锁)

- tryOptimisticRead(乐观锁)

```java
StampedLockTest类

	private double x, y;//内部定义表示坐标点
	private final StampedLock lock  = new StampedLock();//定义了StampedLock锁,

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
```

#### java.util.concurrent.ConcurrentHashMap

支持多线程并发写入访问，永远不会锁住整个集合，并发写入时有较好性能，默认支持16个线程并发写入

java8新增了30多个新方法大致分为三类：

forEach系列（forEach,forEachKey,forEachValue,forEachEntry）

search系列（search,searchKeys,searchValues,searchEntries）

reduce系列（reduce,reduceToDouble,reduceToLong,reduceKeys,reduceValues）



#### java.util.concurrent.ForkJoinPool

将一个任务拆分成多个“小任务”并进行计算的特殊线程池

详情参看 `《Java 8 新特性》`的 `增强的ForkJoinPool`

