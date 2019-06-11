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

      

- JVM中的堆

  - 新生代
  - 老年代
  - 永久代

  

------

更多api，查看中文http://www.matools.com/api/java8和英文https://docs.oracle.com/javase/8/docs/api/

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

![加锁区别](F:\workspaceGit\java8\加锁区别.png)

对于 ConcurrentHashMap，Java8 也引入了`红黑树`,所以其由 **数组+链表+红黑树** 组成

支持多线程并发写入访问，永远不会锁住整个集合，并发写入时有较好性能，默认支持16个线程并发写入

java8新增了30多个新方法大致分为三类：

forEach系列（forEach,forEachKey,forEachValue,forEachEntry）`对每个元素执行给定的操作`

search系列（search,searchKeys,searchValues,searchEntries）`返回在每个元素上应用给定函数的第一个可用非空结果; 当找到结果时跳过进一步的搜索`

reduce系列（reduce,reduceToDouble,reduceToLong,reduceKeys,reduceValues）`累积每个元素`

也添加了一些其它方法，比如 `mappingCount `和 `newKeySet`



这些批量操作接受一个`parallelismThreshold`参数,如果map的size小于给的阈值，则使用`Long.MAX_VALUE`作为最大并发量；如果设置为1，则产生足够多的子任务来并发执行



在`ConcurrentHashMap`中如果存储大量的元素，那么使用`size()`方法获取的结果可能不正确，因为其是用`int`类型作为返回值，`map.mappingCount()`也能返回容器大小为`long`类型

常用方法：

```java
//遍历key-value对,对每个key-value对做给定action操作
void forEach(BiConsumer<? super K,? super V> action)
    
//遍历key,对每个key执行给定的action操作，parallelismThreshold为并发量
void forEachKey(long parallelismThreshold, Consumer<? super K> action)
    
//遍历value,对每个value执行给定的action操作，parallelismThreshold为并发量
void forEachValue(long parallelismThreshold, Consumer<? super V> action)
    
//遍历entry,对每个entry执行给定的action操作，parallelismThreshold为并发量
forEachEntry(long parallelismThreshold, Consumer<? super Map.Entry<K,V>> action)
    
//遍历key-value对,使用searchFunction查找返回非空结果，如果没有则返回null。
<U> U search(long parallelismThreshold, BiFunction<? super K,? super V,? extends U> searchFunction)
    
//遍历key,使用searchFunction查找返回非空结果，如果没有则返回null。
<U> U  searchKeys(long parallelismThreshold, Function<? super K,? extends U> searchFunction)

//遍历value,使用searchFunction查找返回非空结果，如果没有则返回null。
<U> U searchValues(long parallelismThreshold, Function<? super V,? extends U> searchFunction)
    
//遍历entry,使用searchFunction查找返回非空结果，如果没有则返回null。
<U> U searchEntries(long parallelismThreshold, Function<Map.Entry<K,V>,? extends U> searchFunction)
    
//遍历key-value对,按照transformer转换后，按照reducer累加，如果没有则返回null。
<U> U reduce(long parallelismThreshold, BiFunction<? super K,? super V,? extends U> transformer, BiFunction<? super U,? super U,? extends U> reducer)
    
//遍历key-value对,按照transformer转换后，按照reducer累加，basis基础值，如果没有则返回null。
double reduceToDouble(long parallelismThreshold, ToDoubleBiFunction<? super K,? super V> transformer, double basis, DoubleBinaryOperator reducer)
    
//遍历key-value对,按照transformer转换后，按照reducer累加，basis基础值，如果没有则返回null。
int reduceToInt(long parallelismThreshold, ToIntBiFunction<? super K,? super V> transformer, int basis, IntBinaryOperator reducer)
    
//遍历key-value对,按照transformer转换后，按照reducer累加，basis基础值，如果没有则返回null。
long reduceToLong(long parallelismThreshold, ToLongBiFunction<? super K,? super V> transformer, long basis, LongBinaryOperator reducer)
    
//遍历key,按照reducer累加，如果没有则返回null。
K reduceKeys(long parallelismThreshold, BiFunction<? super K,? super K,? extends K> reducer)
    
//遍历value,按照reducer累加，如果没有则返回null。
V reduceValues(long parallelismThreshold, BiFunction<? super V,? super V,? extends V> reducer)
 
//返回容器映射数量
long mappingCount()
    
//创建一个新的Set，存放转换成Boolean类型后的值
static <K> ConcurrentHashMap.KeySetView<K,Boolean> 	newKeySet()
    
//使用remappingFunction根据原key-value对计算一个新value，新value不为null则替换；新value为null则删除原key-value对；新旧value同时为null则不改变任何key-value对，直接返回null
V compute(K key, BiFunction<? super K,? super V,? extends V> remappingFunction)
 
//如果key对应value为null,使用mappingFunction根据key计算一个新value，新value不为null则替换；如果map中不存在key则添加一组key-value对
V computeIfAbsent(K key, Function<? super K,? extends V> mappingFunction)
    
//如果key对应value为null,使用remappingFunction根据旧key、value计算一个新value，新value不为null则替换；如果新value为null则删除旧key-value对
V computeIfPresent(K key, BiFunction<? super K,? super V,? extends V> remappingFunction)

```

