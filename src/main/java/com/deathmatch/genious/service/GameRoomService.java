package com.deathmatch.genious.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UserDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class GameRoomService {

	private Map<String, GameRoom> gameRooms = new LinkedHashMap<>();
	
	
	public List<String> findAllId(){
		return new LinkedList<>(gameRooms.keySet());
	}
	
	public List<GameRoom> findAllRooms(){
		return new LinkedList<>(gameRooms.values());
	}
	
	public GameRoom findRoomById(String roomId) {
		return gameRooms.get(roomId);
	}
	
	// 우영 Code
	/*
	 * public GameRoom createRoom(String name) {
	 * 
	 * String randomId = UUID.randomUUID().toString(); GameRoom gameRoom =
	 * GameRoom.builder() .roomId(randomId) .name(name) .build();
	 * gameRooms.put(randomId, gameRoom);
	 * 
	 * return gameRoom; }
	 */
	
	// GH 임시 작성 코드
	private final String[] userArr = new String[2];
	
	public String[] userListArr() {
		return userArr;
	}
	
	public GameRoom createRoom(String name, HttpSession session) {
		String randomId = UUID.randomUUID().toString();
		UserDTO currentUser = (UserDTO)session.getAttribute("login");
		
		if(userArr[0] == null) {
			userArr[0] = currentUser.getUserEmail();
		}else if(userArr[1] == null) {
			userArr[1] = currentUser.getUserEmail();
		}else if(userArr[1] != null) {
			//userArr[1]에 값이 있음 -> 두번째 방까지 들어갔으니까 새로운 방이 되어야함
			userArr[0] = currentUser.getUserEmail();
			userArr[1] = null;
		}
		
		System.out.println("첫번째 유저 :" + userArr[0]);
		System.out.println("두번째 유저: " + userArr[1]);
		
		GameRoom gameRoom = GameRoom.builder()
				.roomId(randomId)
				.name(name)
				.player1(userArr[0])
				.player2(userArr[1])
				.build();
		gameRooms.put(randomId, gameRoom);
		
		return gameRoom;
	}
	
	public void destroyRoom(String roomId) {
		gameRooms.remove(roomId);
	}
}
