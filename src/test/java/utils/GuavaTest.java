package utils;

import org.junit.Test;

import com.google.common.base.Splitter;

public class GuavaTest {

    
    @Test
    public void Splitter(){
//        String str = "foo,bar,,   qux";
//        Iterable<String> result = Splitter.on(',')
//                .trimResults().omitEmptyStrings().split(str);
//        result.forEach(e -> System.out.println(e));
        
        String str2 = "foo,bar,,   qux\r\nasd ss";
        Iterable<String> result2 = Splitter.onPattern("\\s|,")
                .trimResults().omitEmptyStrings().split(str2);
        result2.forEach(e -> System.out.println(e));
        System.out.println(result2);
    }
    
}
