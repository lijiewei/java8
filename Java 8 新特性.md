## Java 8 新特性



本文介绍以下新特性：



- 增强的工具类Arrays
- 增强的包装类
- 接口中的默认方法
- 改进的匿名内部类
- Lambda表达式
- 新增的日期、时间包
- 新增的日期、时间格式器
- 增强的Iterator遍历集合元素
- 新增的Predicate操作集合
- 新增的Stream操作集合
- 改进的List接口和ListIterator接口
- 为Map新增的方法
- 改进的类型推断
- 函数式接口与@FunctionalInterface
- 新增的重复注释
- 新增的类型注释
- 改进的线程池
- 增强的ForkJoinPool
- 新增的方法参数反射



---------

#### 增强的工具类Arrays

Java8 为Arrays增加了一些工具方法，这些工具方法可以充分利用多CPU并行的能力来提高设值、排序的性能

```java
//使用op参数指定的计算公式计算得到的结果作为新的元素。
void parallelPrefix(xxx[] array, XxxBinaryOperator op)
    
//使用op参数指定的计算公式计算fromIndex到toIndex索引的元素得到的结果作为新的元素
void parallelPrefix(xxx[] array, int fromIndex, int toIndex, XxxBinaryOperator op)
    
//使用指定的生成器（generator）为所有数组元素设置值，该生成器控制数组元素的值的生成算法
void setAll(xxx[] array, IntToXxxFunction generator)
    
//使用指定的生成器（generator）为所有数组元素设置值，该生成器控制数组元素的值的生成算法,增加并行能力，利用多CPU并行来提高性能
void parallelSetAll(xxx[] array, IntToXxxFunction generator)
    
//数组排序，增加并行能力，利用多CPU并行来提高性能
void parallelSort(xxx[] a)
    
//对fromIndex到toIndex索引的元素排序，增加并行能力，利用多CPU并行来提高性能
void parallelSort(xxx[] a, int fromIndex, int toIndex)
    
//将该数组的所有元素转换成对应的Spliterator对象
Spliterator.OfXxx spliterator(xxx[] array)
    
//将该数组startInclusive到endExclusive索引的元素转换成对应的Spliterator对象
Spliterator.OfXxx spliterator(xxx[] array, int startInclusive, int endExclusive)
    
//将数组转换成Stream
XxxStream stream(xxx[] array)
    
//将数组startInclusive到endExclusive索引的元素转换成Stream
XxxStream stream(double[] array, int startInclusive, int endExclusive)
```

  使用示范：

```java
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
System.out.println(Arrays.toString(arr2));//[3, -12, -300, -4800, -144000, -2592000]

int[] arr3 = new int[5];
//使用指定的生成器（generator）为所有数组元素设置值
Arrays.parallelSetAll(arr3, new IntUnaryOperator() {
    //operand 当前元素索引
    @Override
    public int applyAsInt(int operand) {
        return operand * 5;
    }
});
System.out.println(Arrays.toString(arr3));//[0, 5, 10, 15, 20]
```



#### 增强的包装类

Java8 支持无符号算术运算，为整型包装类增加了支持无符号运算的方法。

为Integer、Long增加了如下方法：

```java
//将指定int或long型整数转换为无符号整数对应的字符串
 static String toUnsignedString(int/long i)

//将指定int或long型整数转换为指定进制的无符号整数对应的字符串
static String toUnsignedString(int/long i, int radix)

//将指定字符串解析成无符号整数
static xxx parseUnsignedXxx(String s)

//将指定字符串按照指定进制解析成无符号整数
static xxx parseUnsignedXxx(String s,int radix)

//将x、y两个整数转换为无符号整数后比较大小
static int compareUnsigned(xxx x, xxx y)

//将x、y两个整数转换为无符号整数后计算他们相除的商
static long divideUnsigned(long dividend, long divisor)

//将x、y两个整数转换为无符号整数后计算他们相除的余数
static long remainderUnsigned(long dividend, long divisor)
```

#### 接口中的默认方法

接口里可以包含成员变量（只能是静态常量）、方法（只能是抽象实例方法、类方法、默认方法或私有方法）、内部类（包括内部接口、枚举）

私有方法是java9增加的，主要作用就是作为工具方法被默认方法调用

```java
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
```

```java
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
```

```java
@Test
public void ComTest() {
    IOutput output = new OutputImpl();
    System.out.println(output.MAX_CACHE_LINE);
    output.out();
    IOutput.staticTest();
    output.defaultTest();
}
```

#### 改进的匿名内部类

