package utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class Jdk8Test {
    
    public static void main(String[] args) {
        String str = "abc";
        System.out.println(str+"1");
        System.out.println(str+"2");
    }

    @Test
    public void hash(){
        ClassTest a = new ClassTest();
        a.setId(2);
//        a.setName("aa");
        System.out.println(a);
    }
    
    @Test
    public void stream(){
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Stream<Integer> stream = numbers.stream();
        stream.filter((x) -> {
            return x % 2 == 0;
        }).map((x) -> {
            return x * x;
        }).forEach(System.out::println);
    }
    
    @Test
    public void stream2(){
        Stream<Long> fibonacci = Stream.generate(new FibonacciSupplier());
//        fibonacci.limit(10).forEach(System.out::println);
        
        // 取得数列的第20~30
        List<Long> list = fibonacci.skip(20).limit(10).collect(Collectors.toList());
        System.out.println(list);
        
        Stream<Double> piStream = Stream.generate(new PiSupplier());
        piStream.skip(100).limit(10)
                .forEach(System.out::println);
    }
    
    @Test
    public void localDate(){
       //Create local date 
       LocalDate localDate = LocalDate.now();  
       System.out.println("The local date is :: " + localDate);   
       //Find the length of the month. That is, how many days are there for this month.  
       System.out.println("The number of days available for this month:: " + localDate.lengthOfMonth());   
       //Know the month name  
       System.out.println("What is the month name? :: " + localDate.getMonth().name());   
       //add 2 days to the today's date.  
       System.out.println(localDate.plus(2, ChronoUnit.DAYS));   
       //substract 2 days from today  
       System.out.println(localDate.minus(2, ChronoUnit.DAYS));   
       //Convert the string to date  
       System.out.println(LocalDate.parse("2017-04-07")); 
    }
    
    @Test
    public void localTime(){
      //Get local time  
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime);
       //Get the hour of the day  
       System.out.println("The hour of the day:: " + localTime.getHour());  
       //add 2 hours to the time.  
       System.out.println(localTime.plus(2, ChronoUnit.HOURS));  
       //add 6 minutes to the time.  
       System.out.println(localTime.plusMinutes(6));  
       //substract 2 hours from current time  
       System.out.println(localTime.minus(2, ChronoUnit.HOURS)); 
    }
    
    @Test
    public void localDateTime(){
      //Get LocalDateTime object  
        LocalDateTime localDateTime = LocalDateTime.now();  
        System.out.println(localDateTime);  
        //Find the length of month. That is, how many days are there for this month.  
        System.out.println("The number of days available for this month:: " + localDateTime.getMonth().length(true));  
        //Know the month name  
        System.out.println("What is the month name? :: " + localDateTime.getMonth().name());  
        //add 2 days to today's date.  
        System.out.println(localDateTime.plus(2, ChronoUnit.DAYS));  
        //substract 2 days from today  
        System.out.println(localDateTime.minus(2, ChronoUnit.DAYS)); 
    }
    
    @Test
    public void year(){
        //Get year  
        Year year = Year.now();  
        System.out.println("Year ::" + year);  
        //know the year is leap year or not  
        System.out.println("Is year[" +year+"] leap year?"+ year.isLeap()); 
    }
    
    /**
     * Period是用来计算两个给定的日期之间包含多少天，多少月或者多少年，它是不可变类且线程安全的
     */
    @Test
    public void period(){
        LocalDate localDate = LocalDate.now();  
        Period period = Period.between(localDate, localDate.plus(2, ChronoUnit.DAYS));  
        System.out.println(period.getDays());
    }
    
}

/**
 * 生成斐波那契数列，完全可以用一个无穷流表示（受限Java的long型大小，可以改为BigInteger）
 *
 */
class FibonacciSupplier implements Supplier<Long> {

    long a = 0;
    long b = 1;

    @Override
    public Long get() {
        long x = a + b;
        a = b;
        b = x;
        return a;
    }
}

/**
 * 计算π可以利用π的展开式：
 * π/4 = 1 - 1/3 + 1/5 - 1/7 + 1/9 - ...
 *
 */
class PiSupplier implements Supplier<Double> {

    double sum = 0.0;
    double current = 1.0;
    boolean sign = true;

    @Override
    public Double get() {
        sum += (sign ? 4 : -4) / this.current;
        this.current = this.current + 2.0;
        this.sign = ! this.sign;
        return sum;
    }
}

class ClassTest{
    private int id;
    private String name;
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
    public int hashCode() {
        return Objects.hash(this.id,this.name);
    }
    
    @Override
    public boolean equals(Object obj) {
        return Objects.equals(this, obj);
    }
    
}
