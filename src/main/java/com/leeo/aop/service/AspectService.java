package com.leeo.aop.service;

import org.springframework.stereotype.Service;

@Service
public class AspectService {

    public void save(String obj){
        System.out.println("----: AopTestService.save("+obj+")");
    }
    
    public String delete(String obj){
        System.out.println("----: AopTestService.delete("+obj+")");
        
        return "ok";
    }
    
    public void update(String obj) {
        System.out.println("----: AopTestService.update("+obj+")");
        throw new RuntimeException("运行时异常.");
    }
}
