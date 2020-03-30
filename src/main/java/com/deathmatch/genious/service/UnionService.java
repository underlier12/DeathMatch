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
import com.deathmatch.genious.domain.UnionPlayerDTO;
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
			readyAction(session, gameDTO, gameRoom);
			break;
			
		case UNI:
			uniAction(session, gameDTO, gameRoom);
			break;
			
		case ON:
			onAction(session, gameDTO, gameRoom);
			break;
			
		case TIMEUP:
			timeupAction(session, gameDTO, gameRoom);
			break;
			
		default:
			log.info("default action");
			break;
		}
		send(gameRoom);
	}

	private void joinAction(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {		
		loadGame(session, gameRoom);
		queue.offer(unionSettingService.join(session, gameDTO, gameRoom));
		unionSettingService.register(session, gameDTO, gameRoom);
	}

	private void readyAction(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionSettingService.ready(session, gameDTO, gameRoom));
		
		if(unionSettingService.readyCheck(gameRoom)) {
			startGame(gameRoom);
			startRound(gameDTO, gameRoom);
		}
	}

	private void uniAction(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		gameRoom.setLastGameDTO(gameDTO);
		gameRoom.setPass(0);
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
		gameRoom.setLastGameDTO(gameDTO);
		queue.offer(gameDTO);
		
		if(!gameDTO.getMessage().equals("합!")) {
			gameRoom.setPass(0);
			queue.offer(unionDealerService.onResult(session, gameRoom, gameDTO));
		}
		queue.offer(unionDealerService.whoseTurn(gameDTO, gameRoom));
	}
	
	private void timeupAction(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		gameRoom.setLastGameDTO(gameDTO);
		if(unionDealerService.countPass(gameDTO, gameRoom) > 5) {
			queue.offer(unionDealerService.closeRound(gameRoom));
			isGameOver(gameDTO, gameRoom);
			return;
		}
		queue.offer(unionDealerService.whoseTurn(gameDTO, gameRoom));
	}
	
	private void loadGame(WebSocketSession session, GameRoom gameRoom) {
		if(!gameRoom.getPlaying()) {
			switch (gameRoom.getEngaged().size()) {
			case 2:
//				queue.offer(unionSettingService.loadPlayer(gameRoom.getEngaged().get(0), gameRoom));
				queue.offer(unionSettingService.loadPlayer(gameRoom.getEngaged().get(1), gameRoom));
//				break;
			case 1:
				queue.offer(unionSettingService.loadPlayer(gameRoom.getEngaged().get(0), gameRoom));
				break;
			}
			load(session, gameRoom);
		}
	}
	
	private void startGame(GameRoom gameRoom) {
		unionSettingService.startGame(gameRoom);
		queue.offer(unionSettingService.standby(gameRoom));
	}
	
	private void startRound(UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionDealerService.decideRound(gameRoom));
		queue.offer(unionSettingService.setUnionProblem(gameRoom));
		unionSettingService.setUnionAnswer(gameRoom);
//		queue.offer(unionDealerService.whoseTurn(gameDTO, gameRoom));
	}

	private void endRound(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionDealerService.uniResult(session, gameRoom, gameDTO, true));
		queue.offer(unionDealerService.closeRound(gameRoom));
	}
	
	private void isGameOver(UnionGameDTO gameDTO, GameRoom gameRoom) {
		if(gameRoom.getTotalRound() == gameRoom.getRound()) {
			queue.offer(unionDealerService.endGame(gameRoom));
			unionSettingService.resetGame(gameRoom);
		} else {
			startRound(gameDTO, gameRoom);
		}
	}
	
	private void maintainRound(WebSocketSession session, UnionGameDTO gameDTO, GameRoom gameRoom) {
		queue.offer(unionDealerService.uniResult(session, gameRoom, gameDTO, false));
		queue.offer(unionDealerService.whoseTurn(gameDTO, gameRoom));
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
	
	public void load(WebSocketSession session, GameRoom gameRoom) {
		while(!queue.isEmpty()) {
			sendMessage(session, queue.poll());
		}
	}

	public void afterConnectionClosed(WebSocketSession session, UnionPlayerDTO player, 
			GameRoom gameRoom, CloseStatus status) {
		unionSettingService.quitSession(session, gameRoom, status); 
		
		if(unionSettingService.isPlaying(gameRoom)
				&& !unionSettingService.isGuest(player)) {
			
			queue.offer(unionSettingService.quitPlayer(player, gameRoom));
			queue.offer(unionDealerService.endGame(gameRoom));
			unionSettingService.resetGame(gameRoom);
			
		} else if(!unionSettingService.isGuest(player)) {
			queue.offer(unionSettingService.quitPlayer(player, gameRoom));
		}
		send(gameRoom);			
	}
	
	public Boolean isEmptyRoom(GameRoom gameRoom) {
		return gameRoom.getSessions().size() == 0;
	}
}