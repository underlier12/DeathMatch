package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genious.domain.GameDTO;
import com.deathmatch.genious.domain.GameRoom;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UnionService {

	private ObjectMapper objectMapper = new ObjectMapper();

	public void handleActions(WebSocketSession session, GameDTO gameDTO, GameRoom gameRoom) {
		
		Set<WebSocketSession> sessions = gameRoom.getSessions();
		
		if (gameDTO.getType().equals(GameDTO.MessageType.JOIN)) {
			// JOIN
			sessions.add(session);
			gameDTO.setMessage(gameDTO.getSender() + "님이 입장했습니다.");
			sendMessageAll(sessions, gameDTO);
			
		} else if (gameDTO.getType().equals(GameDTO.MessageType.TALK)) {
    		// TALK
			sendMessageAll(sessions, gameDTO);
    		
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.READY)) {
    		// READY
    		gameDTO.setMessage(gameDTO.getSender() + "님이 준비하셨습니다.");
    		unionService.readyCheck(sessions, gameDTO);
    		
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.SCORE)) {
    		// SCORE
    		countScore(gameDTO, unionService);
    		
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.UNI)) {
    		// UNI
    		uniCheck(gameDTO, unionService);
//    		unionService.uniCheck(session, gameDTO);
    		
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.ON)) {
    		// ON
    		gameDTO.setMessage(gameDTO.getSender() + "님이 '결'을 외치셨습니다.");
    		unionService.onCheck(session, gameDTO);
    		
    	} else {
    		// OUT
    		
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
	
	public <T> void readyCheck(Set<WebSocketSession> sessions, T message) {
		// TODO
		System.out.println("=========== Enter UnionService =========");
		System.out.println("=========== readyCheck(Set) =========");
    	System.out.println();
    	
	}
	
	public <T> void uniCheck(WebSocketSession session, T message) {
		// TODO
		System.out.println("=========== Enter UnionService =========");
		System.out.println("=========== uniCheck(WebSocketSession) =========");
    	System.out.println();
		
    	try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> void onCheck(WebSocketSession session, T message) {
		// TODO
		System.out.println("=========== Enter UnionService =========");
		System.out.println("=========== onCheck(WebSocketSession) =========");
    	System.out.println();
    	
    	try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public <T> void countScore(WebSocketSession session, T message) {
		// TODO
		System.out.println("=========== Enter UnionService =========");
		System.out.println("=========== countScore(Set) =========");
    	System.out.println();
    	
    	try {
			session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