在Java8之前，Java要求被局部内部类、匿名内部类访问的局部变量必须使用final修饰,从Java8开始这个限制被取消了，Java8更加智能：如果局部变量被匿名内部类访问，那么该局部变量相当于自动使用了final修饰，但是必须按照有final修饰的方式来用

示列：

```java
interface A {
    void test();
}
@Test
public void InternalClassTest() {
        //Java8开始，匿名内部类、局部内部类允许范围非final的局部变量
        //默认使用了final修饰,可以不写
        int age = 8;
    	//age = 2; 报错
        A a = new A() {
            @Override
            public void test() {
                System.out.println(age);
            }
        };
        //调用方法
        a.test();
}
```

#### Lambda表达式

#### Lambda表达式入门

支持将代码块作为方法参数，Lambda表达式允许使用更简洁的代码来创建只有一个抽象方法的接口（这种接口被称为函数式接口）的实例。片面的说Lambda表达式的主要作用就是代替匿名内部类的繁琐语法。

lambda 表达式的语法格式如下：

` (parameters) -> expression 或 (parameters) ->{ statements; }`

lambda表达式的组成:

-  形参列表：不需要声明参数类型。形参列表只有一个参数，可以省略形参列表的圆括号 ,没有参数时不能省略
-  箭头（->）：英文中画线和大于符号组成
- 代码块：如果代码块只包含一条语句，可以省略代码块花括号。如果只有一条语句且为return语句，甚至可以省略return关键字，自动返回这条语句。	

示列：

```java
public interface IArrayDeal {

    /** 接口里定义的deal方法用于封装“处理行为”. */
    void deal(int[] target);
}
```

```java
public interface ICalculator {

    /** 接口里定义的deal方法用于封装“处理行为”. */
    int dealNum(int a, int b);
}
```

```java
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
```

```java
public class LambdaTest {

    @Test
    public void InnerClassTest() {
        //匿名内部类的方式
        Caller caller = new Caller();
        int[] array = {3, -4, 6, 4};
        caller.dealArray(array, new IArrayDeal() {
            @Override
            public void deal(int[] target) {
                int sum = 0;
                for (int tmp : target) {
                    sum += tmp;
                }
                System.out.println("数组总和：" + sum);
            }
        });
    }

    @Test
    public void LambdaTest() {
        //Lambda表达式的方式,不需要重新方法名字和返回值类型
        Caller caller = new Caller();
        int[] array = {3, -4, 6, 4};
        caller.dealArray(array, targetArray -> {
            int sum = 0;
            for (int tmp : targetArray) {
                sum += tmp;
            }
            System.out.println("数组总和：" + sum);
        });
    }

    @Test
    public void InnerClassTest1() {
        //匿名内部类的方式
        Caller caller = new Caller();
        int sum = caller.dealNum(2, 3, new ICalculator() {
            @Override
            public int dealNum(int a, int b) {
                return a + b;
            }
        });
        System.out.println(sum);
    }

    @Test
    public void LambdaTest1() {
        //Lambda表达式的方式,不需要重新方法名字和返回值类型
        Caller caller = new Caller();
        int sum = caller.dealNum(2, 3, (a, b)-> a + b);
        System.out.println(sum);
    }
}
```

#### Lambda表达式与函数式接口

Lambda表达式的类型，也被称为“目标类型（target type）”,Lambda表达式的目标类型必须是“函数式接口（function interface）”。函数式接口代表只包含一个抽象方法的接口。函数式接口可以包含多个默认方法、类方法，但只能声明一个抽象方法。

如果采用匿名内部类语法来创建函数式接口的实例，则只需要实现一个抽象方法，在这种情况下即可采用Lambda表达式来创建对象，该表达式创建出来的对象的目标类型就是这个函数式接口。

函数式接口：Runnable、ActionListener等接口

```java
Runnable r = () -> {
    for (int i = 0 ;i < 100 ; i++){
        System.out.println(i);
    }
};
```

保证Lambda表达式的目标类型是一个明确的函数式接口，常见方式：

1. 将Lambda表达式赋值给函数式接口类型的变量。
2. 将Lambda表达式作为函数式接口类型的参数传递给某个方法。
3. 使用函数式接口对Lambda表达式进行强制的类型转换。

#### 方法引用与构造器引用

Lambda表达式的代码块只有一条代码时，可以在代码块中使用方法引用和构造器引用。两个英文冒号（::）

Lambda表达式支持的几种引用方式：

