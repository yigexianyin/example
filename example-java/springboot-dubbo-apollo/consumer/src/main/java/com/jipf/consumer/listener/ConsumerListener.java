package com.jipf.consumer.listener;

import com.jipf.consumer.controller.ConsumerController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ConsumerListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ConsumerController consumerController;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        System.out.println("springboot 启动完毕!");

        System.out.println(consumerController.sayHello("小明"));
    }
}
