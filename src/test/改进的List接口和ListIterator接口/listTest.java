package 改进的List接口和ListIterator接口;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/5 22:34
 */
public class listTest {

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

}
