package 新增的重复注释;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/14 0:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FKTags {

    //定义value成员变量，该成员变量可接受多个@FkTag注解
    FKTag[] value();
}
