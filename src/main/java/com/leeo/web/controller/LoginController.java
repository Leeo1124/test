package com.leeo.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    /**
     * 登录
     * 
     * @param username
     * @param password
     * @param request
     * @return
     */
    @RequestMapping(value = "login")
    public String login(String username, String password, HttpServletRequest request) {
        logger.info("来自IP[{}]的访问", request.getRemoteHost() );
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            return "default/login";
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            if (!subject.isAuthenticated()){//使用shiro来验证  
                token.setRememberMe(true);  
                subject.login(token);
            }
            logger.info("用户【{}】登录成功.", username);
            
            return "default/index";
        } catch ( UnknownAccountException uae ) {   
            //用户名未知...  
            logger.info("用户不存在");  
        } catch ( IncorrectCredentialsException ice ) {  
            //凭据不正确，例如密码不正确 ...  
            logger.info("密码不正确");  
        } catch ( LockedAccountException lae ) {   
            //用户被锁定，例如管理员把某个用户禁用...  
            logger.info("用户被禁用");  
        } catch ( ExcessiveAttemptsException eae ) {  
            //尝试认证次数多余系统指定次数 ...  
            logger.info("请求次数过多，用户被锁定");  
        } catch ( AuthenticationException ae ) {  
            //其他未指定异常  
            logger.info("未知错误，无法完成登录");  
        } catch (Exception e) {
            logger.error("用户【"+username+"】登录验证失败.", e);
        }
        
        return "default/login";
    }

    /**
     * 登出
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "logout")
    public String logout() {
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute("user");
//        if (null != user) {
//            logger.info("用户【" + user.getUsername() + "】退出系统.");
//            session.removeAttribute("user");
//        }
        
        Subject subject = SecurityUtils.getSubject();    
        subject.logout(); 
        logger.info("用户【{}】退出系统.", subject.getPrincipal());

        return "default/login";
    }
    
    /**
     * Controller测试
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "loginTest", method = RequestMethod.POST)
    public String loginTest(HttpServletRequest request,
            HttpServletResponse response) {
        String account = request.getParameter("username");
        String password = request.getParameter("password");
        logger.info("----username:" + account);
        logger.info("----password:" + password);
        if (account.equals("admin") && password.equals("1")) {
            return "index";
        } else {
            return "login";
        }
    }
}