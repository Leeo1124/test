//package com.leeo.websocket;
//import javax.annotation.Resource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//import org.springframework.web.socket.config.annotation.EnableWebSocket;
//import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
//import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
///**
// * WebScoket配置处理器
// * 
// * @author Leeo
// */
//@Component
//@EnableWebSocket
//public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer {
//	@Resource
//	private MyWebSocketHandler handler;
//	
//	@Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//		registry.addHandler(this.handler, "/ws").addInterceptors(new HandShake());
//		registry.addHandler(this.handler, "/ws/sockjs").addInterceptors(new HandShake()).withSockJS();
//	}
//}