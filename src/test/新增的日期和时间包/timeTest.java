package 新增的日期和时间包;

import org.junit.Test;

import java.time.*;

/**
 * @Description:
 * @Author: Administrator
 * @CreateDate: 2019/5/4 22:40
 */
public class timeTest {
    @Test
    public void Test() {

        Clock clock = Clock.systemUTC();
        //获取当前时刻
        System.out.println(clock.instant());
        System.out.println(clock.millis());
        System.out.println(System.currentTimeMillis());

        Duration duration = Duration.ofSeconds(6000);
        System.out.println("天："+duration.toDays());
        System.out.println("小时："+duration.toHours());
        System.out.println("分："+duration.toMinutes());

        //clock上加6000秒
        Clock clock1 = Clock.offset(clock, duration);

        Instant instant = Instant.now();

        //加6000秒
        Instant instant2 = instant.plusSeconds(6000);

        Instant instant3 = Instant.parse("2014-02-23T10:12:35.342z");

        //加5小时4分钟
        Instant instant4 = instant3.plus(Duration.ofHours(5).plusMinutes(4));

        //获取instant4的5天前的时刻
        Instant instant5 = instant4.minus(Duration.ofDays(5));

        LocalDate localDate = LocalDate.now();

        //获取2014年的第146天
        LocalDate localDate1 = LocalDate.ofYearDay(2014, 146);

        //设置2014年5月21日
        LocalDate localDate2 = LocalDate.of(201, Month.MAY, 21);

        LocalTime localTime = LocalTime.now();

        //设置为22点33分
        LocalTime localTime1 = LocalTime.of(22, 33);

        //一天中的第5503秒
        LocalTime localTime2 = LocalTime.ofSecondOfDay(5503);

        LocalDateTime localDateTime = LocalDateTime.now();

        //当前时间加上25小时3分钟
        LocalDateTime localDateTime1 = localDateTime.plusHours(25).plusMinutes(3);


        Year year = Year.now();

        //年份加5年
        Year year1 = year.plusYears(5);

        //设置月份为10
        YearMonth yearMonth = year.atMonth(10);

        //加5年，减3个月
        YearMonth yearMonth1 = yearMonth.plusYears(5).minusMonths(3);

        MonthDay monthDay = MonthDay.now();

        //设置为5月23日
        MonthDay monthDay1 = monthDay.with(Month.MAY).withDayOfMonth(23);
    }
}
