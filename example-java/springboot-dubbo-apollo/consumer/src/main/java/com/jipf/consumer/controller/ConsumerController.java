package com.jipf.consumer.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jipf.api.DemoService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Reference(version = "1.0.0")
    private DemoService demoService;

    public String sayHello(String message) {

        return demoService.sayHello(message);
    }
}
