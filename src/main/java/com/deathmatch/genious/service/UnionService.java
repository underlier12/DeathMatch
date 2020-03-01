package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genious.domain.GameDTO;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionProblemDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UnionService {

	private final ObjectMapper objectMapper = new ObjectMapper();
//	private final UnionDealerService unionDealerService;
	private final UnionSettingService unionSettingService;

	public void handleActions(WebSocketSession session, GameDTO gameDTO, GameRoom gameRoom) {
		
		Set<WebSocketSession> sessions = gameRoom.getSessions();
		Map<String, Boolean> readyUser = gameRoom.getReadyUser();
		
		if (gameDTO.getType().equals(GameDTO.MessageType.JOIN)) {
			// JOIN
			sessions.add(session);
			readyUser.put(session.getId(), Boolean.FALSE);
			gameDTO.setMessage(gameDTO.getSender() + "님이 입장했습니다.");
			sendMessageAll(sessions, gameDTO);
			
		} else if (gameDTO.getType().equals(GameDTO.MessageType.TALK)) {
    		// TALK
			sendMessageAll(sessions, gameDTO);
			
//			testJSON(sessions, gameDTO);
    		
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.READY)) {
    		// READY
    		readyUser.put(session.getId(), Boolean.TRUE);
    		gameDTO.setMessage(gameDTO.getSender() + "님이 준비하셨습니다.");
    		sendMessageAll(sessions, gameDTO);
    		
    		if(readyCheck(readyUser)) {
    			gameDTO.setSender("Dealer");
    			gameDTO.setMessage("참여자들이 준비를 마쳤습니다. \n곧 게임을 시작합니다");
    			sendMessageAll(sessions, gameDTO);
    			
    			UnionProblemDTO unionProblemDTO = unionSettingService.problemSetting(gameRoom);
    			sendMessageAll(sessions, unionProblemDTO);
    			
    			System.out.println("gameRoom.getProblemMap : " + gameRoom.getProblemMap());
    			System.out.println("gameRoom.getP.keySet() : " + gameRoom.getProblemMap().keySet());
    		}
    		
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.SCORE)) {
    		// SCORE
    		
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.UNI)) {
    		// UNI
    		gameDTO.setMessage(gameDTO.getSender() + "님이 '결'을 외치셨습니다.");
    		sendMessageAll(sessions, gameDTO);
    		
    	} else if (gameDTO.getType().equals(GameDTO.MessageType.ON)) {
    		// ON
    		sendMessageAll(sessions, gameDTO);
    		
    	} else {
    		// OUT
    		
    	}
	}
	
//	private void testJSON(Set<WebSocketSession> sessions, GameDTO gameDTO) {
//		
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("type", "TALK");
//		jsonObject.put("roomId", gameDTO.getRoomId());
//		jsonObject.put("sender", "Setting");
//		jsonObject.put("message", "TestSetting");
//		
//		System.out.println("=========== Enter UnionService =========");
//		System.out.println("jsonObject : " + jsonObject);
//		
//		String jsonString = jsonObject.toJSONString();
//		
//		System.out.println("jsonString : " + jsonString);
//		
//		try {
//			GameDTO gameDTOTest = objectMapper.readValue(jsonString, GameDTO.class);
//			sendMessageAll(sessions, gameDTOTest);
//		} catch (JsonParseException e) {
//			e.printStackTrace();
//		} catch (JsonMappingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
//	}

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
	
	public boolean readyCheck(Map<String, Boolean> readyUser) {
		boolean isReady = false;
		int countReady = 0;
		
		for (Boolean ready : readyUser.values()) {
			if (ready) {
				countReady++;
			}
		}
		
    	if (countReady > 1) {
    		isReady = true;
    	}
    	return isReady;
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
