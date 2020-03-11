package com.deathmatch.genious.util;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.deathmatch.genious.domain.UnionGameDTO;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.service.GameRoomService;
import com.deathmatch.genious.service.UnionService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Component
public class UnionHandler extends TextWebSocketHandler{

	private final ObjectMapper objectMapper;
    private final GameRoomService gameRoomService;
    private final UnionService unionService;
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
    	log.info("session ID : " + session.getId());
    	log.info("Session : " + session + "\n");
    	
    	Map<String, Object> map = session.getAttributes();
    	String userEmail = (String) map.get("userEmail");
    	
    	log.info("userEmail : " + userEmail);
    }
    
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
		
		log.info("message.getPayload() : " + message.getPayload() + "\n");
		
    	UnionGameDTO gameDTO = objectMapper.readValue(message.getPayload(), UnionGameDTO.class);
    	GameRoom gameRoom = gameRoomService.findRoomById(gameDTO.getRoomId());
    	unionService.handleActions(session, gameDTO, gameRoom);
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
		log.info("afterConnectionClosed");
		unionService.afterConnectionClosed(session, status);
	}
	
	@Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.info("handleTransportError occured!");
	}
	
}
