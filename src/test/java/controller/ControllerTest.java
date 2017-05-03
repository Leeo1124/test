package controller;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.leeo.web.controller.LoginController;

/**
 * 直接Controller调用方法
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring-context.xml",
    "classpath:spring-ldap.xml",
    "classpath:spring-task.xml",
    "classpath:spring-hibernate.xml",
    "classpath:spring-cxf.xml",
    "classpath:spring-amq.xml"})
public class ControllerTest {
    
    @Autowired
    private LoginController loginController;

	@Test
	public void testLogin() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		request.setCharacterEncoding("UTF-8");  
		request.setMethod("POST");
		request.addParameter("username", "aa");
		request.addParameter("password", "bb");
		
		assertEquals("login",this.loginController.loginTest(request, response));
	}

}