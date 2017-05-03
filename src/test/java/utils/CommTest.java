package utils;

import java.util.Calendar;

import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.leeo.utils.DateUtils;


public class CommTest {

    public static void main(String[] args) {

    }
    
    @Test
    public void calendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.string2Date("2016-01-31"));
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.add(Calendar.YEAR, 1);
        System.out.println(DateUtils.Date2String(calendar.getTime(),DateUtils.DateFormat.DATE_TIME_LA));
    }
    
    @Test
    public void springPathMatcher(){
        PathMatcher pathMatcher = new AntPathMatcher();
        boolean flag = pathMatcher.match("/websocket/**", "/websocket/123/hcy");
        System.out.println(flag);
    }
}
