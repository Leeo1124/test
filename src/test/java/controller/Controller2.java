package controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;  
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;  
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;  
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup; 

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 * 方式二：调用请求路径
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml", "classpath:spring-task.xml",
    "classpath:spring-hibernate.xml", "classpath:spring-cxf.xml",
    "classpath:spring-amq.xml" })
//当然 你可以声明一个事务管理 每个单元测试都进行事务回滚 无论成功与否  
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class Controller2 {
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testLogin() throws Exception {
//        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/loginTest");
//        requestBuilder.param("username", "admin");
//        requestBuilder.param("password", "1");
//        this.mockMvc.perform(requestBuilder)//执行请求
//        .andExpect(status().isOk())//验证状态码
//        .andDo(print());//输出MvcResult到控制台
        
        this.mockMvc.perform((post("/loginTest").param("username", "admin").param("password", "1")))//执行请求
            .andExpect(status().isOk())//验证状态码
            .andDo(print());//输出MvcResult到控制台
        
    }

//    @Test
//    //有些单元测试你不希望回滚
//    @Rollback(false)
//    public void testInsert() throws Exception {
//        this.mockMvc.perform((post("/insertTest"))).andExpect(status().isOk())
//            .andDo(print());
//    }

}