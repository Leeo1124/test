package com.leeo.websocket;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.leeo.websocket.entity.Message;

/**
 * Socket处理器
 * 
 * @author Leeo
 */
@Component
public class MyWebSocketHandler implements WebSocketHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(MyWebSocketHandler.class);
    
	public static final Map<String, WebSocketSession> userSocketSessionMap = new ConcurrentHashMap<>();
	
	/**
	 * 建立连接
	 */
	@Override
    public void afterConnectionEstablished(WebSocketSession session) {
	    String username = (String) session.getAttributes().get("username");
		if (userSocketSessionMap.get(username) == null) {
			userSocketSessionMap.put(username, session);
		}
		logger.info("userSocketSessionMap:"+userSocketSessionMap);
	}
	
	/**
	 * 消息处理，在客户端通过Websocket API发送的消息会经过这里，然后进行相应的处理
	 */
	@Override
    public void handleMessage(WebSocketSession session,WebSocketMessage<?> message) throws Exception {
		if (message.getPayloadLength() == 0)
			return;
		Message msg = new Gson().fromJson(message.getPayload().toString(),Message.class);
		msg.setDate(new Date());
		sendMessageToUser(msg.getTo(), new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));
	}

	/**
	 * 消息传输错误处理
	 */
	@Override
    public void handleTransportError(WebSocketSession session,Throwable exception) throws Exception {
        logger.error(exception.getMessage(), exception);
		if (session.isOpen()) {
			session.close();
		}
		
		Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// 移除Socket会话
		while (it.hasNext()) {
			Entry<String, WebSocketSession> entry = it.next();
			if (entry.getValue().getId().equals(session.getId())) {
				userSocketSessionMap.remove(entry.getKey());
				logger.error("websocket connection exception: " + entry.getKey());
				logger.info("Socket会话已经移除:用户ID【{}】",entry.getKey());
				break;
			}
		}
	}

	/**
	 * 连接关闭
	 */
	@Override
    public void afterConnectionClosed(WebSocketSession session,CloseStatus closeStatus) {
	    logger.info("Websocket:【{}】已经关闭",session.getId());
		Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// 移除Socket会话
		while (it.hasNext()) {
			Entry<String, WebSocketSession> entry = it.next();
			if (entry.getValue().getId().equals(session.getId())) {
				userSocketSessionMap.remove(entry.getKey());
				logger.info("Socket会话已经移除:用户ID【{}】",entry.getKey());
				break;
			}
		}
	}

	/**
	 * 是否分段发送消息
	 */
	@Override
    public boolean supportsPartialMessages() {
		return false;
	}

	/**
	 * 给所有在线用户发送消息
	 * 
	 * @param message
	 */
	public void broadcast(final TextMessage message) {
		Iterator<Entry<String, WebSocketSession>> it = userSocketSessionMap.entrySet().iterator();
		// 多线程群发
		while (it.hasNext()) {
			final Entry<String, WebSocketSession> entry = it.next();
			if (entry.getValue().isOpen()) {
				// entry.getValue().sendMessage(message);
				new Thread(new Runnable() {
					@Override
                    public void run() {
						try {
							if (entry.getValue().isOpen()) {
								entry.getValue().sendMessage(message);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();
			}
		}
	}

	/**
	 * 给某个用户发送消息
	 * 
	 * @param userName
	 * @param message
	 * @throws IOException
	 */
	public void sendMessageToUser(String userName, TextMessage message)throws IOException {
		@SuppressWarnings("resource")
        WebSocketSession session = userSocketSessionMap.get(userName);
		if (null != session && session.isOpen()) {
			session.sendMessage(message);
		}
	}
}