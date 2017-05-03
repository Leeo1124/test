package cache;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.leeo.web.entity.User;
import com.leeo.web.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml", "classpath:spring-amq.xml",
    "classpath:spring-data-jpa.xml","classpath:spring-redis.xml" })
public class RedisCacheTest {
    
    @Autowired
    private UserService userService;
    
    @Test
    public void cacheable(){
        User user = this.userService.findByUsername("root");
        System.out.println("-------: " + user);
    }
    
    //内部调用（this），所以没有走 proxy，导致 spring cache 失效(非 public方法类似)
    //,如果想实现基于注释的缓存，必须采用基于 AspectJ的 AOP机制(？？？不知如何实现)
    @Test
    public void cacheable2(){
        User user = this.userService.findByUsername2("root");
        System.out.println("-------: " + user);
    }
    
    @Test
    public void cacheable3(){
        List<User> users = this.userService.findByNativeSql("root");
        System.out.println("-------: " + users);
    }
    
    @Test
    public void cachePut(){
        User user = this.userService.findByUsername("root");
        user.setEmail("root@163.com");
        this.userService.updateUser(user);
        user.setEmail("123@163.com");
        this.userService.updateUser(user);
        user = this.userService.findByUsername("root");
        System.out.println("-------: " + user.getEmail());
    }
    
    @Test
    public void cacheEvict(){
        this.userService.deleteByUserName("root");
    }

}
