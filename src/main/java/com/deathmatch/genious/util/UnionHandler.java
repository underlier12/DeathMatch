package com.deathmatch.genious.util;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.deathmatch.genious.domain.GameDTO;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.service.GameRoomService;
import com.deathmatch.genious.service.UnionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class UnionHandler extends TextWebSocketHandler{

	private final ObjectMapper objectMapper = new ObjectMapper();
    private final GameRoomService gameRoomService;
    private final UnionService unionService;
    
    // 연결 시작
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
    	System.out.println("======= UnionHandler =======");
    	System.out.println("session ID : " + session.getId());
		System.out.println("Session : " + session);
		
        super.afterConnectionEstablished(session);
        System.out.println("세션 연결");
        System.out.println("============================");
        
    }
    
	// 메세지 전달
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
		
    	GameDTO gameDTO = objectMapper.readValue(message.getPayload(), GameDTO.class);
    	GameRoom gameRoom = gameRoomService.findRoomById(gameDTO.getRoomId());
    	
    	unionService.handleActions(session, gameDTO, gameRoom);
	}
}