| 种类                   | 示列               | 说明                                                         | 对应的Lambda表达式                          |
| ---------------------- | :----------------- | ------------------------------------------------------------ | ------------------------------------------- |
| 引用类方法             | 类名::类方法       | 函数式接口中被实现方法的全部参数传给该类方法作为参数         | （a, b, ...）->类名.类方法（a,b, ...）      |
| 引用特定对象的实例方法 | 特定对象::实例方法 | 函数式接口中被实现方法的全部参数传给该方法作为参数           | （a,b, ...）->特定对象.实列方法（a,b, ...） |
| 引用某类对象的实例方法 | 类名::实例方法     | 函数式接口中被实现方法的第一个参数作为调用者，后面的参数全部传给该方法作为参数 | （a,b, ...）->a.实列方法（b, ...）          |
| 引用构造器             | 类名::new          | 函数式接口中被实现方法的全部参数传给该构造器作为参数         | （a,b, ...）->new 类名（a,b, ...）          |



###### 1. 引用类方法

```java
@FunctionalInterface
public interface IConverter {
    Integer convert(String from);
}
```

```java
@Test
public void LambdaTest3() {
      //1.引用类方法
    IConverter converter = from -> Integer.valueOf(from);
    Integer val = converter.convert("99");
    System.out.println(val);

    IConverter converter1 = Integer::valueOf;
    Integer val2 = converter1.convert("99");
    System.out.println(val2);
}
```

###### 2. 引用特定对象的实例方法

```java
@Test
public void LambdaTest4() {
    //2.引用特定对象的实例方法
    IConverter converter = from -> "fkit.org".indexOf(from);
    Integer val = converter.convert("it");
    System.out.println(val);

    IConverter converter1 = "fkit.org" :: indexOf;
    Integer val2 = converter1.convert("it");
    System.out.println(val2);
}
```

###### 3. 引用某类对象的实例方法

```java
@FunctionalInterface
public interface ISubStr {
    String test(String a, int b, int c);
}
```

```java
@Test
public void LambdaTest5() {
    //3.引用某类对象的实例方法
    ISubStr subStr = (a, b, c) -> a.substring(b, c);
    String val = subStr.test("Java I Love you", 2, 9);
    System.out.println(val);

    ISubStr subStr2 = String :: substring;
    String val2 = subStr2.test("Java I Love you", 2, 9);
    System.out.println(val2);
}
```

###### 4. 引用构造器

```java
@FunctionalInterface
public interface IHome {

    JFrame win(String title);
}
```

```java
@Test
public void LambdaTest6() {
    //4.引用构造器
    IHome home = (String a) -> new JFrame(a);
    JFrame val = home.win("我的窗口");
    System.out.println(val);

    IHome home2 = JFrame::new;
    JFrame val2 = home2.win("我的窗口");
    System.out.println(val2);
}
```

#### Lambda表达式与匿名内部类的联系和区别

Lambda表达式是匿名内部类的一种简化，因此它可以部分取代匿名内部类的作用。

Lambda表达式与匿名内部类存在如下相同点：

1. 都可以直接访问“effectively final”的局部变量，以及外部类的成员变量（包括实例变量和类变量）
2. Lambda表达式创建的对象与匿名内部类生成的对象一样，都可以直接调用从接口中继承的默认方法。

```java
@FunctionalInterface
public interface IDisPlayable {
    //定义一个抽象方法和默认方法
    void display();
    default int add(int a, int b){
        return a + b;
    }
}
```

```java
private int age = 12;
private static String name = "Lambda表达式与匿名内部类的联系和区别";
@Test
public void LambdaTest7() {
    test();
}
private void test(){
    String book = "我在里面了";
    IDisPlayable dis = () -> {
        System.out.println("book局部变量："+ book);
        System.out.println("外部类的age实例变量："+age);
        System.out.println("外部类的namg类变量为"+name);
    };
    dis.display();
    //调用dis对象从接口中继承的add()方法
    System.out.println(dis.add(2, 3));
}
```

Lambda表达式与匿名内部类存在如下区别：

1. 匿名内部类可以在任意接口创建实例---不管接口包含多少个抽象方法，只要匿名内部类实现所有抽象方法即可；但Lambda表达式只能为函数式接口创建实例。
2. 匿名内部类可以为抽象类甚至普通类创建实例；但Lambda表达式只能为函数式接口创建实例。
3. 匿名内部类实现的抽象方法的方法体允许调用接口中定义的默认方法；但Lambda表达式的代码块不允许调用接口中定义的默认方法。

#### 新增的日期、时间包

新增了一个java.time包，包含如下常用的类

Clock：获取指定时区的当前日期、时间，可取代System类的currentTimeMillis()方法

Duration：持续时间，获取一段时间

Instant：一个具体的时刻，精确到纳秒

LocalDate：不带时区的日期，如：2007-12-03

LocalTime：不带时区的时间，如：10:15:30

LocalDateTime：不带时区的日期、时间，如2007-12-03T10:15:30

