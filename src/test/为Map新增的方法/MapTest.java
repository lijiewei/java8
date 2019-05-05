package 为Map新增的方法;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/5 23:30
 */
public class MapTest {

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
}
