package com.deathmatch.genious.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.deathmatch.genious.domain.GameRoom;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GameRoomService {

	private Map<String, GameRoom> gameRooms = new LinkedHashMap<>();
	
	public List<GameRoom> findAllRooms(){
		return new LinkedList<>(gameRooms.values());
	}
	
	public GameRoom findRoomById(String roomId) {
		return gameRooms.get(roomId);
	}
	
	public GameRoom createRoom(String name) {
		
		String randomId = UUID.randomUUID().toString();
		GameRoom gameRoom = GameRoom.builder()
				.roomId(randomId)
				.name(name)
				.build();
		gameRooms.put(randomId, gameRoom);
		
		return gameRoom;
	}
	
}
