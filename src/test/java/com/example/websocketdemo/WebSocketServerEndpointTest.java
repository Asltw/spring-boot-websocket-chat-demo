package com.example.websocketdemo;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class WebSocketServerEndpointTest {

    @Test
    public void test() throws URISyntaxException {
        WebSocketClient client = new WebSocketClient(new URI("ws://localhost:8888/webSocket"), new Draft_6455()){

            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("打开连接");
            }

            @Override
            public void onMessage(String message) {
                System.out.println("收到消息");
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("连接关闭");
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
            }
        };
        client.connect();

        System.out.println(client.getDraft());
        while(!client.getReadyState().equals(WebSocket.READYSTATE.OPEN)){
            System.out.println("还没有打开");
        }
        System.out.println("打开了");
        try {
            client.send("hello world".getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.send("hello world");
//        client.close();
    }
}
