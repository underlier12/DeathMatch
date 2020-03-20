package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genious.dao.UnionDealerDAO;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionDealerDTO;
import com.deathmatch.genious.domain.UnionGameDTO;
import com.deathmatch.genious.domain.UnionPlayerDTO;
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
	
	public UnionDealerDTO whoseTurn(GameRoom gameRoom) {
		preprocessing();
		
		String myTurn = nextTurn(gameRoom);
		
		log.info("myTurn : " + myTurn);
						
		jsonMap.put("type", "TURN");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("user1", myTurn);
		jsonMap.put("message", myTurn + "님의 차례입니다. ");
		jsonMap.put("countDown", 10);
		
		postprocessing();
		
		return unionDealerDTO;
	}
	
	public String nextTurn(GameRoom gameRoom) {
		
		List<UnionPlayerDTO> engaged = gameRoom.getEngaged();
		String myTurn = engaged.get(1).getUserEmail();
		
		if(engaged.get(0).getTurn()) {
			myTurn = engaged.get(0).getUserEmail();
		}
		
		return myTurn;
	}
	
	public UnionDealerDTO decideRound(GameRoom gameRoom) {
		
		preprocessing();
		
		int nextRound = gameRoom.getRound() + 1;
		
		gameRoom.setRound(nextRound);
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
	
	public UnionDealerDTO uniResult(WebSocketSession session, GameRoom gameRoom, UnionGameDTO gameDTO, Boolean isCorrect) {
		preprocessing();
		
		String message;
		int score;
		
		if(isCorrect) {
			message = "정답 +3";
			score = 3;
		} else {
			message = "틀렸습니다 -1점";
			score = -1;
		}
		
		jsonMap.put("type", "UNI");
		jsonMap.put("answer", gameDTO.getMessage());
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("gameId", gameRoom.getGameId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("message", message);
		jsonMap.put("score", score);
		jsonMap.put("round", gameRoom.getRound());
		jsonMap.put("user1", gameDTO.getSender());
		
		log.info("jsonMap : " + jsonMap);
		
		postprocessing();
		
		unionDealerDAO.insertSubmittedAnswer(unionDealerDTO);
		scoring(session, score);
				
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
	
	public UnionDealerDTO onResult(WebSocketSession session, GameRoom gameRoom, UnionGameDTO gameDTO) {
		
		preprocessing();
		
		String message = null;
		int score = 0;
		
		String onInfo = onCheck(gameDTO, gameRoom);
		
		switch (onInfo) {
		case "CORRECT":
			message = "정답 +1점";
			score = 1;
			break;
			
		case "ALREADY-SUBMIT":
			message = "이미 제출된 답입니다 -1점";
			score = -1;
			break;
			
		case "INCORRECT":
			message = "틀렸습니다 -1점";
			score = -1;
			break;
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
		
		unionDealerDAO.insertSubmittedAnswer(unionDealerDTO);
		scoring(session, score);
		
		return unionDealerDTO;
	}
	
	public void scoring(WebSocketSession session, int score) {
		Map<String, Object> map = session.getAttributes();
		UnionPlayerDTO unionPlayerDTO = (UnionPlayerDTO) map.get("player");
		
		int currentScore = unionPlayerDTO.getScore();
		unionPlayerDTO.setScore(currentScore + score);
	}

	public UnionDealerDTO endGame(GameRoom gameRoom, UnionGameDTO gameDTO) {
		preprocessing();
		
		String winner = announceWinner(gameRoom);
		
		jsonMap.put("type", "END");
		jsonMap.put("roomId", gameRoom.getRoomId());
		jsonMap.put("sender", "Dealer");
		jsonMap.put("message", "데스매치 결합이 종료되었습니다.");
		jsonMap.put("user1", winner);
		
		log.info("jsonMap : " + jsonMap);
		
		postprocessing();
				
		return unionDealerDTO;
	}
	
	public String announceWinner(GameRoom gameRoom) {
		String winner;
		
		Set<WebSocketSession> sessions = gameRoom.getSessions();
		String[] userArray = new String[2];
		int[] scoreArray = new int[2];
		
		for(WebSocketSession sess : sessions) {
			Map<String, Object> map = sess.getAttributes();
			UnionPlayerDTO unionPlayerDTO = (UnionPlayerDTO) map.get("player");
			
			switch (unionPlayerDTO.getStatus()) {
			case "HOST":
				userArray[0] = unionPlayerDTO.getUserEmail();
				scoreArray[0] = unionPlayerDTO.getScore();
				break;

			case "OPPONENT":
				userArray[1] = unionPlayerDTO.getUserEmail();
				scoreArray[1] = unionPlayerDTO.getScore();
				break;
				
			default:
				break;
			}
			
		}
		
		if(scoreArray[0] > scoreArray[1]) {
			winner = userArray[0];
		} else if(scoreArray[0] < scoreArray[1]) {
			winner = userArray[1];
		} else {
			winner = "무승부";
		}
		return winner;
	}

}
