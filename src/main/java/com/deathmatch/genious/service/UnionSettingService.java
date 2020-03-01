package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionCardDTO;
import com.deathmatch.genious.domain.UnionCardDTO.BackType;
import com.deathmatch.genious.domain.UnionCardDTO.ColorType;
import com.deathmatch.genious.domain.UnionCardDTO.ShapeType;
import com.deathmatch.genious.domain.UnionProblemDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UnionSettingService {
	
	private List<UnionCardDTO> allCardList;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final Map<String, UnionCardDTO> problemMap = new LinkedHashMap<>();
//	private final Set<UnionAnswerDTO> answerSet = new HashSet<>();

	@PostConstruct
	public void init() {
		allCardList = new ArrayList<>();
		enumerateAllCards();
	}
	
	private void enumerateAllCards() {
		
		System.out.println("=========== Enter UnionSettingService =========");

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
					
//					System.out.println("name : " + name);
//					System.out.println("resource : " + resourceAddress);
					allCardList.add(unionCard);
				}
			}
		}
		
		System.out.println("allCardList : " + allCardList);
	}

	public int decideRound(int round) {
		return round+1;
	}
	
	public Map<String, UnionCardDTO> makeUnionProblem() {
		int selectCardNumber = 9;
		List<UnionCardDTO> randomCardList = allCardList;
		
		System.out.println("=========== Enter UnionSettingService =========");
		System.out.println("기본 카드 배열 : " + allCardList);
		Collections.shuffle(randomCardList);
		System.out.println("랜덤 카드 배열 : " + randomCardList);
		
		for(int i = 0; i < selectCardNumber; i++) {
			problemMap.put(randomCardList.get(i).getName(), randomCardList.get(i));
		}
		
		return problemMap;
		
	}
	
public UnionProblemDTO problemSetting(GameRoom gameRoom) {
		
		Map<String, UnionCardDTO> problemMap = makeUnionProblem();
		gameRoom.setProblemMap(problemMap);
		
		System.out.println("=========== Enter UnionService =========");
		System.out.println("problemMap : " + problemMap);
		System.out.println("problemMap.keySet() : " + problemMap.keySet());
		
		JSONObject jsonObject = new JSONObject();
		Map<String, String> jsonMap = new HashMap<String, String>();
		List<String> problemKeyList = new ArrayList<String>(problemMap.keySet());
		
		jsonMap.put("type", "PROBLEM");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Setting");
		
		jsonMap.put("card1", problemKeyList.get(0));
		jsonMap.put("card2", problemKeyList.get(1));
		jsonMap.put("card3", problemKeyList.get(2));
		jsonMap.put("card4", problemKeyList.get(3));
		jsonMap.put("card5", problemKeyList.get(4));
		jsonMap.put("card6", problemKeyList.get(5));
		jsonMap.put("card7", problemKeyList.get(6));
		jsonMap.put("card8", problemKeyList.get(7));
		jsonMap.put("card9", problemKeyList.get(8));
		
		jsonObject = new JSONObject(jsonMap);
		
		String jsonString = jsonObject.toJSONString();
		
		System.out.println("jsonString : " + jsonString);
		
		UnionProblemDTO unionProblemDTO = new UnionProblemDTO();
		
		try {
			unionProblemDTO = objectMapper.readValue(jsonString, UnionProblemDTO.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("unionProblemDTO : " + unionProblemDTO);
		
		return unionProblemDTO;
	}
	
	public void makeUnionAnswer() {
		
	}
}
