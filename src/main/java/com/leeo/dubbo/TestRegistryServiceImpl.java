package com.leeo.dubbo;

import org.springframework.stereotype.Service;

@Service("testRegistryService")
public class TestRegistryServiceImpl implements TestRegistryService {
    
    @Override
    public String hello(String name) {
        return "hello," + name;
    }
    
}