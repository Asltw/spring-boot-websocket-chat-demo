package com.example.websocketdemo.model;

import lombok.Data;

/**
 * Created by rajeevkumarsingh on 24/07/17.
 */
@Data
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
