package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionGameDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Service
public class UnionService {

	private final ObjectMapper objectMapper;
	private final UnionDealerService unionDealerService;
	private final UnionSettingService unionSettingService;

	Queue<Object> queue = new LinkedList<>();
	
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
		send(gameRoom);
	}


	private void joinAction(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		gameRoom.getSessions().add(session);
		gameDTO.setMessage(gameDTO.getSender() + "님이 입장했습니다.");
		queue.offer(gameDTO);
	}


	private void readyAction(UnionGameDTO gameDTO, GameRoom gameRoom) {
		Map<String, Boolean> readyUser = gameRoom.getReadyUser();

		readyUser.put(gameDTO.getSender(), Boolean.TRUE);
		gameDTO.setMessage(gameDTO.getSender() + "님이 준비하셨습니다.");
		queue.offer(gameDTO);
		
		log.info("readyUser : " + readyUser);
		
		if(unionSettingService.readyCheck(readyUser)) {
			allReady(gameDTO, gameRoom);
		}
	}

	private void uniAction(UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionDealerService.uniCheck(gameRoom, gameDTO));
	}


	private void onAction(UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionDealerService.onCheck(gameRoom, gameDTO));
	}


	private void outAction(UnionGameDTO gameDTO, GameRoom gameRoom) {
		log.info("OUT\n");		
	}
	

	private void allReady(UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionSettingService.standby(gameRoom));
		queue.offer(unionSettingService.setUnionProblem(gameRoom));
				
		unionSettingService.setUnionAnswer(gameRoom);
		
		queue.offer(unionDealerService.decideRound(gameRoom));
		queue.offer(unionSettingService.setPlayers(gameRoom));
	}

	public void send(GameRoom gameRoom) {
		while(!queue.isEmpty()) {
			sendMessageAll(gameRoom.getSessions(), queue.poll());			
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
