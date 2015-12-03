package com.leeo.quartz;

import org.springframework.stereotype.Component;

@Component
public class SendTask {

    private void execute() {
        System.out.println(System.currentTimeMillis() + "-----");
    }
}
