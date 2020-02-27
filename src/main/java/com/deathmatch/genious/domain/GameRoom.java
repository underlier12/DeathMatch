package com.deathmatch.genious.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genious.service.GameRoomService;
import com.deathmatch.genious.service.UnionService;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GameRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public GameRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }
 
//    // 세션 연결
//    public void connectionEstablish(WebSocketSession session, GameDTO gameDTO, GameRoomService gameRoomService) {
//    	System.out.println("=========== Enter ChatRoom =========");
//    	System.out.println("=========== connectionEstablish(WebSocketSession, ChatMessage, ChatService) =========");
//    	System.out.println();
//    	
//        sessions.add(session);
//        gameDTO.setMessage(gameDTO.getSender() + "님이 입장했습니다.");
//    	sendMessage(gameDTO, gameRoomService);
//    	
//    }
    
    public void handleActions(WebSocketSession session, GameDTO gameDTO, 
    		GameRoomService gameRoomService, UnionService unionService) {
    	
    	System.out.println("=========== Enter ChatRoom =========");
    	System.out.println("=========== handleActions(WebSocketSession, ChatMessage, ChatService) =========");
    	System.out.println();
    	
    	if (gameDTO.getType().equals(GameDTO.MessageType.JOIN)) {
    		// JOIN
		    sessions.add(session);
		    gameDTO.setMessage(gameDTO.getSender() + "님이 입장했습니다.");
		    sendMessage(gameDTO, gameRoomService);
		    
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.TALK)) {
    		// TALK
    		sendMessage(gameDTO, gameRoomService);
    		
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

    public <T> void sendMessage(T message, GameRoomService gameRoomService) {
    	
    	System.out.println("=========== Enter ChatRoom =========");
    	System.out.println("=========== sendMessage(T, ChatService) =========");
    	System.out.println();
    	
        sessions.parallelStream().forEach(session -> gameRoomService.sendMessage(session, message));
    }
    
    public <T> void countScore(T message, UnionService unionService) {
    	System.out.println("=========== Enter ChatRoom =========");
    	System.out.println("=========== countScore(T, ChatService) =========");
    	System.out.println();
    	
    	sessions.parallelStream().forEach(session -> unionService.countScore(session, message));
    }
    
    public <T> void uniCheck(T message, UnionService unionService) {
    	System.out.println("=========== Enter ChatRoom =========");
    	System.out.println("=========== uniCheck(T, ChatService) =========");
    	System.out.println();
    	
    	sessions.parallelStream().forEach(session -> unionService.countScore(session, message));
    }
}
