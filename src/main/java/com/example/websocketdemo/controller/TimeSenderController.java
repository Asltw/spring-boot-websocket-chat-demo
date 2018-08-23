package com.example.websocketdemo.controller;

import com.example.websocketdemo.model.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeSenderController {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private SimpMessagingTemplate brokerMessagingTemplate;

    @Autowired
    public TimeSenderController(SimpMessagingTemplate simpMessagingTemplate) {
        this.brokerMessagingTemplate = simpMessagingTemplate;
    }

//    @Scheduled(fixedRate = 2000)
    public void test() {
        String time = LocalTime.now().format(TIME_FORMAT);
        System.out.println("执行的时间：" + time);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(String.valueOf(System.currentTimeMillis()));
        chatMessage.setType(ChatMessage.MessageType.CHAT);
        chatMessage.setSender("Cat");
        this.brokerMessagingTemplate.convertAndSend("/topic/public", chatMessage);
    }
}