MonthDay：月日，如04-02

Year：年。如：2019

YearMonth：年月，如2019-11

ZonedDateTime：一个时区化的日期、时间

ZoneId：一个时区

DayOfWeek：枚举类，定义了周一到周日的枚举值

Month：枚举类，定义了一月到十二月的枚举值

```java

Clock.systemUTC().millis()==System.currentTimeMillis()
```



#### 新增的日期、时间格式器

在java.time.format包下提供了一个DateTimeFormatter格式器类，该类相当于DateFormat 和SimpleDateFormat的合体

获取DateTimeFormatter对象的三种常见方式：

1. 使用静态常量创建DateTimeFormatter格式器。DateTimeFormatter类中包含如ISO_LOCAL_DATE静态常量，本身是DateTimeFormatter实例
2. 使用代表不同风格的枚举值来创建DateTimeFormatter格式器。在FormatStyle枚举类中定义了FUll、LONG、MEDIUM、SHORT四个枚举值，代表不同风格日期时间
3. 根据模式字符串来创建DateTimeFormatter格式器。类似于SimpleDateFormate

###### 使用DateTimeFormatter完成格式化

格式化的两种方式：

1. 调用DateTimeFormatter的format(TemporalAccessor temporal)方法执行格式化，其中LocalDate、LocalDateTime、LocalTime等类都是TemporalAccessor 接口的实现类
2. 调用LocalDate、LocalDateTime、LocalTime等日期、时间对象的format(DateTimeFormatter formatter)方法执行格式化

```java
@Test
public void FormatTest() {
    DateTimeFormatter[] formatters = new DateTimeFormatter[]{
            //使用静态常量创建
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ISO_LOCAL_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            //使用本地化的不同风格来创建
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL,FormatStyle.MEDIUM),
            DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG),
            //根据模式字符串来创建
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    };
    LocalDateTime localDateTime = LocalDateTime.now();
    for (int i = 0; i < formatters.length; i++) {
        System.out.println(localDateTime.format(formatters[i]));
        System.out.println(formatters[i].format(localDateTime));
    }
}
```

###### 使用DateTimeFormatter解析字符串

使用日期、时间对象提供的parse(CharSequence text,DateTimeFormatter formatter)方法进行解析

```java
@Test
public void ParseTest() {
    String str = "2019-11-10 12:12:30";
    //根据模式字符串来创建格式器
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);
}
```

#### 增强的Iterator遍历集合元素

```java
//使用Lambda表达式来遍历集合元素
void forEachRemaining(Consumer action)
```

```java
@Test
public void IteratorTest() {
    Collection books = new HashSet();
    books.add("aa");
    books.add("bb");
    books.add("cc");
    books.add("dd");
    Iterator iterator = books.iterator();
    //使用Lambda表达式来遍历集合元素
    iterator.forEachRemaining(obj -> System.out.println("迭代元素："+obj));
}
```

#### 新增的Predicate操作集合

Java8为Collection集合新增了一个removeIf(Predicate filter)方法，该方法将会批量删除符合filter条件的所有元素。

Predicate 也是函数式接口,可以充分简化集合的运算。

Predicate 接口中包含一个test（）抽象方法，该方法通常用来对参数进行某种判断（test()方法的判断逻辑由Lambda表达式来实现），然后返回一个boolean值。该接口通常用于判断参数是否满足特定条件，经常用于进行筛滤数据

```java
@Test
public void PredicateTest() {
    Collection<String> books =  new HashSet<>();
    books.add("a");
    books.add("bb");
    books.add("ccc");
    books.add("ddd");
    //过滤长度大于2的
    books.removeIf(ele ->ele.length()>2 );
    books.forEach(System.out::println);
}
```

```java
@Test
public void PredicateTest2() {
    Collection<String> books =  new HashSet<>();
    books.add("a");
    books.add("bb");
    books.add("ccc");
    books.add("ddd");
    //统计包含“bb”子串的数量
    System.out.println(calAll(books, ele -> ((String) ele).contains("bb")));
    //统计包含“cc”子串的数量
    System.out.println(calAll(books, ele -> ((String) ele).contains("cc")));
    //统计字符串长度大于2的数量
    System.out.println(calAll(books, ele -> ((String) ele).length() > 2));

}

private int calAll(Collection<String> books, Predicate p){
    int total = 0;
    for (String book : books) {
        //使用Predicate的test()方法判断该对象是否满足Predicate指定的条件
        if(p.test(book)){
            total ++;
        }
    }
    return total;
}
```



#### 新增的Stream操作集合

