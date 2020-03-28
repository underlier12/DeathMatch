package com.deathmatch.genious.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genious.dao.UnionDealerDAO;
import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionDealerDTO;
import com.deathmatch.genious.domain.UnionDealerDTO.MessageType;
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
	
	public Map<String, Object> preprocessing(MessageType type, String roomId) {
		Map<String, Object> jsonMap = new HashMap<>();
		
		jsonMap.put("type", type.toString());
		jsonMap.put("roomId", roomId);
		jsonMap.put("sender", "Dealer");
		
		return jsonMap;
	}
	
	public UnionDealerDTO postprocessing(Map<String, Object> jsonMap) {
		JSONObject jsonObject = new JSONObject(jsonMap);
		String jsonString = jsonObject.toJSONString();		
		UnionDealerDTO unionDealerDTO = null;

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
	
	public UnionDealerDTO whoseTurn(UnionGameDTO gameDTO, GameRoom gameRoom) {
		Map<String, Object> jsonMap = preprocessing(MessageType.TURN, gameRoom.getRoomId());
		
		String myTurn = gameDTO.getSender();
		String message = "결을 외칠 수 있습니다.";
		int time = 5;
		
		log.info("getmessage : " + gameDTO.getMessage());
		
		switch (gameDTO.getMessage()) {
		case "합!":
			message = "합이 되는 조합을 찾아주세요.";
			break;
			
		case "결!":
		case "READY":
		case "TIMEUP":
			time = 10;
			myTurn = nextTurn(gameRoom);
			message = myTurn + "님의 차례입니다.";
			break;
		}
		
		jsonMap.put("user1", myTurn);
		jsonMap.put("message", message);
		jsonMap.put("countDown", time);
		
		UnionDealerDTO unionDealerDTO = postprocessing(jsonMap);
		
		return unionDealerDTO;
	}
	
	public String nextTurn(GameRoom gameRoom) {
		List<UnionPlayerDTO> engaged = gameRoom.getEngaged();
		String myTurn;
		
		if(!engaged.get(0).getTurn()) {
			myTurn = engaged.get(0).getUserId();
			engaged.get(0).setTurn(true);
			engaged.get(1).setTurn(false);
		} else {
			myTurn = engaged.get(1).getUserId();
			engaged.get(1).setTurn(true);
			engaged.get(0).setTurn(false);
		}
		
		return myTurn;
	}
	
	public UnionDealerDTO decideRound(GameRoom gameRoom) {
		Map<String, Object> jsonMap = preprocessing(MessageType.ROUND, gameRoom.getRoomId());
		int nextRound = gameRoom.getRound() + 1;
		gameRoom.setRound(nextRound);
		
		jsonMap.put("round", nextRound);
		jsonMap.put("message", Integer.toString(nextRound) + " ROUND 그림 공개합니다.");
		
		UnionDealerDTO unionDealerDTO = postprocessing(jsonMap);
		
		return unionDealerDTO;
	}
	
	public UnionDealerDTO closeRound(GameRoom gameRoom) {
		Map<String, Object> jsonMap = preprocessing(MessageType.END, gameRoom.getRoomId());
		int currentRound = gameRoom.getRound();
		
		jsonMap.put("message", Integer.toString(currentRound) + " ROUND가 종료되었습니다.");
		
		UnionDealerDTO unionDealerDTO = postprocessing(jsonMap);
		
		return unionDealerDTO;
	}

	public String uniCheck(UnionGameDTO gameDTO, GameRoom gameRoom) {		
		int submittedSize = unionDealerDAO.countCorrectSubmittedAnswer(gameDTO, gameRoom);
		int answerSize = unionDealerDAO.countAnswer(gameDTO, gameRoom);
		return (answerSize == submittedSize) ? "CORRECT" : "INCORRECT";
	}
	
	public UnionDealerDTO uniResult(WebSocketSession session, GameRoom gameRoom, UnionGameDTO gameDTO, Boolean isCorrect) {
		Map<String, Object> jsonMap = preprocessing(MessageType.UNI, gameRoom.getRoomId());
		String message = "틀렸습니다 -1점";
		int score = -1;
		
		if(isCorrect) {
			message = "정답 +3";
			score = 3;
		}
		jsonMap.put("gameId", gameRoom.getGameId());
		jsonMap.put("answer", gameDTO.getMessage());
		jsonMap.put("message", message);
		jsonMap.put("score", score);
		jsonMap.put("round", gameRoom.getRound());
		jsonMap.put("user1", gameDTO.getSender());
		
		log.info("jsonMap : " + jsonMap);
		UnionDealerDTO unionDealerDTO = postprocessing(jsonMap);
		unionDealerDAO.insertSubmittedAnswer(unionDealerDTO);
		scoring(session, score);
				
		return unionDealerDTO;
	}
	
	public String onCheck(UnionGameDTO gameDTO, GameRoom gameRoom) {
		Boolean isCorrectSubmittedAnswer = unionDealerDAO.checkCorrectSubmittedAnswer(gameDTO, gameRoom);
		Boolean isAnswer = unionDealerDAO.checkAnswer(gameDTO, gameRoom);
		String onInfo;		
		
		if(isAnswer && !isCorrectSubmittedAnswer) onInfo = "CORRECT";
		else if(isCorrectSubmittedAnswer) 		  onInfo = "ALREADY-SUBMIT";
		else 									  onInfo = "INCORRECT";
		
		return onInfo;
	}
	
	public UnionDealerDTO onResult(WebSocketSession session, GameRoom gameRoom, UnionGameDTO gameDTO) {
		Map<String, Object> jsonMap = preprocessing(MessageType.ON, gameRoom.getRoomId());
		String onInfo = onCheck(gameDTO, gameRoom);
		String message = null;
		int score = 0;
		
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
		
		jsonMap.put("answer", gameDTO.getMessage());
		jsonMap.put("gameId", gameRoom.getGameId());
		jsonMap.put("message", message);
		jsonMap.put("score", score);
		jsonMap.put("user1", gameDTO.getSender());
		jsonMap.put("round", gameRoom.getRound());
		
		UnionDealerDTO unionDealerDTO = postprocessing(jsonMap);
		unionDealerDAO.insertSubmittedAnswer(unionDealerDTO);
		scoring(session, score);
		
		return unionDealerDTO;
	}
	
	public void scoring(WebSocketSession session, int score) {
		Map<String, Object> map = session.getAttributes();
		UnionPlayerDTO player = (UnionPlayerDTO) map.get("player");
		
		int currentScore = player.getScore();
		player.setScore(currentScore + score);
	}
	
	public UnionDealerDTO endGame(GameRoom gameRoom) {
		Map<String, Object> jsonMap = preprocessing(MessageType.END, gameRoom.getRoomId());
		String winner = announceWinner(gameRoom);
		
		jsonMap.put("message", "데스매치 결합이 종료되었습니다.");
		jsonMap.put("user1", winner);
		
		log.info("jsonMap : " + jsonMap);
		
		UnionDealerDTO unionDealerDTO = postprocessing(jsonMap);
		gameRoom.setPlaying(false);
				
		return unionDealerDTO;
	}
	
	public String announceWinner(GameRoom gameRoom) {
		log.info("engaged size : " + gameRoom.getEngaged().size());
		if(gameRoom.getEngaged().size() < 2) {
			return gameRoom.getEngaged().get(0).getUserId();
		}
		
		List<UnionPlayerDTO> engaged = gameRoom.getEngaged();
		String winner = "무승부";
		
		if(engaged.get(0).getScore() > engaged.get(1).getScore()) {
			winner = engaged.get(0).getUserId();
		} else if(engaged.get(0).getScore() < engaged.get(1).getScore()) {
			winner = engaged.get(1).getUserId();
		}
		return winner;
	}

}