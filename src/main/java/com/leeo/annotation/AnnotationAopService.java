package com.leeo.annotation;

import org.springframework.stereotype.Service;

@Service
public class AnnotationAopService {

    @MonitorLogger
    public void save(){
        System.out.println("----: AnnotationAopService.save");
    }
}
