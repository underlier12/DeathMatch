package com.deathmatch.genius.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.deathmatch.genius.util.IndianHandler;
/**
 * 
 * @author 
 * WebSocket Setting
 */

@Configuration
@EnableWebSocket
public class IndianHandlerConfig implements WebSocketConfigurer {
	
	private final IndianHandler indianHandler;
	
	public IndianHandlerConfig(IndianHandler indianHandler) {
		this.indianHandler = indianHandler;
	}
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(indianHandler, "/ws/indian")
			.setAllowedOrigins("*") // wss 통신을 위해 필요하다
			.withSockJS();
	}
	
}
