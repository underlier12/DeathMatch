package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.deathmatch.genious.dao.UnionDealerDAO;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionDealerDTO;
import com.deathmatch.genious.domain.UnionGameDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Service
public class UnionDealerService {

	private final UnionDealerDAO unionDealerDAO;
	private final ObjectMapper objectMapper;
	
	private UnionDealerDTO unionDealerDTO;
	private Map<String, Object> jsonMap;
	private JSONObject jsonObject;
	private String jsonString;
	
	public void preprocessing() {
		jsonMap = new HashMap<>();
	}
	
	public void postprocessing() {
		jsonObject = new JSONObject(jsonMap);
		jsonString = jsonObject.toJSONString();
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

	}
	
	public UnionDealerDTO decideRound(GameRoom gameRoom) {
		
		preprocessing();
		
		int nextRound = gameRoom.getRound() + 1;
		
		gameRoom.setRound(nextRound);
		gameRoom.setGameId(makeGameId());
		log.info("gameId : " + gameRoom.getGameId());
						
		jsonMap.put("type", "ROUND");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("round", nextRound);
		jsonMap.put("message", Integer.toString(nextRound) + " ROUND 그림 공개합니다.");
		
		postprocessing();
		
		return unionDealerDTO;
	}
	
	public UnionDealerDTO closeRound(GameRoom gameRoom) {
		preprocessing();
						
		int currentRound = gameRoom.getRound();
		
		jsonMap.put("type", "END");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("message", Integer.toString(currentRound) + " ROUND가 종료되었습니다.");
		
		postprocessing();
		
		return unionDealerDTO;
	}
	
	public String makeGameId() {
		return UUID.randomUUID().toString();
	}

	public String uniCheck(UnionGameDTO gameDTO, GameRoom gameRoom) {		
		String isCorrect = null;
		
		int answerSize = unionDealerDAO.countAnswer(gameDTO, gameRoom);
		int submittedSize = unionDealerDAO.countCorrectSubmittedAnswer(gameDTO, gameRoom);
		
		if(answerSize == submittedSize) {
			isCorrect = "CORRECT";
		} else {
			isCorrect = "INCORRECT";
		}
		return isCorrect;
	}
	
	public UnionDealerDTO uniResult(GameRoom gameRoom, UnionGameDTO gameDTO, Boolean isCorrect) {
		preprocessing();
		
		jsonMap.put("type", "UNI");
		jsonMap.put("answer", gameDTO.getMessage());
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("gameId", gameRoom.getGameId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("round", gameRoom.getRound());
		jsonMap.put("user1", gameDTO.getSender());
		
		if(isCorrect) {
			jsonMap.put("message", "정답 +3");
			jsonMap.put("score", 3);
		} else {
			jsonMap.put("message", "틀렸습니다 -1점");
			jsonMap.put("score", -1);
		}
		
		log.info("jsonMap : " + jsonMap);
		
		postprocessing();
		
		unionDealerDAO.insertSubmittedAnswer(unionDealerDTO);
				
		return unionDealerDTO;
	}
	
	public String onCheck(UnionGameDTO gameDTO, GameRoom gameRoom) {
		String onInfo;
		
		Boolean isAnswer = unionDealerDAO.checkAnswer(gameDTO, gameRoom);
		Boolean isCorrectSubmittedAnswer = unionDealerDAO.checkCorrectSubmittedAnswer(gameDTO, gameRoom);
		
		log.info("isA : " + isAnswer);
		log.info("isSA : " + isCorrectSubmittedAnswer);
		
		if(isAnswer && !isCorrectSubmittedAnswer) {
			onInfo = "CORRECT";
		} else if(isCorrectSubmittedAnswer){
			onInfo = "ALREADY-SUBMIT";
		} else {
			onInfo = "INCORRECT";
		}
		
		return onInfo;
	}
	
	public UnionDealerDTO onResult(GameRoom gameRoom, UnionGameDTO gameDTO) {
		
		preprocessing();
		
		jsonMap.put("type", "ON");
		jsonMap.put("answer", gameDTO.getMessage());
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("gameId", gameRoom.getGameId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("user1", gameDTO.getSender());
		jsonMap.put("round", gameRoom.getRound());
		
		String onInfo = onCheck(gameDTO, gameRoom);
		
		switch (onInfo) {
		case "CORRECT":
			jsonMap.put("message", "정답 +1점");
			jsonMap.put("score", 1);
			break;

		case "ALREADY-SUBMIT":
			jsonMap.put("message", "이미 제출된 답입니다 -1점");
			jsonMap.put("score", -1);
			break;
		
		case "INCORRECT":
			jsonMap.put("message", "틀렸습니다 -1점");
			jsonMap.put("score", -1);
			break;
			
		default:
			break;
		}
		postprocessing();
		unionDealerDAO.insertSubmittedAnswer(unionDealerDTO);
		
		return unionDealerDTO;
	}

	public Object endGame(GameRoom gameRoom, UnionGameDTO gameDTO) {
		preprocessing();
		
		jsonMap.put("type", "END");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("message", "데스매치 결합이 종료되었습니다.");
		
		log.info("jsonMap : " + jsonMap);
		
		postprocessing();
				
		return unionDealerDTO;
	}

}
