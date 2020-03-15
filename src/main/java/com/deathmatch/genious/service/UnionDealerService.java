package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import com.deathmatch.genious.dao.UnionDealerDAO;
import com.deathmatch.genious.dao.UnionSettingDAO;
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
	private String message;
	private int score;
	
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
		jsonMap.put("round", Integer.toString(nextRound));
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

	public String uniCheck(GameRoom gameRoom) {		
		String isCorrect = null;
		if(gameRoom.getAnswerSet().size() == gameRoom.getSubmitedAnswerSet().size()) {
			isCorrect = "CORRECT";
		} else {
			isCorrect = "INCORRECT";
		}
		return isCorrect;
	}
	
	public UnionDealerDTO correctUni(GameRoom gameRoom, UnionGameDTO gameDTO) {
		
		preprocessing();
		
		jsonMap.put("type", "UNI");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("message", "정답 +3");
		jsonMap.put("score", 3);
		jsonMap.put("user1", gameDTO.getSender());
		
		log.info("jsonMap : " + jsonMap);
		
		postprocessing();
				
		return unionDealerDTO;
	}
	
	public UnionDealerDTO incorrectUni(GameRoom gameRoom, UnionGameDTO gameDTO) {
		
		preprocessing();
		
		jsonMap.put("type", "UNI");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("message", "틀렸습니다 -1점");
		jsonMap.put("score", -1);
		jsonMap.put("user1", gameDTO.getSender());
		
		log.info("jsonMap : " + jsonMap);
		
		postprocessing();
				
		return unionDealerDTO;
	}
	
	public UnionDealerDTO onCheck(GameRoom gameRoom, UnionGameDTO gameDTO) {
		
		preprocessing();
		
//		message = gameDTO.getMessage();
//		
//		if(scoring(gameDTO, gameRoom)) {
//			
//			gameRoom.getSubmitedAnswerSet().add(message);
//			message = message + " 정답 +1점";
//			score = 1;
//			
//		} else {
//			
//			message = message + " 틀렸습니다 -1점";
//			score = -1;
//			
//		}
		score = onScoring(gameDTO, gameRoom);
		
		if(score > 0) {
			message = "정답 +1점";
		} else {
			message = "틀렸습니다 -1점";
		}
		
		jsonMap.put("type", "ON");
		jsonMap.put("answer", gameDTO.getMessage());
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("gameId", gameRoom.getGameId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("message", message);
		jsonMap.put("score", score);
		jsonMap.put("user1", gameDTO.getSender());
		jsonMap.put("round", gameRoom.getRound());
		
		postprocessing();
		
		unionDealerDAO.insertSubmittedAnswer(unionDealerDTO, gameRoom);
		
		return unionDealerDTO;
	}
	
//	public boolean scoring(UnionGameDTO gameDTO, GameRoom gameRoom) {
//		return gameRoom.getAnswerSet().contains(gameDTO.getMessage());
//	}
	
	public int onScoring(UnionGameDTO gameDTO, GameRoom gameRoom) {
		int score = 0;
		
		Boolean isAnswer = unionDealerDAO.checkAnswer(gameDTO, gameRoom);
//		Boolean isSubmittedAnswer = unionDealerDAO.checkCorrectSubmittedAnswer(gameDTO, gameRoom);
		
		log.info("isA : " + isAnswer);
//		log.info("isSA : " + isSubmittedAnswer);
		
//		if(isAnswer && !isSubmittedAnswer) {
		if(isAnswer) {
			score = 1;
		} else {
			score = -1;
		}
		
		return score;
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
