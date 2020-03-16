package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
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
			log.info("OUT ACTION");
			outAction(session, gameDTO, gameRoom);
			break;

		default:
			break;
		}
		send(gameRoom);
	}

	private void joinAction(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		unionSettingService.welcome(session, gameDTO, gameRoom);
		queue.offer(unionSettingService.join(gameDTO, gameRoom));
	}

	private void readyAction(UnionGameDTO gameDTO, GameRoom gameRoom) {
		Map<String, Boolean> readyUser = gameRoom.getReadyUser();
		readyUser.put(gameDTO.getSender(), Boolean.TRUE);
		
		queue.offer(unionSettingService.ready(gameDTO, gameRoom));
		
		if(unionSettingService.readyCheck(readyUser)) {
			queue.offer(unionSettingService.standby(gameRoom));
			startRound(gameDTO, gameRoom);
		}
	}

	private void uniAction(UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(gameDTO);
		
		switch (unionDealerService.uniCheck(gameDTO, gameRoom)) {
		case "CORRECT":
			endRound(gameDTO, gameRoom);
			isGameOver(gameDTO, gameRoom);
			break;

		case "INCORRECT":
			maintainRound(gameDTO, gameRoom);
			break;
		}
		
	}

	private void onAction(UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(gameDTO);
		queue.offer(unionDealerService.onResult(gameRoom, gameDTO));
	}
	
	private void outAction(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		gameRoom.getSessions().remove(session);
		log.info("session : " + gameRoom.getSessions());
	}
	
	
	private void startRound(UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionDealerService.decideRound(gameRoom));
		queue.offer(unionSettingService.setUnionProblem(gameRoom));
		unionSettingService.setUnionAnswer(gameRoom);
	}

	private void endRound(UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionDealerService.uniResult(gameRoom, gameDTO, true));
		queue.offer(unionDealerService.closeRound(gameRoom));
	}
	
	private void isGameOver(UnionGameDTO gameDTO, GameRoom gameRoom) {
		
		if(gameRoom.getTotalRound() == gameRoom.getRound()) {
			queue.offer(unionDealerService.endGame(gameRoom, gameDTO));
		} else {
			startRound(gameDTO, gameRoom);
		}
	}
	
	private void maintainRound(UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionDealerService.uniResult(gameRoom, gameDTO, false));
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

	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
		unionSettingService.bye(session, status);
	}
	
}
