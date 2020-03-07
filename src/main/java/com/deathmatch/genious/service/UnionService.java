package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genious.domain.GameDTO;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionDealerDTO;
import com.deathmatch.genious.domain.UnionProblemDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Service
public class UnionService {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final UnionDealerService unionDealerService;
	private final UnionSettingService unionSettingService;

	public void handleActions(WebSocketSession session, GameDTO gameDTO, GameRoom gameRoom) {
		
		Set<WebSocketSession> sessions = gameRoom.getSessions();
		Map<String, Boolean> readyUser = gameRoom.getReadyUser();
		
		if (gameDTO.getType().equals(GameDTO.MessageType.JOIN)) {
			sessions.add(session);
			gameDTO.setMessage(gameDTO.getSender() + "님이 입장했습니다.");
			sendMessageAll(sessions, gameDTO);
			
		} else if (gameDTO.getType().equals(GameDTO.MessageType.TALK)) {
			sendMessageAll(sessions, gameDTO);
			    		
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.READY)) {
    		readyUser.put(gameDTO.getSender(), Boolean.TRUE);
    		gameDTO.setMessage(gameDTO.getSender() + "님이 준비하셨습니다.");
    		sendMessageAll(sessions, gameDTO);
    		
    		log.info("readyUser : " + readyUser + "\n");
    		
    		if(unionDealerService.readyCheck(readyUser)) {
    			
    			UnionDealerDTO unionProblemDTO = unionDealerService.standby(gameRoom);
    			sendMessageAll(sessions, unionProblemDTO);
    			
    			UnionProblemDTO unionProblemDTO2 = unionSettingService.setUnionProblem(gameRoom);
    			sendMessageAll(sessions, unionProblemDTO2);
    			
    			log.info("gameRoom.getP.keySet() : " + gameRoom.getProblemMap().keySet());
    			
    			unionSettingService.setUnionAnswer(gameRoom);
    			
    			UnionDealerDTO unionDealerDTO = unionSettingService.decideRound(gameRoom);
    			sendMessageAll(sessions, unionDealerDTO);
    			
    			UnionDealerDTO unionDealerDTO2 = unionSettingService.setPlayers(gameRoom);
    			sendMessageAll(sessions, unionDealerDTO2);
    			
    		}
    		
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.SCORE)) {
    		
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.UNI)) {
    		
    		UnionDealerDTO unionDealerDTO = unionDealerService.uniCheck(gameRoom, gameDTO);
    		sendMessageAll(sessions, unionDealerDTO);
    		
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.ON)) {
    		UnionDealerDTO unionDealerDTO = unionDealerService.onCheck(gameRoom, gameDTO);
    		sendMessageAll(sessions, unionDealerDTO);
    		
    	} else {
    		log.info("OUT\n");
    	}
	}

	public <T> void sendMessageAll(Set<WebSocketSession> sessions, T message){
		sessions.parallelStream().forEach(session -> sendMessage(session, message));

	}
	
	public <T> void sendMessage(WebSocketSession session, T message) {
		
		try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
