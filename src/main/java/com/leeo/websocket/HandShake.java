package com.leeo.websocket;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.leeo.web.entity.User;
/**
 * Socket建立连接（握手）和断开
 * 
 * @author Leeo
 */
public class HandShake implements HandshakeInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(HandShake.class);
    
	@Override
    public boolean beforeHandshake(ServerHttpRequest request,ServerHttpResponse response, WebSocketHandler wsHandler,
		Map<String, Object> attributes) {
	    
		if (request instanceof ServletServerHttpRequest) {
			ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
			HttpSession session = servletRequest.getServletRequest().getSession(false);
			// 标记用户
			User user = (User) session.getAttribute("user");
			String username = user.getUsername();
			logger.info("Websocket:用户【{}】已经建立连接",username);
			if (StringUtils.isNotBlank(username)) {
				attributes.put("username", username);
			} else {
				return false;
			}
		}
		return true;
	}
	
	@Override
    public void afterHandshake(ServerHttpRequest request,ServerHttpResponse response, WebSocketHandler wsHandler,
			Exception exception) {
	}
}