#### java.util.concurrent.ForkJoinPool

Java7提供了ForkJoinPool来支持将一个任务拆分成多个“小任务”并行计算，再把多个“小任务”的结果合并成总的计算结果。ForkJoinPool是ExecutorService的实现类，因此是一种特殊的线程池。

ForkJoinPool常用构造器：

~~~java
```java
//创建一个保护parallelism个并行线程的ForkJoinPool。
ForkJoinPool（int parallelism）
```

//以Runtime.availableProcessors()方法的返回值作为parallelism参数来创建ForkJoinPool
ForkJoinPool（）
~~~

Java8为ForkJoinPool增加了通用池功能。ForkJoinPool类通过如下两个静态方法提供通用池功能：

```java
//该方法返回一个通用池，通用池的运行状态不会受shutdown（）或shutdownNow()方法的影响。
ForkJoinPool commonPool()

//该方法返回通用池的并行级别
int getCommonPoolParallelism()
```

创建了ForkJoinPool实例后，可调用ForkJoinPool的submit(ForkJoinTask task)或invoke(ForkJoinTask task)方法来执行指定任务了。其中ForkJoinTask代表一个可以并行、合并的任务。

ForkJoinTask是一个抽象类，有两个抽象子类：RecursiveAction和RecursiveTask。其中RecursiveTask代表有返回值的任务，RecursiveAction代表没有返回值的任务

Future<T>  <--  ForkJoinTask<T>  <--  RecursiveAction/RecursiveTask

Executor  <--  ExecutorService  <--  AbstractExecutorService  <--  ForkJoinPool

```java
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
```

```java
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
```

```java
@Test
public void RecursiveActionTest() throws InterruptedException {
    //创建一个通用池
    ForkJoinPool pool = new ForkJoinPool();
    //提交可分解的printTask任务
    pool.submit(new RecursiveActionTask(0, 300));
    pool.awaitTermination(2, TimeUnit.SECONDS);
    pool.shutdown();
}

@Test
public void RecursiveTaskTaskTest() throws InterruptedException, ExecutionException {
    int[] arr = new int[100];
    Random rand = new Random();
    int total = 0;
    for (int i = 0, len = arr.length; i < len; i++) {
        int tmp = rand.nextInt(20);
        total += (arr[i] = tmp);
    }
    System.out.println("total:" + total);

    //创建一个通用池
    ForkJoinPool pool = new ForkJoinPool();
    //提交可分解的printTask任务
    Future<Integer> future = pool.submit(new RecursiveTaskTask(arr, 0, arr.length));
    System.out.println(future.get());
    pool.shutdown();
}


```

------

#### java.util.concurrent.atomic

AtomicBoolean、AtomicInteger、AtomicLong、AtomicReference，这几个类的共同特点是都提供单个变量的原子方式访问和更新功能。

AtomicLong举例说明：

通常情况下，在Java中的++i或者–i不是线程安全的。一般情况下，只能加锁才能保证上述操作的原子性。有了AtomicLong后，使用AtomicLong就可以保证上述操作的原子性。

```java
public class Counter {
    private static long counter = 0;

    public static long addOne() {
        return ++counter;
    }
}

@Test
public void threadTest() throws InterruptedException {
    for (int i = 0; i < 100; i++) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long sum = Counter.addOne();
                if (sum > 90) {
                    System.out.println("计数器值最终值为  "+ sum);
                }
            }
        };
        thread.start();
    }
    Thread.sleep(10000);
}
```

`发现打印的结果大多数情况下都没有等于100的，说明多线程下++i不安全`

```java
public class Counter2 {

    private static AtomicLong counter = new AtomicLong(0);

    public static long addOne() {
        return counter.incrementAndGet();
    }
}

@Test
public void atomicLongTest() throws InterruptedException {
    for (int i = 0; i < 100; i++) {
        Thread thread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long sum = Counter2.addOne();
                if ( sum > 90) {
                    System.out.println("计数器值最终值为  "+ sum);
                }
            }
        };
        thread.start();
    }
    Thread.sleep(10000);
}
```

`发现打印的结果每次都正常`

#### java.util.concurrent.atomic.LongAdder

LongAdder继承Striped64,最核心的方法已经由Striped64实现

是对AtomicLong类的改进。比如LongAccumulator与LongAdder在高并发环境下比AtomicLong更高效

