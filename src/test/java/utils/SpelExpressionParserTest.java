package utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.leeo.utils.ApplicationContextHolder;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml", "classpath:spring-amq.xml",
    "classpath:spring-data-jpa.xml" })
public class SpelExpressionParserTest {

	public static void main(String[] args) {
		System.out.println("----"+Arrays.toString(args));
	}
	
	public static String execute(String s) {
		System.out.println("----"+s);
		
		return s;
	}
	
	public static String execute2(String... s) {
	    System.out.println("----"+Arrays.toString(s));
        
        return s.toString();
    }

	@Test
	public void getSystemProperties() {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
		ctx.refresh();
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setBeanResolver(new BeanFactoryResolver(ctx));
		Properties result1 = parser.parseExpression("@systemProperties")
				.getValue(context, Properties.class);
		System.out.println(result1);

		Assert.assertEquals(System.getProperties(), result1);
	}
	
	@Test
    public void list() {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        List<Integer> numbers = (List<Integer>) parser.parseExpression("{1,2,3,4}").getValue(context);
        List<String> listOfLists = (List<String>) parser.parseExpression("{{'a','b'},{'x','y'}}").getValue(context);
        
        System.out.println(numbers);
        System.out.println(listOfLists);
    }
	
	@Test
    public void map() {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        Map<String, Object> inventorInfo = (Map<String, Object>) parser.parseExpression("{name:'Nikola',dob:'10-July-1856'}").getValue(context);
        Map<String, Object> mapOfMaps = (Map<String, Object>) parser.parseExpression("{name:{first:'Nikola',last:'Tesla'},dob:{day:10,month:'July',year:1856}}").getValue(context);
        
        System.out.println(inventorInfo);
        System.out.println(mapOfMaps);
    }
	
	@Test
	public void staticMethod() throws NoSuchMethodException, SecurityException {
		
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.registerFunction("execute", SpelExpressionParserTest.class.getDeclaredMethod("execute", new Class[] { String.class }));
		Object result = parser.parseExpression("#execute('aa')").getValue(context);
		
		System.out.println(result);
	}
	
	/**
	 * 有问题
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	@Test
    public void staticMethod2() throws NoSuchMethodException, SecurityException {
        
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        context.registerFunction("main", SpelExpressionParserTest.class.getDeclaredMethod("main", new Class[] { String.class }));
        Object result = parser.parseExpression("#main('a','b')").getValue(context);
        
        System.out.println(result);
    }
	
	@Test
	public void beanMethodByString() {
		ExpressionParser parser = new SpelExpressionParser();
		StandardEvaluationContext context = new StandardEvaluationContext();
		context.setBeanResolver(new BeanFactoryResolver(ApplicationContextHolder.getApplicationContext()));
		Object result = parser.parseExpression("@userService.findByUsername('admin')").getValue(context);
		
		System.out.println(result);
	}

}
