package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionCardDTO;
import com.deathmatch.genious.domain.UnionCardDTO.BackType;
import com.deathmatch.genious.domain.UnionCardDTO.ColorType;
import com.deathmatch.genious.domain.UnionCardDTO.ShapeType;
import com.deathmatch.genious.domain.UnionSettingDTO;
import com.deathmatch.genious.util.UnionCombination;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Service
public class UnionSettingService {
	
	private final UnionCombination unionCombination;
	private final ObjectMapper objectMapper;

	private List<UnionCardDTO> allCardList;
	private Map<String, Object> jsonMap;
	private UnionSettingDTO unionSettingDTO;
	private JSONObject jsonObject;
	
	private String jsonString;
	
	@PostConstruct
	public void init() {
		allCardList = new ArrayList<>();
		enumerateAllCards();
	}
	
	private void enumerateAllCards() {
		
		ShapeType[] shapeValues = UnionCardDTO.ShapeType.values();
		ColorType[] colorValues = UnionCardDTO.ColorType.values();
		BackType[] backValues = UnionCardDTO.BackType.values();
		
		for(ShapeType shape : shapeValues) {
			for(ColorType color : colorValues) {
				for(BackType back : backValues) {
					
					char shapeFirstChar = shape.toString().charAt(0); 
					char colorFirstChar = color.toString().charAt(0);
					char backFirstChar = back.toString().charAt(0);
					
					String name = String.valueOf(shapeFirstChar)
							+ String.valueOf(colorFirstChar)
							+ String.valueOf(backFirstChar);
					
					String resourceAddress = "genious/resources/images/"
							+ name + ".jpg";
					
					UnionCardDTO unionCard = UnionCardDTO.builder()
							.name(name)
							.shape(shape)
							.color(color)
							.background(back)
							.resourceAddress(resourceAddress)
							.build();
					
					allCardList.add(unionCard);
				}
			}
		}
		
		log.info("allCardList : " + allCardList);
	}
	
	public void preprocessing() {
		jsonMap = new HashMap<>();
	}
	
	public void postprocessing() {
		jsonObject = new JSONObject(jsonMap);
		jsonString = jsonObject.toJSONString();
		log.info("jsonString : " + jsonString);

		try {
			unionSettingDTO = objectMapper.readValue(jsonString, UnionSettingDTO.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
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
	
	public UnionSettingDTO standby(GameRoom gameRoom) {
		
		preprocessing();
		
		jsonMap.put("type", "READY");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Setting");
		jsonMap.put("message", "참가자들이 모두 준비를 마쳤습니다.\n곧 게임을 시작합니다.");
		
		postprocessing();
		
		return unionSettingDTO;
	}
	
	
	
	public UnionSettingDTO setPlayers(GameRoom gameRoom) {
		
		preprocessing();
		
		Object[] players = gameRoom.getReadyUser().keySet().toArray();
		
		jsonMap.put("type", "READY");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Setting");
		jsonMap.put("user1", (String)players[0]);
		jsonMap.put("user2", (String)players[1]);
		
		postprocessing();
		
		return unionSettingDTO;
	}
	
	public List<UnionCardDTO> makeUnionProblem() {
		List<UnionCardDTO> problemList = new ArrayList<>();
		List<UnionCardDTO> randomCardList = allCardList;
		
		Collections.shuffle(randomCardList);
		
		for(int i = 0; i < 9; i++) {
			problemList.add(randomCardList.get(i));
		}
		
		return problemList;
	}
	
	public UnionSettingDTO setUnionProblem(GameRoom gameRoom) {
		
		preprocessing();
		
		List<UnionCardDTO> problemList = makeUnionProblem();
		List<String> problemCardNames = new ArrayList<>();
		gameRoom.setProblemList(problemList);
		
		log.info("problemList.toString : " + problemList.toString());

		for(UnionCardDTO card : problemList) {
			problemCardNames.add(card.getName());
		}
		
		jsonMap.put("type", "PROBLEM");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Setting");
		jsonMap.put("cards", problemCardNames);
		
		postprocessing();
		
		log.info("unionProblemDTO : " + unionSettingDTO + "\n");
		
		return unionSettingDTO;
	}
	
	public Set<String> makeUnionAnswer(List<UnionCardDTO> problemList,
			Set<UnionCardDTO[]>  answerCandidateSet) {
				
		Set<String> answerSet = new HashSet<>();
		for(UnionCardDTO[] answerCandidate : answerCandidateSet) {
			
			int satisfiedCondition = 0;

			Set<ShapeType> shapeList = new HashSet<>();
			Set<ColorType> colorList = new HashSet<>();
			Set<BackType> backList = new HashSet<>();
			
			for(int i = 0; i < 3; i++) {
				shapeList.add(answerCandidate[i].getShape());
				colorList.add(answerCandidate[i].getColor());
				backList.add(answerCandidate[i].getBackground());
			}
			
			if(shapeList.size() == 1 || shapeList.size() == 3) satisfiedCondition++;
			if(colorList.size() == 1 || colorList.size() == 3) satisfiedCondition++;
			if(backList.size() == 1 || backList.size() == 3) satisfiedCondition++;
			
			if(satisfiedCondition == 3) {
				
				int[] indices = new int[3];
				
				for(int i = 0; i < 3; i++) {
					indices[i] = problemList.indexOf(answerCandidate[i]) + 1;
				}
				
				Arrays.sort(indices);
				log.info(indices);
				
				String answer = Arrays.toString(indices).replaceAll("[^0-9]","");
				answerSet.add(answer);

			}
			
		}
		
		for(String answer : answerSet) {
			log.info("answer : " + answer);
		}
		log.info("answerSet.size() : " + answerSet.size() + "\n");
		
		return answerSet;
	}
	
	public void setUnionAnswer(GameRoom gameRoom){
		
		List<UnionCardDTO> problemList = gameRoom.getProblemList();
		
		Set<UnionCardDTO[]> answerCandidateSet = new HashSet<>();
		Set<String> answerSet = new HashSet<>();
		
		answerCandidateSet = unionCombination.makeCombination(problemList);
		answerSet = makeUnionAnswer(problemList, answerCandidateSet);
		gameRoom.setAnswerSet(answerSet);
		
		log.info("answerSet : " + gameRoom.getAnswerSet() + "\n");
	}	
}
