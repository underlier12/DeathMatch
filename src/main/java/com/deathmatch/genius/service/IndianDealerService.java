package com.deathmatch.genius.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.deathmatch.genius.dao.IndianSettingDAO;
import com.deathmatch.genius.domain.IndianCardDTO;
import com.deathmatch.genius.domain.IndianDealerDTO;
import com.deathmatch.genius.domain.IndianDealerDTO.MessageType;
import com.deathmatch.genius.domain.IndianGameDTO;
import com.deathmatch.genius.domain.IndianGameRoom;
import com.deathmatch.genius.domain.IndianPlayerDTO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class IndianDealerService {

	private final IndianSettingDAO indianDAO;
	private final ObjectMapper objectMapper;

	public IndianDealerService(IndianSettingDAO indianDAO, ObjectMapper objectMapper) {
		this.indianDAO = indianDAO;
		this.objectMapper = objectMapper;
	}

	public Map<String, Object> convertMap(MessageType type, String roomId) {
		Map<String, Object> jsonMap = new HashMap<>();
		jsonMap.put("type", type.toString());
		jsonMap.put("roomId", roomId);
		jsonMap.put("sender", "Dealer");

		return jsonMap;
	}

	public IndianDealerDTO processing(Map<String, Object> jsonMap) {
		JSONObject jsonObject = new JSONObject(jsonMap);
		String jsonString = jsonObject.toJSONString();
		IndianDealerDTO indianDealerDTO = null;
		log.info("jsonString : " + jsonString);

		try {
			indianDealerDTO = objectMapper.readValue(jsonString, IndianDealerDTO.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return indianDealerDTO;
	}

	/* Make Problem */

	List<IndianCardDTO> cardDeck = new ArrayList<>();
	String[] cardArr;
	int cardIndex = 0;

	public void makeCardDeck() {
		cardDeck = indianDAO.problemList();
	}

	public String[] drawCard(List<IndianCardDTO> cardDeck) {
		cardArr = new String[2];
		int startIdx = cardIndex;
		cardIndex += 2;
		cardArr[0] = cardDeck.get(startIdx).getCardNum();
		//cardArr[1] = cardDeck.get(startIdx + 1).getCardNum();
		cardArr[1] = "10";
		return cardArr;
	}

	public IndianDealerDTO draw(String[] cardArr, IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = convertMap(MessageType.DRAW, indianRoom.getRoomId());
		makeCardDeck();
		cardArr = drawCard(cardDeck);
		jsonMap.put("card1", cardArr[0]);
		jsonMap.put("card2", cardArr[1]);
		jsonMap.put("player", indianRoom.getPlayers().get(0).getUserId());
		jsonMap.put("chip1", indianRoom.getPlayers().get(0).getChip());
		jsonMap.put("chip2", indianRoom.getPlayers().get(1).getChip());
		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		return indianDealerDTO;
	}

	public IndianDealerDTO drawAct(IndianGameRoom indianRoom) {
		makeCardDeck();
		IndianDealerDTO getCard = draw(cardArr, indianRoom);
		log.info("drawAct");
		return getCard;
	}

	public String endRound(IndianGameRoom indianRoom) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		int [] cardNums = getCardNum();
		int cardNum1 = cardNums[0];
		int cardNum2 = cardNums[1];
		log.info("player: " + players.get(0).getUserId() + " 카드는 : " + cardNum1);
		log.info("player: " + players.get(1).getUserId() + " 카드는 : " + cardNum2);
		if(cardNum1 > cardNum2) {
			return "승자는 " + players.get(0).getUserId() + " 입니다 ";
		}else if (cardNum1 < cardNum2) {
			return "승자는 " + players.get(1).getUserId() + " 입니다 ";
		}else if(cardNum1 == cardNum2) {
			return "무승부 입니다 ";
		}
		return  "Error";
	}
	
	public String loseTenCard(IndianGameDTO indianGameDTO,IndianGameRoom indianRoom) {
		int [] cardNums = getCardNum();
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		//String message = " ";
		int cardNum1 = cardNums[0]; //player1 의 카드
		int cardNum2 = cardNums[1]; //player2의 카드
		int chip1 = players.get(0).getChip();
		int chip2 = players.get(1).getChip();
		String player = indianGameDTO.getSender();
		log.info("lose Ten card request Player: " + player);
		
		// 클라이언트에서 요청한 사람이 첫번째 플레이어라면
		if(player.equals(players.get(0).getUserId()) 
				&& cardNum1 == 10) {
			chip1-=10;
			log.info(players.get(0).getUserId() + " 님의 칩의 개수는: "
					+ chip1 + "입니다");
			return players.get(0).getUserId() + " 님이 칩 10개를 잃었습니다";
		}else if(player.equals(players.get(1).getUserId())
				&& cardNum2 == 10) {
			chip2-=10;
			log.info(players.get(1).getUserId() + " 님의 칩의 개수는: "
					+ chip2 + "입니다");
			return players.get(1).getUserId() + " 님이 칩 10개를 잃었습니다 ";
		}
		
		log.info(players.get(0).getUserId() + " 님의 칩의 개수는: "
				+ chip1 + "입니다");
		log.info(players.get(1).getUserId() + " 님의 칩의 개수는: "
				+ chip2 + "입니다");
		
		return "Card is Ten but not betting3" ;
	}
	
	public int[] getCardNum() {
		int [] cardNums = new int[2];
		cardNums[0] = Integer.parseInt(cardArr[0]);
		cardNums[1] = Integer.parseInt(cardArr[1]);
		return cardNums;
	}
	
	public IndianDealerDTO whoseTurn(IndianGameRoom indianRoom) {
		Map<String,Object> jsonMap = convertMap(MessageType.TURN,indianRoom.getRoomId());
		String whoseTurn = nextTurn(indianRoom);
		jsonMap.put("message", whoseTurn + " 님의 차례입니다 ");
		jsonMap.put("player",whoseTurn);
		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		return indianDealerDTO;
	}
	
	public String nextTurn(IndianGameRoom indianRoom) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		String myTurn;
		log.info("nextTurn Method");
		if(!players.get(0).getTurn()) {
			myTurn = players.get(0).getUserId();
			players.get(0).setTurn(true);
			players.get(1).setTurn(false);
		} else {
			myTurn = players.get(1).getUserId();
			players.get(1).setTurn(true);
			players.get(0).setTurn(false);
		}
		log.info(players.get(0).getTurn());
		log.info(players.get(1).getTurn());
		log.info(myTurn);
		return myTurn;
	}
}
