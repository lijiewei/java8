package 增强的Iterator遍历集合元素;

import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/5 21:46
 */
public class IteratorTest {

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
}
