package 新增的Stream操作集合;

import org.junit.Test;
import sun.security.util.Length;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.IntStream;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/5 0:13
 */
public class StreamTest {

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
}
