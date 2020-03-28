package com.deathmatch.genious.util;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.deathmatch.genious.domain.UnionGameDTO;
import com.deathmatch.genious.domain.UnionPlayerDTO;
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

	private final GameRoomService gameRoomService;
	private final ObjectMapper objectMapper;
    private final UnionService unionService;
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("afterConnectionEstablished");
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
		
		Map<String, Object> map = session.getAttributes();
		UnionPlayerDTO player = (UnionPlayerDTO) map.get("player");
		GameRoom gameRoom = gameRoomService.findRoomById(player.getRoomId());
		unionService.afterConnectionClosed(session, player, gameRoom, status);
	}
	
	@Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.info("handleTransportError occured!");
	}
	
}
