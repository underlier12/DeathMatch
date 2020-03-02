package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genious.domain.GameDTO;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionDealerDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UnionDealerService {

	private final ObjectMapper objectMapper = new ObjectMapper();

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
	
	public UnionDealerDTO standby(GameRoom gameRoom) {
		
		UnionDealerDTO unionDealerDTO = null;
		JSONObject jsonObject;
		Map<String, String> jsonMap = new HashMap<String, String>();

		jsonMap.put("type", "READY");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("message", "참가자들이 모두 준비를 마쳤습니다.\n곧 게임을 시작합니다.");
		jsonMap.put("score", "0");
		
		jsonObject = new JSONObject(jsonMap);
		String jsonString = jsonObject.toJSONString();
		System.out.println("jsonString : " + jsonString);

		try {
			unionDealerDTO = objectMapper.readValue(jsonString, UnionDealerDTO.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return unionDealerDTO;
	}
	
	public UnionDealerDTO uniCheck(GameRoom gameRoom) {
		
		String message;
		int score;
		UnionDealerDTO unionDealerDTO = null;
		
		if(gameRoom.getAnswerSet().size() == gameRoom.getSubmitedAnswerSet().size()) {
			message = "정답";
			score = 3;
		} else {
			message = "틀렸습니다";
			score = -1;
		}
		
		JSONObject jsonObject;
		Map<String, String> jsonMap = new HashMap<String, String>();

		jsonMap.put("type", "UNI");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("message", message);
		jsonMap.put("score", Integer.toString(score));
		
		jsonObject = new JSONObject(jsonMap);
		String jsonString = jsonObject.toJSONString();
		System.out.println("jsonString : " + jsonString);

		try {
			unionDealerDTO = objectMapper.readValue(jsonString, UnionDealerDTO.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return unionDealerDTO;
	}
	
	public <T> void onCheck(WebSocketSession session, T message) {
		
	}
	
	public <T> void countScore(WebSocketSession session, T message) {
		
	}
}
