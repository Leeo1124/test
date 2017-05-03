package redis;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import redis.clients.jedis.Jedis;

import com.leeo.redis.RedisClient;
import com.leeo.web.entity.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml", "classpath:spring-amq.xml",
    "classpath:spring-data-jpa.xml","classpath:spring-redis.xml" })
public class RedisTest {
    
    @Autowired
    private RedisTemplate redisTemplate;

    public static void main(String[] args) {
        new RedisClient().show();
    }
    
    @Test
    public void getKes(){
        Jedis jedis = new Jedis("192.168.34.129");
        System.out.println("Connection to server sucessfully");
        Set<String> set = jedis.keys("*");
        for (String str : set) {
            System.out.println("Set of stored keys: " + str);
        }
    }
    
    @Test
    public void keys(){
        Set<String> keys = this.redisTemplate.keys("class*");
        if(CollectionUtils.isNotEmpty(keys)){
            for(String key : keys){
                System.out.println("----: "+key);
            }
        }
    }
    
    public void put() {  
//        this.redisTemplate.opsForHash().put("userCache", user.getKey(), user);  
    }  
  
    public void delete() {  
//        this.redisTemplate.opsForHash().delete("userCache", key.getKey());  
    }  
  
    @Test
    public void get() {  
        User user = (User) this.redisTemplate.opsForHash().get("userCache", "class com.leeo.web.service.UserService.root");  
        System.out.println("----: "+user);
    }
    
    @Test
    public void getString() {  
        String str = (String) this.redisTemplate.opsForHash().get("oasis-message-wangjianguo", "");  
        System.out.println("----: "+str);
    }  
}
