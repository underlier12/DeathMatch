package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genious.domain.GameRoom;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GameRoomService {

	private ObjectMapper objectMapper = new ObjectMapper();
	private Map<String, GameRoom> gameRooms = new LinkedHashMap<>();
	
	public List<GameRoom> findAllRooms(){
		return new LinkedList<>(gameRooms.values());
	}
	
	public GameRoom findRoomById(String roomId) {
		
		System.out.println("=========== Enter ChatService =========");
    	System.out.println("=========== findRoomById(String) =========");
    	System.out.println();
    	
		return gameRooms.get(roomId);
	}
	
	public GameRoom createRoom(String name) {
		
		System.out.println("=========== Enter ChatService =========");
    	System.out.println("=========== createRoom(String) =========");
    	System.out.println();
		
		String randomId = UUID.randomUUID().toString();
		GameRoom chatRoom = GameRoom.builder()
				.roomId(randomId)
				.name(name)
				.build();
		gameRooms.put(randomId, chatRoom);
		System.out.println("chatRooms : " + gameRooms);
		return chatRoom;
	}
	
	public <T> void sendMessage(WebSocketSession session, T message) {
		
		System.out.println("=========== Enter ChatService =========");
    	System.out.println("=========== sendMessage(WebSocketSession, T) =========");
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
