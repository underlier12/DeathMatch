package com.deathmatch.genius.util;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.deathmatch.genius.domain.IndianGameDTO;
import com.deathmatch.genius.domain.IndianGameRoom;
import com.deathmatch.genius.domain.IndianPlayerDTO;
import com.deathmatch.genius.service.GameRoomService;
import com.deathmatch.genius.service.IndianService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class IndianHandler extends TextWebSocketHandler {

	private final GameRoomService gameRoomService;
	private final ObjectMapper objectMapper;
	private final IndianService indianService;
	
	public IndianHandler(GameRoomService gameRoomService,ObjectMapper objectMapper,IndianService indianService) {
		this.gameRoomService = gameRoomService;
		this.objectMapper = objectMapper;
		this.indianService = indianService;
	}

	// 클라이언트가 연결될때 실행됨
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("WebSocketSessionEstablished");
	}

	// 클라이언트가 웹 소켓 서버로 메세지를 전송했을때 실행됨
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		IndianGameDTO indianGameDTO = objectMapper.readValue(message.getPayload(), IndianGameDTO.class);
		IndianGameRoom gameRoom = (IndianGameRoom) gameRoomService.findRoomById(indianGameDTO.getRoomId());
		log.info("indianRoom: " + gameRoom.getRoomId());
		indianService.handleActions(session, indianGameDTO, gameRoom);
        log.info("message submit success");
	}

	// 클라이언트가 연결을 끊을때 사용
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("afterConnectionClosed");
		
		Map<String,Object> map = session.getAttributes();
		IndianPlayerDTO player = (IndianPlayerDTO)map.get("player");
		log.info("player " + player.toString());
		IndianGameRoom gameRoom = (IndianGameRoom) gameRoomService.findRoomById(player.getRoomId());
		indianService.afterConnectionClosed(session, player, gameRoom, status);
		
		if(indianService.isEmptyRoom(gameRoom)) {
			log.info("isEmptyRoom session: " + gameRoom.getSessions());
			gameRoomService.destroyRoom(gameRoom.getRoomId());
		}
		
	}
	
	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.info("handleTransportError occured!");
	}

}
