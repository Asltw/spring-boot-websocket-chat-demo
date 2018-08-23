package com.example.websocketdemo.controller;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class MyTest {

    @PostConstruct
    public void init() {
        System.out.println("初始化....");
    }

    @PreDestroy
    public void destroy() {
        System.out.println("销毁....");
    }
}
