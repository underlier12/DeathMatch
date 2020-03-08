package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genious.domain.UnionGameDTO;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionDealerDTO;
import com.deathmatch.genious.domain.UnionSettingDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Service
public class UnionService {

	private final ObjectMapper objectMapper;// = new ObjectMapper();
	private final UnionDealerService unionDealerService;
	private final UnionSettingService unionSettingService;

	public void handleActions(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		
		switch (gameDTO.getType()) {
		case JOIN:
			joinAction(session, gameDTO, gameRoom);
			break;
			
		case READY:
			readyAction(gameDTO, gameRoom);
			break;
			
		case UNI:
			uniAction(gameDTO, gameRoom);
			break;
			
		case ON:
			onAction(gameDTO, gameRoom);
			break;
			
		case OUT:
			outAction(gameDTO, gameRoom);
			break;

		default:
			break;
		}
	}


	private void joinAction(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		gameRoom.getSessions().add(session);
		gameDTO.setMessage(gameDTO.getSender() + "님이 입장했습니다.");
		sendMessageAll(gameRoom.getSessions(), gameDTO);
	}


	private void readyAction(UnionGameDTO gameDTO, GameRoom gameRoom) {
		Map<String, Boolean> readyUser = gameRoom.getReadyUser();

		readyUser.put(gameDTO.getSender(), Boolean.TRUE);
		gameDTO.setMessage(gameDTO.getSender() + "님이 준비하셨습니다.");
		sendMessageAll(gameRoom.getSessions(), gameDTO);
		
		log.info("readyUser : " + readyUser + "\n");
		
		if(unionDealerService.readyCheck(readyUser)) {
			allReady(gameDTO, gameRoom);
		}
	}

	private void uniAction(UnionGameDTO gameDTO, GameRoom gameRoom) {
		UnionDealerDTO unionDealerDTO = unionDealerService.uniCheck(gameRoom, gameDTO);
		sendMessageAll(gameRoom.getSessions(), unionDealerDTO);
	}


	private void onAction(UnionGameDTO gameDTO, GameRoom gameRoom) {
		UnionDealerDTO unionDealerDTO = unionDealerService.onCheck(gameRoom, gameDTO);
		sendMessageAll(gameRoom.getSessions(), unionDealerDTO);
	}


	private void outAction(UnionGameDTO gameDTO, GameRoom gameRoom) {
		log.info("OUT\n");		
	}
	

	private void allReady(UnionGameDTO gameDTO, GameRoom gameRoom) {
		UnionDealerDTO unionProblemDTO = unionDealerService.standby(gameRoom);
		sendMessageAll(gameRoom.getSessions(), unionProblemDTO);
		
		UnionSettingDTO unionProblemDTO2 = unionSettingService.setUnionProblem(gameRoom);
		sendMessageAll(gameRoom.getSessions(), unionProblemDTO2);
		
		log.info("gameRoom.getP.keySet() : " + gameRoom.getProblemMap().keySet());
		
		unionSettingService.setUnionAnswer(gameRoom);
		
		UnionDealerDTO unionDealerDTO = unionSettingService.decideRound(gameRoom);
		sendMessageAll(gameRoom.getSessions(), unionDealerDTO);
		
		UnionDealerDTO unionDealerDTO2 = unionSettingService.setPlayers(gameRoom);
		sendMessageAll(gameRoom.getSessions(), unionDealerDTO2);
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
