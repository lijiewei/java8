package 新增的重复注释;

import java.lang.annotation.*;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/13 23:58
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(FKTags.class)
public @interface FKTag {

    //为注解定义2个成员变量
    String name() default "zhangsan";
    int age();
}
