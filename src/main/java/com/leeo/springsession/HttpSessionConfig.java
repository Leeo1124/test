//package com.leeo.springsession;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
//import org.springframework.session.web.http.HeaderHttpSessionStrategy;
//import org.springframework.session.web.http.HttpSessionStrategy;
//
//@EnableRedisHttpSession  
//public class HttpSessionConfig {  
//    
//    @Bean  
//    public JedisConnectionFactory connectionFactory() {  
//        JedisConnectionFactory connection = new JedisConnectionFactory();  
//        connection.setPort(6379);  
//        connection.setHostName("192.168.34.129");
//        return connection;  
//    }  
//    
//    @Bean
//    public HttpSessionStrategy httpSessionStrategy() {
//            return new HeaderHttpSessionStrategy(); 
//    }
//}  