新增了Stream、IntStream、LongStream、DoubleStream等流式API，这些API代表多个支持串行和并行聚集操作的元素。上面4个接口中，Stream是一个通用的流接口，而IntStream、LongStream、DoubleStream则代表元素类型为int、long、double的流

每个流式API提供了对应的Builder，列如Stream.Builder、IntStream.Builder、LongStream.Builder、DoubleStream.Builder，通过这些Builder来创建对应的流

独立使用Stream的步骤如下：

1. 使用Stream或XxxStream的builder()类方法创建该Stream对应的Builder
2. 重复调用Builder的add()方法向该流中添加多个元素
3. 调用Builder的build()方法获取对应的Stream
4. 调用Stream的聚集方法

```java
@Test
public void StreamTest() {
    IntStream is = IntStream.builder().add(20).add(13).add(-2).add(18).build();
    System.out.println("is所有元素的最大值：" + is.max().getAsInt());
    System.out.println("is所有元素的最小值：" + is.min().getAsInt());
    System.out.println("is所有元素的总和：" + is.sum());
    System.out.println("is所有元素的总数：" + is.count());
    System.out.println("is所有元素的平均值：" + is.average());
    System.out.println("is所有元素的平方是否都大于20：" + is.allMatch(ele -> ele*ele >20));
    System.out.println("is是否包含任何元素的平方大于20：" + is.anyMatch(ele -> ele*ele >20));
    //将is映射成一个新Stream,新Stream的每个元素是原Stream元素的2倍+1
    IntStream newIs = is.map(ele -> ele * 2 + 1);
    //使用方法引用的方式来遍历集合元素
    newIs.forEach(System.out::println);
}
```

Stream提供了大量的方法进行聚集操作，这些方法既可以是"中间的"（intermediate）,也可以是"末端的"（terminal）

- 中间方法：中间操作允许流保持打开状态，并允许直接调用后续方法。
- 末端方法：对流的最终操作。

Stream常用的中间方法：

```java
//过滤Stream中所有不符合predicate的元素
filter(Predicate predicate)

//使用ToXxxFunction对流中元素执行一对一的转换，该方法返回的新流中包含了ToXxxFunction转换生成的所有元素
mapToXxx(ToXxxFunction mapper)

//依次对每个元素执行一些操作，该方法返回的流与原有流包含相同的元素。主要用于调式
peek(Consumer action)

//该方法用于排序流中所有重复的元素（equals()判断）
distinct()

//用于保证流中的元素在后续的访问中处于有序状态
sorted()

//用于保证对该流的后续访问中最大允许访问的元素个数
limit(long maxSize)
```

Stream常用的末端方法：

```java
//遍历流中所有元素，对每个元素执行action
forEach(Consumer action)

//将流中所有元素转换为一个数组
toArray()

//该方法有三个重载的版本，都用于通过某种操作来合并流中的元素
reduce()

//返回流中所有元素的最小值
min()

//返回流中所有元素的最大值
max()

//返回流中所有元素的数量
count()

//判断流中是否至少包含一个元素符合predicate条件
anyMatch(Predicate predicate)

//判断流中是否每个元素都符合predicate条件
allMatch(Predicate predicate)

//判断流中是否所有元素都不符合predicate条件
noneMatch(Predicate predicate)

//返回流中第一个元素
findFirst()

//返回流中任意一个元素
finAny()
```

调用Collection的stream()方法即可返回对应集合的Stream，然后就可以通过Stream提供的方法对所有集合元素进行处理

```java
@Test
public void CollectionStreamTest() {
    Collection books = new HashSet();
    books.add("java");
    books.add("xml");
    books.add("php");
    books.add("sql");
    books.add("json");

    //统计书名包含"xm"子串的图书数量
    System.out.println(books.stream().filter(ele -> ((String) ele).contains("xm")).count());
    //统计书名包含"son"子串的图书数量
    System.out.println(books.stream().filter(ele -> ((String) ele).contains("son")).count());
    //统计书名字符串长度大于10的图书数量
    System.out.println(books.stream().filter(ele -> ((String) ele).length() > 10).count());
    //生成一个对应书名的字符串长度的集合
    books.stream().mapToInt(ele -> ((String)ele).length()).forEach(System.out::println);
}
```

#### 改进的List接口和ListIterator接口

Java8为List接口添加了如下两个默认方法

```java
//根据operator指定的计算规则重新设置List集合的所有元素
void replaceAll(UnaryOperator operator)

//根据Comparator参数对List集合的元素排序
void sort(Comparator c)
```

