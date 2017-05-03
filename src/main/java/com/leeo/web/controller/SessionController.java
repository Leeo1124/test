package com.leeo.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/session")
public class SessionController {

    private static final Logger logger = LoggerFactory
        .getLogger(SessionController.class);

    @RequestMapping(value = "write", method = RequestMethod.GET)
    public String writeSession(HttpServletRequest request) {
        String userName = request.getParameter("userName");
        logger.info("---userName: " + userName);
        Map<Object, Object> user = new HashMap<>();
        user.put("userName", userName);
        request.getSession().setAttribute("user", user);

        return "login";
    }

    @RequestMapping(value = "read", method = RequestMethod.GET)
    public String readSession(HttpServletRequest request) {
        Map<Object, Object> user = (Map<Object, Object>) request.getSession()
            .getAttribute("user");
        logger.info("---user: " + user);

        return "login";
    }
}