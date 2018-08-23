package com.example.websocketdemo;

import com.example.websocketdemo.model.ChatMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.Nullable;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.SECONDS;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebsocketDemoApplicationTests {



	private String url;

	private static final String ADD_USER = "/app/chat.addUser";
	private static final String SEND_MESSAGE = "/app/chat.sendMessage";
	private static final String SUBSCRIBE_ENDPOINT = "/topic/public";

	@Before
	public void setup() {
		url = "ws://localhost:8888/ws";
	}

	@Test
	public void testAdd() throws InterruptedException, ExecutionException, TimeoutException {
		WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		StompSession stompSession = stompClient.connect(url, new StompSessionHandlerAdapter() {
		}).get(1, SECONDS);
		stompSession.subscribe(SUBSCRIBE_ENDPOINT, new ChatMessageStompFrameHandler());
		ChatMessage chatMessage = new ChatMessage();
		chatMessage.setSender("xxxx");
		chatMessage.setType(ChatMessage.MessageType.JOIN);
		stompSession.send(ADD_USER, chatMessage);
//		new Scanner(System.in).nextLine(); // 不要马上断开连接
	}

	@Test
	public void testAdd2() {
		WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
		stompClient.setMessageConverter(new MappingJackson2MessageConverter());
		stompClient.connect(url, new MySessionHandler());
		new Scanner(System.in).nextLine();
	}



	private List<Transport> createTransportClient() {
		List<Transport> transports = new ArrayList<>(1);
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		return transports;
	}

	private class ChatMessageStompFrameHandler implements StompFrameHandler {

		@Override
		public Type getPayloadType(StompHeaders headers) {
			System.out.println(headers.toString());
			return ChatMessage.class;
		}

		@Override
		public void handleFrame(StompHeaders headers, @Nullable Object o) {
			System.out.println("hahahahha" + o);
			System.out.println("=====" + ((ChatMessage) o).getSender());
		}
	}

	private class MySessionHandler extends StompSessionHandlerAdapter {

		@Override
		public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setSender("xxxx");
			chatMessage.setType(ChatMessage.MessageType.JOIN);
			session.subscribe(SUBSCRIBE_ENDPOINT, this);
			session.send(ADD_USER, chatMessage);
			System.out.println("new session " + session.getSessionId());
			System.out.println("connection " + connectedHeaders);
		}

		@Override
		public void handleException(StompSession session, @Nullable StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
			exception.printStackTrace();
		}

		@Override
		public Type getPayloadType(StompHeaders headers) {
			return ChatMessage.class;
		}

		@Override
		public void handleFrame(StompHeaders headers, @Nullable Object o) {
			System.out.println("receive message：" + o);
		}
	}
}
