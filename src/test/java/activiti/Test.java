package activiti;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class Test {

    public static void main(String[] args) throws Exception {
        
        String param = "activityService.addApplicationCheckHisMap(1234)";
        String[] paraArr = param.split("\\.");
        System.out.println(paraArr[0]);
        System.out.println(paraArr[1].replaceAll("(?=\\().*(?<=\\))", ""));
        System.out.println(StringUtils.substringsBetween(paraArr[1], "(", ")")[0]);
        System.out.println(StringUtils.substringBefore(paraArr[1], "("));
        
        ActivityService activityService = new ActivityService();
        Map<String, String> map = new HashMap<>();
        map.put("applyId", "aaaaaa");
        invokeMethod(activityService, "addApplicationCheckHisMap", new Class[]{Map.class}, new Object[]{map});

    }

    public static Object invokeMethod(final Object obj, final String methodName, final Class<?>[] parameterTypes,
            final Object[] args) throws Exception {
        Method method = null;
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
                method = superClass.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
        }
        if (null != method) {
            return method.invoke(obj, args);
        }
        
        return null;
    }
    
    public static String getContent(String expression){  
        //正则表达式//(([^)]*)//)  
        ////(，表示我要抽取的第一个左括号  
        //([^)]*)，其中[^)]*表示从左括号开始算起，直至遇到右括号之前的所有字符，然后还加上了分组符号()，是为了把括号里的字符定义为一个组，方便后面的抽取  
        String regStr = "(?=\\().*(?=\\))";  
        Pattern pattern = Pattern.compile(regStr);  
        Matcher matcher = pattern.matcher(expression);  
        while(matcher.find()){  
            //此时拿到的group1，就是在上面定好的组，对应了括号里面的内容  
            return matcher.group(1);  
        }  
        return null;  
    }  
}

class ActivityService {
    public void addApplicationCheckHisMap(Map<String, String> param) {
        System.out.println(param);
    }
}
