package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.deathmatch.genious.domain.UnionGameDTO;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionAnswerDTO;
import com.deathmatch.genious.domain.UnionCardDTO;
import com.deathmatch.genious.domain.UnionDealerDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Service
public class UnionDealerService {

	private final ObjectMapper objectMapper;// = new ObjectMapper();
	
	public UnionDealerDTO uniCheck(GameRoom gameRoom, UnionGameDTO gameDTO) {
		
		String message;
		int score;
		UnionDealerDTO unionDealerDTO = null;
		
		if(gameRoom.getAnswerSet().size() == gameRoom.getSubmitedAnswerSet().size()) {
			message = "정답 +3";
			score = 3;
		} else {
			message = "틀렸습니다 -1점";
			score = -1;
		}
		
		JSONObject jsonObject;
		Map<String, String> jsonMap = new HashMap<String, String>();

		jsonMap.put("type", "UNI");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("message", message);
		jsonMap.put("score", Integer.toString(score));
		jsonMap.put("user1", gameDTO.getSender());
		
		jsonObject = new JSONObject(jsonMap);
		String jsonString = jsonObject.toJSONString();
		log.info("jsonString : " + jsonString);

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
	
	public UnionDealerDTO onCheck(GameRoom gameRoom, UnionGameDTO gameDTO) {
		
		List<UnionCardDTO> problemList = gameRoom.getProblemList();
		List<UnionCardDTO> submitedList = new ArrayList<>();
		UnionAnswerDTO unionAnswerDTO = null;
		UnionDealerDTO unionDealerDTO = null;
		String message = gameDTO.getMessage();
		String[] messageArray = message.split("");
		
		for(String cardIndex : messageArray) {
			int intCardIndex = Integer.parseInt(cardIndex);
			UnionCardDTO card = problemList.get(intCardIndex-1);
			submitedList.add(card);
		}
		
		log.info("submitedList : " + submitedList.get(0).getName()
				+ " " + submitedList.get(1).getName()
				+ " " + submitedList.get(2).getName());
		
		unionAnswerDTO = UnionAnswerDTO.builder()
				.card1(submitedList.get(0))
				.card2(submitedList.get(1))
				.card3(submitedList.get(2))
				.build();
		
		JSONObject jsonObject = new JSONObject();
		Map<String, String> jsonMap = new HashMap<String, String>();
		
		jsonMap.put("type", "ON");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("user1", gameDTO.getSender());
		
		if(scoring(unionAnswerDTO, gameRoom)) {
			
			jsonMap.put("message", message + " 정답 +1점");
			jsonMap.put("score", "1");
			
		} else {
			
			jsonMap.put("message", message + " 틀렸습니다 -1점");
			jsonMap.put("score", "-1");
			
		}
		
		jsonObject = new JSONObject(jsonMap);
		String jsonString = jsonObject.toJSONString();
		log.info("jsonString : " + jsonString);
		
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
	
	public boolean scoring(UnionAnswerDTO unionAnswerDTO, GameRoom gameRoom) {
		
		boolean correct = false;
		
		for(UnionAnswerDTO answer : gameRoom.getAnswerSet()) {
			
			Set<UnionCardDTO> submitedAnswer = new HashSet<>();
			submitedAnswer.add(unionAnswerDTO.getCard1());
			submitedAnswer.add(unionAnswerDTO.getCard2());
			submitedAnswer.add(unionAnswerDTO.getCard3());
			submitedAnswer.add(answer.getCard1());
			submitedAnswer.add(answer.getCard2());
			submitedAnswer.add(answer.getCard3());
			
			if(submitedAnswer.size() == 3) {
				correct = true;
				gameRoom.getSubmitedAnswerSet().add(unionAnswerDTO);
				break;
			}

		}
		return correct;
	}

}