```java
@Test
public void listTest() {
    List<String> books = new ArrayList<>();
    books.add("a");
    books.add("bb");
    books.add("ccc");
    books.add("dddd");
    books.sort((o1, o2) -> o2.length() - o1.length());
    System.out.println(books);//[dddd, ccc, bb, a]
    //将每个字符串的长度作为新的集合元素
    books.replaceAll(ele -> String.valueOf(ele.length()));
    System.out.println(books);//[1, 2, 3, 4]
}
```

List除iterator()方法外，额外提供了一个listIterator()方法，该方法返回一个ListIterator对象，ListIterator接口继承了Iterator接口，提供了专门操作List的方法。

Iterator向后迭代，只能删除元素；ListIterator可以向前迭代，还可以添加元素

ListIterator接口在Iterator接口基础上增加了如下方法：

```java
//返回该迭代器关联的集合是否还有上一个元素
boolean hasPrevious()

//返回该迭代器的上一个元素
Object previous

//在指定位置插入一个元素
void add(Object o)
```

```java
@Test
public void ListIteratorTest() {
    List<String> books = new ArrayList<>();
    books.add("a");
    ListIterator<String> listIterator = books.listIterator();
    while (listIterator.hasNext()){
        listIterator.next();
        listIterator.add("b");
    }
    //反向迭代
    while (listIterator.hasPrevious()){
        System.out.println(listIterator.previous());//b a
    }
}
```

#### 为Map新增的方法

Java8为Map增加了如下方法：

```java
//移除指定key和value的元素
boolean remove(Object key, Object value)

//使用remappingFunction根据原key-value对计算一个新value，新value不为null则替换；新value为null则删除原key-value对；新旧value同时为null则不改变任何key-value对，直接返回null
Object compute(Object key, BiFunction remappingFunction)

//如果key对应value为null,使用mappingFunction根据key计算一个新value，新value不为null则替换；如果map中不存在key则添加一组key-value对
Object computeIfAbsent(Object key, Function mappingFunction)

//如果key对应value为null,使用remappingFunction根据旧key、value计算一个新value，新value不为null则替换；如果新value为null则删除旧key-value对
Object computeIfPresent(Object key, BiFunction remappingFunction)

//遍历key-value对
void forEach(BiConsumer action)

//获取key对应value，如果key不存在则返回defaultValue
Object getOrDefault(Object key, V defaultValue)

//如果key对应value为null则用传入的value替换旧value（可能是添加key-value对），如果key对应value不为null，则用remappingFunction根据旧value、传入value计算一个新value并替换
Object merge(Object key, Object value, BiFunction remappingFunction)

//key对应value为null，用传入value替换原来的null值
Object putIfAbsent(Object key, Object value)
    
//key对应的value替换成新value；如果key不存在则不会添加，而是返回null
Object replace(Object key, Object value)
    
//将key-value对的原value替换成新value
boolean replace(K key, V oldValue, V newValue)
    
//使用BiFunction对原key-value对执行计算，并将计算结果作为该key-value对的value值
void replaceAll(BiFunction function)
```

示范：

```java
@Test
public void MapTest() {
    Map<String,Object> map = new HashMap<>();
    map.put("a", 1);
    map.put("bb", 22);
    map.put("ccc", 333);
    //key不存在则不会添加，而是返回null
    map.replace("dddd", 4444);
    System.out.println(map);//{bb=22, a=1, ccc=333}

    //使用原value与传入参数计算出行value替换原value
    map.merge("bb", 10, (oldVal, param) -> (Integer)oldVal * (Integer)param);
    System.out.println(map);//{bb=220, a=1, ccc=333}

    //map中不存在key则添加一组key-value对
    map.computeIfAbsent("ee", key -> key.length());
    System.out.println(map);//{ee=2, bb=220, a=1, ccc=333}

    //根据旧key、value计算一个新value,新value不为null则替换
    map.computeIfPresent("ee", (key, value) -> (Integer)value * (Integer)value);
    System.out.println(map);//{ee=4, bb=220, a=1, ccc=333}
}
```

#### 改进的类型推断

1. 可通过调用方法的上下文来推断泛型的目标类型
2. 可在方法调用链中，将推断得到的泛型传递到最后一个方法

```java
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
```

```java
@Test
public void InferenceTest() {
    //通过方法赋值的目标参数来推断泛型类型为String
    MyUtil<String> install = MyUtil.get1();
    //无须使用下面语句在调用get1()方法时指定泛型的类型
    //MyUtil<String> install2 = MyUtil.<String>get1();

    //可调用get2()方法所需的参数类型来推断泛型为Integer
    MyUtil.get2(42, MyUtil.get1());
    //无须使用下面语句在调用get2()方法时指定泛型的类型
    //MyUtil.get2(42, MyUtil.<Integer>get1());
}
```

#### 函数式接口与@FunctionalInterface

