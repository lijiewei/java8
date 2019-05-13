package 新增的类型注释;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/14 0:18
 */
@Target(ElementType.TYPE_USE)
public @interface NotNull {
}
