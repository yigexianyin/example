package com.jipf.consumer.controller;

import com.jipf.api.DemoService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Reference
    private DemoService demoService;

    public String sayHello(String message) {

        return demoService.sayHello(message);
    }
}
