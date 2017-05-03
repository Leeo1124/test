package utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.Test;

public class Lang3UtilTest {
    public static void main(String[] args) {
        MyClass one = new MyClass(35, "Becker");
        MyClass two = new MyClass(35, "Becker");
        MyClass three = new MyClass(35, "Agassi");

        System.out.println("One>>>" + one);
        System.out.println("Two>>>" + two);
        System.out.println("Three>>>" + three);

        System.out.println("one equals two? " + one.equals(two));
        System.out.println("one equals three? " + one.equals(three));

        System.out.println("One HashCode>>> " + one.hashCode());
        System.out.println("Two HashCode>>> " + two.hashCode());
        System.out.println("Three HashCode>>> " + three.hashCode());
    }

    @Test
    public void SystemUtils() {
        System.out.println(SystemUtils.getUserHome());
        System.out.println(SystemUtils.getJavaHome());
        System.out.println(SystemUtils.FILE_ENCODING);
        System.out.println(SystemUtils.FILE_SEPARATOR);
        System.out.println(SystemUtils.IS_JAVA_1_7);
    }

    @Test
    public void EqualsBuilder() {
        System.out.println(EqualsBuilder.reflectionEquals(new MyClass(),
            new MyClass()));
    }

    @Test
    public void HashCodeBuilder() {
        System.out.println(HashCodeBuilder.reflectionHashCode(new MyClass()));
    }

    @Test
    public void ToStringBuilder() {
        MyClass clazz = new MyClass();
        clazz.setId(2);
        clazz.setName("aa");
        System.out.println(clazz);
    }

    @Test
    public void ReflectionToStringBuilder() {
        MyClass clazz = new MyClass();
        clazz.setId(2);
//        clazz.setName("aa");
        System.out.println(ReflectionToStringBuilder.toString(clazz));
    }

    @Test
    public void CompareToBuilder() {
        System.out.println(CompareToBuilder.reflectionCompare(1, 1));
        System.out.println(CompareToBuilder.reflectionCompare(1, 2));
        System.out.println(CompareToBuilder.reflectionCompare(2, 1));
        System.out.println(CompareToBuilder.reflectionCompare("ag", "bc"));
    }

    @Test
    public void DateFormatUtils() {
        System.out.println(DateFormatUtils.format(new Date(),
            "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.ISO_DATE_FORMAT.format(new Date()));
        System.out.println(DateFormatUtils.ISO_TIME_NO_T_FORMAT
            .format(new Date()));
        System.out.println(DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
        System.out.println(DateFormatUtils.format(new Date(), "yyyy"));
        System.out.println(DateFormatUtils.format(new Date(), "MM"));
        System.out.println(DateFormatUtils.format(new Date(), "dd"));
    }

    @Test
    public void DateUtils() throws ParseException {
        System.out.println(DateUtils.parseDate("2016-03-13 12:49:45",
            "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtils.isSameDay(new Date(), new Date()));
        System.out.println(DateUtils.addDays(new Date(), 1));
        System.out.println(DateUtils.getFragmentInDays(new Date(),
            Calendar.MONTH));//表示从当月起已经过去多少天了
        System.out.println(DateUtils.getFragmentInDays(new Date(),
            Calendar.YEAR));//表示从当年起已经过去多少天了
        System.out.println(DateUtils.getFragmentInHours(new Date(),
            Calendar.DATE));//表示从今天起已经过去多少个小时了
        System.out.println(DateUtils.getFragmentInMinutes(new Date(),
            Calendar.HOUR_OF_DAY));//表示从当前小时起已经过去多少分钟了
    }

    /**
     * 计算时间差
     * 
     * @throws ParseException
     */
    @Test
    public void DurationFormatUtils() throws ParseException {
        //返回一个对应日期的字符串形式
        System.out.println(DurationFormatUtils.formatDuration((10 + 20 * 60
            + 13 * 3600 + 4 * 24 * 3600) * 1000, "yyyy-MM-dd HH:mm:ss"));

        String dur = DurationFormatUtils.formatPeriod(
            DateUtils.parseDate("2016-03-13 12:49:45", "yyyy-MM-dd HH:mm:ss")
                .getTime(), new Date().getTime(), "y-M-d H-m-s");
        System.out.println(dur);
    }

    /**
     * 计时器
     */
    @Test
    public void StopWatch() {
        StopWatch stWatch = new StopWatch();
        stWatch.start();
        for (int i = 0; i < 10000; i++) {
            System.out.println(i);
        }
        stWatch.stop();
        System.out.println("花费时间 >>" + stWatch.getTime() + "，毫秒");
        System.out.println(DurationFormatUtils.formatDurationHMS(stWatch.getTime()));
        System.out.println(DurationFormatUtils.formatDuration(stWatch.getTime(), "m分s秒S毫秒"));
    }

    @Test
    public void ClassUtilsBySpring() {
        System.out.println(org.springframework.util.ClassUtils
            .classPackageAsResourcePath(Lang3UtilTest.class));
        System.out.println(org.springframework.util.ClassUtils
            .getPackageName(Lang3UtilTest.class));
//        System.out.println(org.springframework.util.ClassUtils.getMethod(arg0, arg1, arg2));
    }

    @Test
    public void ClassUtils() {
        System.out.println(ClassUtils.getPackageName(Lang3UtilTest.class));
    }

    @Test
    public void NumberUtils() {
        System.out.println(NumberUtils.isNumber("12.12"));
        System.out.println(NumberUtils.createBigDecimal("12.8"));
    }

    @Test
    public void RandomStringUtils() {
        System.out.println(RandomStringUtils.randomAlphabetic(5));
        System.out.println(RandomStringUtils.randomAlphanumeric(5));
        System.out.println(RandomStringUtils.randomNumeric(5));
    }
    
    @Test
    public void stringSplit(){
        String str = ",,aa,asda, ,";
        System.out.println(Arrays.toString(str.split(",")));
        System.out.println(Arrays.toString(StringUtils.split(str, ",")));
    }
    
    @Test
    public void stringJoin(){
        List<MyClass> list = new ArrayList<>();
        MyClass m1 = new MyClass();
        m1.setId(1);
        m1.setName("a");
        MyClass m2 = new MyClass();
        m2.setId(2);
        m2.setName("b");
        list.add(m1);
        list.add(m2);
        
        List<String> s = new ArrayList<>();
//        s.add("a");
//        s.add("b");
        String str = "'"+StringUtils.join(s, "','").concat("'");
        System.out.println("----: "+str);
        if(!"''".equals(str)){
            System.out.println("------");
        }
        
        System.out.println(StringUtils.removeEndIgnoreCase("yj,aa", ","));
    }
    
}

class MyClass {
    private int id;
    private String name;

    MyClass() {

    }

    MyClass(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.SHORT_PREFIX_STYLE);

//        //不打印id信息使用方法
//        return (new ReflectionToStringBuilder(this) {
//            @Override
//            protected boolean accept(Field f) {
//                return super.accept(f) && !f.getName().equals("id");
//            }
//        }).toString();
    }
}
