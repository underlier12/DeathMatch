package com.deathmatch.genius.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.deathmatch.genius.domain.GameRoom;
import com.deathmatch.genius.domain.IndianGameRoom;
import com.deathmatch.genius.util.Criteria;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Service
public class GameRoomService {

	private Map<String, Object> gameRooms = new LinkedHashMap<>();
	
//	public List<String> findAllId() {
//		return new LinkedList<>(gameRooms.keySet());
//	}

	public List<Object> findAllRooms() {
		return new LinkedList<>(gameRooms.values());
	}

	public List<Object> findRoomList(Criteria cri) {
		List<Object> roomList = new LinkedList<>();
		int roomCount = findAllRooms().size();
		int startNum = cri.getPageStart();	// 게시글의 시작 행
		int perPageNum = cri.getPerPageNum();	// 페이지당 보여질 게시글의 갯수
		Stream<Object> stream = findAllRooms().stream();
		
		log.info("현재 방의 개수 : " + roomCount);
		log.info("게시글의 시작 행 : " + startNum);
		log.info("페이지당 보여줄 개수 : " + perPageNum);
		
		roomList = stream.skip(startNum).limit(perPageNum).collect(Collectors.toList());
		return roomList;
	}

	public Object findRoomById(String roomId) {
		return gameRooms.get(roomId);
	}

	public Object createRoom(String gameType, String name) {
		String randomId = UUID.randomUUID().toString();
		Object gameRoom;
		if(gameType.equals("union")) {
			gameRoom = GameRoom.builder()
					.gameType(gameType)
					.roomId(randomId)
					.name(name)
					.build();
			gameRooms.put(randomId, gameRoom);			
		} else {
			gameRoom = IndianGameRoom.builder()
					.roomId(randomId)
					.roomName(name)
					.build();
			gameRooms.put(randomId, gameRoom);
		}
		return gameRoom;
	}

	public void destroyRoom(String roomId) {
		gameRooms.remove(roomId);
	}

	public int countRoom() {
		return gameRooms.size();
	}

}