如果接口中只有一个抽象方法（可以包含多个默认方法或多个static方法），该接口就是函数式接口。@FunctionalInterface就是用来指定某个接口必须是函数式接口。

java.util.function包下预定义了大量函数式接口，典型的包含如下4类接口：

XxxFunciton：这类接口中通常包含一个apply（）抽象方法，该方法对参数进行处理、转换（apply()方法的处理逻辑有Lambda表达式来实现），然后返回一个新的值。该函数式接口通常用于对指定数据进行转换处理。

XxxConsumer：这类接口中通常包含一个accept（）抽象方法，该方法与XxxFunciton接口中的apply（）方法基本相似，也负责对参数进行处理，只是改方法不会返回处理结果。

XxxPredicate：这类接口中通常包含一个test（）抽象方法，该方法通常用来对参数进行某种判断（test()方法的判断逻辑由Lambda表达式来实现），然后返回一个boolean值。该接口通常用于判断参数是否满足特定条件，经常用于进行筛滤数据。

XxxSupplier：这类接口中通常包含一个getAsXxx()抽象方法，该方法不需要输入参数，该方法会按某种逻辑算法（getAsXxx()方法的逻辑算法有Lambda表达式来实现）返回一个数据.





#### 新增的重复注释

Java8以前，同一个程序元素前最多只能使用一个相同类型的注解；如果需要在同一个元素前使用多个相同类型的注解，则必须使用注解“容器”。

如：Struts2 中的@Result注解,@Results作为容器

```java

@Results({@Result(name="failure", locatino="failed.jsp"),
         @Result(name="success", locatino="success.jsp")})
public Action FooActino{...}
```

Java8后，可能简化为如下形式（之所以说可能，是因为重复注解还需要对原来的注解进行改造）：

```java
@Result(name="failure", locatino="failed.jsp")
@Result(name="success", locatino="success.jsp")
public Action FooActino{...}
```

开发重复注解需要使用@Repeatable修饰，重复注解只是一种简化写法，这种简化写法是一种假象：多个重复注解其实会被作为“容器”注解的value成员变量的数组元素

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(FKTags.class)
public @interface FKTag {

    //为注解定义2个成员变量
    String name() default "zhangsan";
    int age();
}
```

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FKTags {

    //定义value成员变量，该成员变量可接受多个@FkTag注解
    FKTag[] value();
}
```

```java
//使用方式一
@FKTag(age=20)
@FKTag(name="wangwu", age=24)
public class FkTagTest{...}

//使用方式二
@FkTags({@FKTag(age=20)
@FKTag(name="wangwu", age=24)}）
public class FkTagTest{...}
```



#### 新增的类型注释

ElementType枚举增加了TYPE_PARAMETER、TYPE_USE两个枚举值，这样就允许定义枚举时使用@Target(ElementType.TYPE_USE)修饰，这种注解被称为类型注解（Type Annotation）,类型注解可用于修饰在任何地方出现的类型，如：

- 创建对象（用new关键字创建）
- 类型转换
- 使用implements实现接口
- 使用throws声明抛出异常

```java
@Target(ElementType.TYPE_USE)
public @interface NotNull {
}
```

```java
/**定义类时使用类型注解*/
@NotNull
public class TypeAnnotationTest implements @NotNull/**implements时使用类型注解*/ Serializable {
    //方法形参中使用类型注解
    @Test
    public void Test(@NotNull String name) throws /**throws时使用类型注解*/ @NotNull Exception {
           Object obj = "ljw.com";
           //强制类型转换时使用类型注解
        String str = (@NotNull String)obj;
        //创建对象时使用类型注解
        Object win = new @NotNull String();
    }
    //泛型中使用类型注解
    public void foo(List<@NotNull String> info){}
}
```

#### 改进的线程池

Java5新增了一个Executors工厂类来产生线程池，该工厂类包含如下几个静态工厂方法来创建线程池

```java
//创建一个具有缓存功能的线程池，系统根据需要创建线程，这些线程将会被缓存在线程池中
static ExecutorService newCachedThreadPool()
    
//创建一个可重用、具有固定线程数的线程池
static ExecutorService newFixedThreadPool(int nThreads)

//创建只有一个线程的线程池，它相当于调用newFixedThreadPool方法时传入参数为1
static ExecutorService newSingleThreadExecutor()

//创建具有指定数线程数的线程池，它可以在指定延迟后执行线程任务，corePoolSize指线程中所保存的线程数，即使线程是空闲的也被保存在线程池内
static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
    
//创建只有一个线程的线程池，它可以在指定延迟后执行线程任务
static ScheduledExecutorService newSingleThreadScheduledExecutor()
```

