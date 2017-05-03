package com.leeo.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.leeo.transactional.TransServices;

@Controller
@RequestMapping("/admin")
public class IndexController {

    private static final Logger logger = LoggerFactory.getLogger(IndexController.class);
    
    @Autowired
    private TransServices transServices;
    
    @RequestMapping(value="index")
    public String index(Model model) {

        model.addAttribute("msg", "Hello Spring MVC + Logback");
        
        return "default/index";
    }
    
    /**
     * 只读事物测试
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value="select")
    public String select() {
        
        this.transServices.select();

        logger.info("select transactional test......");
        
        return "index";
    }
}