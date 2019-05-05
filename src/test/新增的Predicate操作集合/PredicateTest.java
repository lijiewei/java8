package 新增的Predicate操作集合;

import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Predicate;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/5 21:58
 */
public class PredicateTest {

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
}