**Java8新增：**

```java
//创建持有足够的线程的线程池来支持给定的并行级别，该方法还会使用多个队列来减少竞争
static ExecutorService newWorkStealingPool(int parallelism)

//该方法是前一个方法的简化版。如果当前机器有4个CPU，则目标并行级别被设置为4，也就是相当于为前一个方法传入4作为参数
static ExecutorService newWorkStealingPool()
```

**总结**：

前三个方法返回ExecutorService对象，可以执行Runnable对象或Callable对象

中间两个方法返回ScheduledExecutorService，是ExecutorService子类，可以在指定延迟后执行任务

最后两个方法，利用了多CPU并行能力



ExecutorService代表尽快执行线程，有如下三个方法：

```java
//将一个Runnable对象提交给指定的线程池，线程池将在有空闲线程时执行Runnable对象代表的任务。其中Future对象代表Runnable任务的返回值--但run（）方法没有返回值，所以Future对象将在run()方法执行结束后返回null.但可以调用Future的isDone()、isCancelled方法来获取Runnable对象的执行状态
Future<？> submit(Runnable task)
    
//将一个Runnable对象提交给指定的线程池，线程池将在有空闲线程时执行Runnable对象代表的任务。其中result显示指定线程执行结束后的返回值，所以Future对象将在run()方法执行结束后返回result
<T> Future<T> submit(Runnable task, T result)
    
//将一个Callable对象提交给指定的线程池，线程池将在有空闲线程时执行Callable对象代表的任务。其中Future对象代表Callable任务的返回值
<T> Future<T> submit(Callable<T> task)
```

ScheduledExecutorService代表在指定延迟后或周期性地执行线程任务，有如下4个方法：

```java
//指定callable任务将在delay延迟后执行
<V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit)

//指定command任务将在delay延迟后执行
ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit)

//指定command任务将在delay延迟后执行，而且以设定频率重复执行。也就是说，在initialDelay后开始执行，依次在initialDelay+period、initialDelay+2*period。。。处重复执行
ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit)

//创建并执行一个在给定初始延迟后首次启用的定期操作，随后在每一次执行终止和下一次执行开始之间都存在给定的延迟。如果任务在任一次执行时遇到异常，就会取消后续执行；否则，只能通过程序来显示取消或终止任务
ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit)
    
```

线程池执行线程任务步骤：

1. 调用Executors类的静态工厂方法创建一个ExecutorService对象，该对象代表一个线程池
2. 创建Runnable实现类或Callable实现类的实列，作为线程执行任务
3. 调用ExecutorService对象的submit()方法来提交Runnable实列或Callable实列
4. 当不想提交任何任务时，调用ExecutorService对象的shutdown()方法来关闭线程池

```java
@Test
public void ThreadPoolTest() {
    //创建一个ExecutorService对象
    ExecutorService executorService = Executors.newWorkStealingPool();
    //创建Runnable实现类的实列
    Runnable target = () -> {
        for (int i = 0; i < 100; i++){
            System.out.println(Thread.currentThread().getName() + "的i值位：" + i);
        }
    };
    //提交Runnable实列
    executorService.submit(target);
    executorService.submit(target);
    //关闭线程池
    executorService.shutdown();
}
```

#### 增强的ForkJoinPool

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

#### 新增的方法参数反射

Java8在java.lang.reflect包下新增了一个Executable抽象基类，该对象代表可执行的类成员，该类派生了Constructor、Method两个子类

Executable提供方法：

```java

//该方法或构造器是否包含数量可变的形参
boolean isVarArgs()
    
//获取该方法或构造器的修饰符
abstract int getModifiers()

//获取该方法或构造器的形参个数
int getParameterCount() 

//获取该方法或构造器的所有形参
Parameter[] getParameters()
```

Parameter也是Java8新增的API,每个Parameter对象代表方法或者构造器的一个参数

Parameter提供方法：

```java
//获取修饰该形参的修饰符
int getModifiers()
    
String getName()
//获取形参名
    
Type getParameterizedType() 
//获取带泛型的形参类型
    
Class<?> getType() 
//获取形参类型
    
boolean isNamePresent()
//该方法返回该类的class文件中是否包含了方法的形参名信息
    
boolean isVarArgs()
//该方法用于判断该参数是否为个数可变的形参
    
```

使用javac命令编译java源文件时，默认生产的class文件并不包含方法的形参名信息，因此调用isNamePresent（）方法返回false，调用getName()方法也不能得到该参数的形参名。

如果希望javac命令编译java源文件时可以保留形参信息，则需要为该命令指定-parameters选项

> 如：javac -parameters -d . myClass.java

