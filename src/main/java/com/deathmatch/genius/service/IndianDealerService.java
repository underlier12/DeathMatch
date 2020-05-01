package com.deathmatch.genius.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import com.deathmatch.genius.dao.IndianSettingDAO;
import com.deathmatch.genius.domain.IndianCardDTO;
import com.deathmatch.genius.domain.IndianDealerDTO;
import com.deathmatch.genius.domain.IndianDealerDTO.MessageType;
import com.deathmatch.genius.domain.IndianGameDTO;
import com.deathmatch.genius.domain.IndianGameRoom;
import com.deathmatch.genius.domain.IndianPlayerDTO;
import com.fasterxml.jackson.core.JsonParseException;
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

	public Map<String, Object> processingMap(MessageType type, String roomId) {
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
	String[] cardArr = new String[2];
	int cardIndex = 0;

	public void makeCardDeck() {
		cardDeck = indianDAO.problemList();
	}

	public String[] drawCard(List<IndianCardDTO> cardDeck) {
		cardArr[0] = cardDeck.get(cardIndex++).getCardNum();
		// cardArr[0] = "10";
		cardArr[1] = cardDeck.get(cardIndex++).getCardNum();
		// cardArr[1] = "10";
		return cardArr;
	}

	public IndianDealerDTO startRound(IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = processingMap(MessageType.START, indianRoom.getRoomId());
		cardArr = drawCard(cardDeck);
		int[] chipArr = upAndDownChip(indianRoom, 0, 0);
		jsonMap.put("card1", cardArr[0]);
		jsonMap.put("card2", cardArr[1]);
		jsonMap.put("player", indianRoom.getPlayers().get(0).getUserId());
		jsonMap.put("player1Chip", chipArr[0]);
		jsonMap.put("player2Chip", chipArr[1]);
		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		return indianDealerDTO;
	}

	public IndianDealerDTO resultRound(IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = processingMap(MessageType.BETRESULT, indianRoom.getRoomId());
		int[] cardNums = parsingCard();
		//int betChip1 = indianGameDTO.getPlayer1BetChip();
		//int betChip2 = indianGameDTO.getPlayer2BetChip();
		//int playerBetChip = indianGameDTO.getPlayerBetChip();
		roundChip(indianGameDTO,indianRoom);
		jsonMap.put("card1", cardNums[0]);
		jsonMap.put("card2", cardNums[1]);
		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		return indianDealerDTO;
	}

	//int playerBetChip1 = 0;
	//int playerBetChip2 = 0;
	
	public void roundChip(IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		int[] cardNums = parsingCard();
		String winner = roundWinner(indianRoom).substring(0, roundWinner(indianRoom).indexOf("님이"));
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		String currentPlayer = indianGameDTO.getSender();
		log.info("roundCurrentPlayer:" + currentPlayer);
		int playerBetChip1 = indianGameDTO.getPlayer1BetChip();
		int playerBetChip2 = indianGameDTO.getPlayer2BetChip();
		log.info("playerBetChip1: " + playerBetChip1);
		log.info("playerBetChip2: " + playerBetChip2);
		
		if(currentPlayer.equals(players.get(0).getUserId())) {
			players.get(0).setBetChip(playerBetChip1);
			log.info("Player1 SetBetChip" + playerBetChip1);
		}else if(currentPlayer.equals(players.get(1).getUserId())) {
			players.get(1).setBetChip(playerBetChip2);
			log.info("Player2 SetBetChip" + playerBetChip2);
		}
		
		if(players.get(0).getBetChip() == players.get(1).getBetChip()) {
			log.info("Same Betting Chip!!!!");
		}
		
		
		/*
		 * if(currentPlayer.equals(players.get(0).getUserId()) &&
		 * cardNums[0]>cardNums[1]) { // 첫번째 플레이어 승리
		 * upAndDownChip(indianRoom,betChip1,-betChip2); log.info("firstPlayer Win");
		 * }else if(currentPlayer.equals(players.get(0).getUserId()) && cardNums[0] <
		 * cardNums[1]) { //첫번째 플레이어 패배 upAndDownChip(indianRoom,-betChip1,betChip2);
		 * log.info("firstPlayer Lose"); }else
		 * if(currentPlayer.equals(players.get(1).getUserId()) && cardNums[0]<
		 * cardNums[1]) { // 두번째 플레이어 승리 upAndDownChip(indianRoom,-betChip1,betChip2);
		 * log.info("SecondPlayer Win"); }else
		 * if(currentPlayer.equals(players.get(1).getUserId()) &&
		 * cardNums[0]>cardNums[1]) { // 두번쨰 플레이어 패배
		 * upAndDownChip(indianRoom,betChip1,-betChip2); log.info("SecondPlayer Lose");
		 * }
		 */

		/*
		 * if (winner.equals(players.get(0).getUserId())) { log.info("Player1 win");
		 * log.info("player get Chip1" + players.get(0).getChip());
		 * upAndDownChip(indianRoom, betChip1, -betChip2); } else if
		 * (winner.equals(players.get(1).getUserId())) { upAndDownChip(indianRoom,
		 * -betChip1, betChip2); log.info("Player2 win"); log.info("player get Chip2" +
		 * players.get(1).getChip()); }
		 */
	}

	public String roundWinner(IndianGameRoom indianRoom) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		int[] cardNums = parsingCard();
		log.info("player: " + players.get(0).getUserId() + " 카드는 : " + cardNums[0]);
		log.info("player: " + players.get(1).getUserId() + " 카드는 : " + cardNums[1]);
		if (cardNums[0] > cardNums[1]) {
			return players.get(0).getUserId() + "님이 승자 입니다 ";
		} else if (cardNums[0] < cardNums[1]) {
			return players.get(1).getUserId() + "님이 승자 입니다 ";
		} else if (cardNums[0] == cardNums[1]) {
			return "무승부 입니다 ";
		}
		return "Error";
	}

	public int[] upAndDownChip(IndianGameRoom indianRoom, int chipNums1, int chipNums2) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		int[] chipArr = new int[2];
		int chip1 = players.get(0).getChip() + chipNums1;
		chipArr[0] = chip1;
		int chip2 = players.get(1).getChip() + chipNums2;
		chipArr[1] = chip2;
		players.get(0).setChip(chip1);
		players.get(1).setChip(chip2);
		log.info("Player : " + indianRoom.getPlayers().get(0).getUserId() + "님의 칩은 "
				+ indianRoom.getPlayers().get(0).getChip());
		log.info("Player : " + indianRoom.getPlayers().get(1).getUserId() + "님의 칩은 "
				+ indianRoom.getPlayers().get(1).getChip());
		return chipArr;
	}

	public int[] parsingCard() {
		int[] cardNums = new int[2];
		cardNums[0] = Integer.parseInt(cardArr[0]);
		cardNums[1] = Integer.parseInt(cardArr[1]);
		return cardNums;
	}

	public IndianDealerDTO giveUpRound(IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		int[] cardNums = parsingCard();
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		String currentPlayer = indianGameDTO.getSender();
		Map<String, Object> jsonMap = processingMap(MessageType.GIVEUP, indianRoom.getRoomId());
		loseChip(indianGameDTO, indianRoom, cardNums[0], cardNums[1]);
		// Turn 종료후 상대 플레이어의 보여주기 위해 카드 번호 전송
		jsonMap.put("card1", cardNums[0]);
		jsonMap.put("card2", cardNums[1]);
		jsonMap.put("player1Chip", players.get(0).getChip());
		jsonMap.put("player2Chip", players.get(1).getChip());
		// Turn 종료후 플레이어를 비교하기 위해 플레이어 아이디 전송
		jsonMap.put("player", indianRoom.getPlayers().get(0).getUserId());

		// 포기시 announce winner
		if (currentPlayer.equals(players.get(0).getUserId())) {
			jsonMap.put("chipMessage", "포기하셨습니다 ! " + players.get(0).getUserId() + " 님이 칩을 잃었습니다 ");
			jsonMap.put("winner", players.get(1).getUserId() + " 님이 승자 입니다");
		} else if (currentPlayer.equals(players.get(1).getUserId())) {
			jsonMap.put("chipMessage", "포기 하셨습니다 ! " + players.get(1).getUserId() + " 님이 칩을 잃었습니다 ");
			jsonMap.put("winner", players.get(0).getUserId() + " 님이 승자 입니다");
		}
		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		return indianDealerDTO;
	}

	public void loseChip(IndianGameDTO indianGameDTO, IndianGameRoom indianRoom, int cardNum1, int cardNum2) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		String player = indianGameDTO.getSender();
		log.info("lose Ten card request Player: " + player);
		if (player.equals(players.get(0).getUserId()) && cardNum1 == 10) {
			// 클라이언트에서 요청한 사람이 첫번째 플레이어라면
			upAndDownChip(indianRoom, -10, 10);
		} else if (player.equals(players.get(1).getUserId()) && cardNum2 == 10) {
			// 클라이언트에서 요청한 사람이 두번째 플레이어라면
			upAndDownChip(indianRoom, 10, -10);
		}
	}

	public IndianDealerDTO whoseTurn(IndianGameRoom indianRoom, IndianGameDTO indianGameDTO) {
		Map<String, Object> jsonMap = processingMap(MessageType.TURN, indianRoom.getRoomId());
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		String whoseTurn = nextTurn(indianRoom);
		int[] betChipArr = betChip(indianRoom, indianGameDTO);
		int[] maxChipArr = modifyMaxChip(indianRoom, indianGameDTO);
		jsonMap.put("betChip1", betChipArr[0]);
		jsonMap.put("betChip2", betChipArr[1]);
		jsonMap.put("player1Chip", maxChipArr[0]);
		jsonMap.put("player2Chip", maxChipArr[1]);
		jsonMap.put("message", whoseTurn + " 님의 차례입니다 ");
		jsonMap.put("player", whoseTurn);	// turn Player로 비교
		jsonMap.put("checkPlayer", players.get(0).getUserId()); // checkPlayer로 확인
		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		return indianDealerDTO;
	}

	public int[] modifyMaxChip(IndianGameRoom indianRoom, IndianGameDTO indianGameDTO) {
		int[] maxChipArr = new int[2];
		int betChip = indianGameDTO.getBetChip();
		String currentPlayer = indianGameDTO.getSender();
		log.info("maxChip1 : " + indianGameDTO.getPlayer1Chip());
		log.info("maxChip2 : " + indianGameDTO.getPlayer2Chip());
		if (indianRoom.getPlayers().get(0).getUserId().equals(currentPlayer)) {
			maxChipArr[0] = indianGameDTO.getPlayer1Chip() - betChip;
			maxChipArr[1] = indianGameDTO.getPlayer2Chip();
		} else if (indianRoom.getPlayers().get(1).getUserId().equals(currentPlayer)) {
			maxChipArr[0] = indianGameDTO.getPlayer1Chip();
			maxChipArr[1] = indianGameDTO.getPlayer2Chip() - betChip;
		}
		return maxChipArr;
	}

	// 배팅한 유저의 턴이 돌아갈때마다 maxChip 전송한다
	public int[] betChip(IndianGameRoom indianRoom, IndianGameDTO indianGameDTO) {
		int[] betChipArr = new int[2];
		String currentPlayer = indianGameDTO.getSender();
		log.info("CurrentPlayer: " + currentPlayer);
		if (indianRoom.getPlayers().get(0).getUserId().equals(currentPlayer)) {
			betChipArr[0] = indianGameDTO.getPlayer1BetChip();
			betChipArr[1] = indianGameDTO.getPlayer2BetChip();
		} else if (indianRoom.getPlayers().get(1).getUserId().equals(currentPlayer)) {
			betChipArr[0] = indianGameDTO.getPlayer1BetChip();
			betChipArr[1] = indianGameDTO.getPlayer2BetChip();
		}
		return betChipArr;
	}

	public String nextTurn(IndianGameRoom indianRoom) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		String myTurn;
		log.info("nextTurn Method");
		if (!players.get(0).getTurn()) {
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