与AtomicLong相比，LongAdder更多地用于`收集统计数据`，而不是细粒度的同步控制。在低并发环境下，两者性能很相似。但在高并发环境下，LongAdder有着明显更高的吞吐量，但是有着更高的空间复杂度。

```java
//添加给定值
void add(long x)

//相当于`add(-1)`
void 	decrement()  

//返回`sum()`为`double`一个宽元转换后
double 	doubleValue()

//返回`sum()`为`float`一个宽元转换后
float 	floatValue()

//相当于`add(1)`
void 	increment()

//在缩小原始int后返回sum()作为int
int 	intValue()

//相当于`sum()`
long 	longValue()

//将保持总和的变量重置为零
void 	reset()

//返回当前总和
long 	sum()

//相当于`sum()`后跟`reset()`
long 	sumThenReset()

//返回`sum()`的String表示形式
String 	toString()
```



#### java.util.concurrent.atomic.LongAccumulator

实现与LongAdder基本一致,只是构造器中可以自定义一个LongBinaryOperator

```java
//具有给定值的更新。
void 	accumulate(long x)

//返回 current value为 double一个宽元转换后。
double 	doubleValue()

//返回 current value为 float一个宽元转换后。
float 	floatValue()

//返回当前值。
long 	get()

//相当于 get()其次是 reset() 。
long 	getThenReset()

//在 缩小原始 int后返回 current value作为int。
int 	intValue()

//相当于 get() 。
long 	longValue()

//重置维持更新到标识值的变量。
void 	reset()

//返回当前值的String表示形式。
String 	toString()
```



#### java.util.concurrent.atomic.DoubleAdder

与LongAdder类似

#### java.util.concurrent.atomic.DoubleAccumulator

与LongAccumulator类似

------

### java.util.concurrent中新的类和接口

增加了2个新接口和4个新类:

1. 接口 [CompletableFuture.AsynchronousCompletionTask](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.AsynchronousCompletionTask.html)
2. 接口 [CompletionStage](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletionStage.html)
3. 类 [CompletableFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletableFuture.html)
4. 类 [ConcurrentHashMap.KeySetView](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.KeySetView.html)
5. 类 [CountedCompleter](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CountedCompleter.html)
6. 类 [CompletionException](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/CompletionException.html)

## JVM中的堆

JVM中的堆，一般分为三大部分：新生代、老年代、永久代

参考文章：https://www.cnblogs.com/cuijj/p/10499621.html

#### 新生代

主要是用来存放新生的对象。一般占据堆的1/3空间。由于频繁创建对象，所以新生代会频繁触发MinorGC进行垃圾回收。

#### 老年代

老年代的对象比较稳定，所以MajorGC不会频繁执行。

在进行MajorGC前一般都先进行了一次MinorGC，使得有新生代的对象晋身入老年代，导致空间不够用时才触发。当无法找到足够大的连续空间分配给新创建的较大对象时也会提前触发一次MajorGC进行垃圾回收腾出空间。

当老年代也满了装不下的时候，就会抛出OOM（Out of Memory）异常。

#### 永久代

“方法区” 主要存储的信息包括：常量信息，类信息，方法信息，而且是全局共享的（多线程共享）；

通常情况下， 很多人把 “方法区” 和“永生代”  对等；  换句话说，是利用“永生代”去实现“方法区”， 这样可能导致OOM （因为“永生代”的大小是可以通过-XX：PermSize  -XX：MaxPermSize）设置的



永久代指内存的永久保存区域，主要存放Class和Meta（元数据）的信息。

Class在被加载的时候被放入永久区域。它和和存放实例的区域不同，GC不会在主程序运行期对永久区域进行清理。所以这也导致了永久代的区域会随着加载的Class的增多而胀满，最终抛出OOM异常。

当永久代满时也会引发Full GC，会导致Class、Method元信息的卸载。



`在Java8中，永久代已经被移除，被一个称为“元数据区”（元空间）的区域所取代。`

元空间的本质和永久代类似，都是对JVM规范中方法区的实现。不过元空间与永久代之间最大的区别在于：元空间并不在虚拟机中，而是使用本地内存。因此，默认情况下，元空间的大小仅受本地内存限制。类的元数据放入
native memory, 字符串池和类的静态变量放入java堆中. 这样可以加载多少类的元数据就不再由MaxPermSize控制, 而由系统的实际可用空间来控制。

JDK 8 中永久代向元空间的转换原因：

　1、字符串存在永久代中，容易出现性能问题和内存溢出。

　2、类及方法的信息等比较难确定其大小，因此对于永久代的大小指定比较困难，太小容易出现永久代溢出。

　3、永久代会为 GC 带来不必要的复杂度，并且回收效率偏低。

   4、Oracle 可能会将HotSpot 与 JRockit 合二为一


--------------------- 
