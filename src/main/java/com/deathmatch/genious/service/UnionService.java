package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.LinkedList;
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
			readyAction(session, gameDTO, gameRoom);
			break;
			
		case UNI:
			uniAction(session, gameDTO, gameRoom);
			break;
			
		case ON:
			onAction(session, gameDTO, gameRoom);
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

	private void readyAction(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionSettingService.ready(session, gameDTO, gameRoom));
		
		if(unionSettingService.readyCheck(gameRoom)) {
			unionSettingService.startGame(gameRoom);
			queue.offer(unionSettingService.standby(gameRoom));
			startRound(gameDTO, gameRoom);
		}
	}

	private void uniAction(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(gameDTO);
		
		switch (unionDealerService.uniCheck(gameDTO, gameRoom)) {
		case "CORRECT":
			endRound(session, gameDTO, gameRoom);
			isGameOver(gameDTO, gameRoom);
			break;

		case "INCORRECT":
			maintainRound(session, gameDTO, gameRoom);
			break;
		}
		
	}

	private void onAction(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(gameDTO);
		queue.offer(unionDealerService.onResult(session, gameRoom, gameDTO));
	}
	
	private void startRound(UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionDealerService.decideRound(gameRoom));
		queue.offer(unionSettingService.setUnionProblem(gameRoom));
		unionSettingService.setUnionAnswer(gameRoom);
	}

	private void endRound(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionDealerService.uniResult(session, gameRoom, gameDTO, true));
		queue.offer(unionDealerService.closeRound(gameRoom));
	}
	
	private void isGameOver(UnionGameDTO gameDTO, GameRoom gameRoom) {
		
		if(gameRoom.getTotalRound() == gameRoom.getRound()) {
			queue.offer(unionDealerService.endGame(gameRoom, gameDTO));
		} else {
			startRound(gameDTO, gameRoom);
		}
	}
	
	private void maintainRound(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionDealerService.uniResult(session, gameRoom, gameDTO, false));
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
