package 新增的日期和时间格式器;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/4 23:08
 */
public class FormatTest {

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

    @Test
    public void ParseTest() {
        String str = "2019-11-10 12:12:30";
        //根据模式字符串来创建格式器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(str, formatter);
    }
}
