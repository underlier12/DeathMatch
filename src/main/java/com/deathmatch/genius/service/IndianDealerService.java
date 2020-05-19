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
import com.deathmatch.genius.domain.IndianPlayerDTO.WinLoseType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class IndianDealerService {

	private final IndianSettingDAO indianDAO;
	private final ObjectMapper objectMapper;
	private final RecordService recordService;

	public IndianDealerService(IndianSettingDAO indianDAO, ObjectMapper objectMapper, RecordService recordService) {
		this.indianDAO = indianDAO;
		this.objectMapper = objectMapper;
		this.recordService = recordService;
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
		if (cardIndex >= 20) { //// 카드 인덱스가 20 이상이 되면 ReShuffle
			makeCardDeck();
			cardIndex = 0;
		}
		cardArr[0] = cardDeck.get(cardIndex++).getCardNum();
		// cardArr[0] = "10";
		// cardArr[1] = cardDeck.get(cardIndex++).getCardNum();
		cardArr[1] = "10";
		log.info("CardIndex " + cardIndex);
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

	public IndianDealerDTO nextRound(IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = processingMap(MessageType.NEXT, indianRoom.getRoomId());
		List<IndianPlayerDTO> players = indianRoom.getPlayers();

		if (players.get(0).getChip() == 0 && players.get(1).getChip() != 0) {
			return endGame(indianRoom);
		} else if (players.get(0).getChip() != 0 && players.get(1).getChip() == 0) {
			return endGame(indianRoom);
		}

		cardArr = drawCard(cardDeck);
		int[] chipArr = upAndDownChip(indianRoom, 0, 0);
		jsonMap.put("card1", cardArr[0]);
		jsonMap.put("card2", cardArr[1]);
		jsonMap.put("player", indianRoom.getPlayers().get(0).getUserId());
		jsonMap.put("player1Chip", chipArr[0]);
		jsonMap.put("player2Chip", chipArr[1]);
		jsonMap.put("message", nextTurn(indianRoom) + "님의 차례입니다 ");

		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		return indianDealerDTO;
	}

	public IndianDealerDTO resultRound(IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = processingMap(MessageType.BETRESULT, indianRoom.getRoomId());
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		int[] cardNums = parsingCard();
		if (cardNums[0] == cardNums[1]) {
			return draw(indianGameDTO, indianRoom);
		}
		if (players.get(0).getChip() == 0 && players.get(1).getChip() != 0) {
			return endGame(indianRoom);
		} else if (players.get(0).getChip() != 0 && players.get(1).getChip() == 0) {
			return endGame(indianRoom);
		}
		log.info("drawCheck");
		roundChipSetting(indianGameDTO, indianRoom);
		loseChip(indianGameDTO, indianRoom);
		jsonMap.put("card1", cardNums[0]);
		jsonMap.put("card2", cardNums[1]);
		jsonMap.put("player1Chip", players.get(0).getChip());
		jsonMap.put("player2Chip", players.get(1).getChip());
		jsonMap.put("player", indianRoom.getPlayers().get(0).getUserId());
		jsonMap.put("message", roundWinner(indianRoom));
		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		return indianDealerDTO;
	}

	public IndianDealerDTO drawNextRound(IndianGameRoom indianRoom, IndianGameDTO indianGameDTO) {
		Map<String, Object> jsonMap = processingMap(MessageType.NEXTDRAW, indianRoom.getRoomId());
		cardArr = drawCard(cardDeck);
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		int betChip1 = indianGameDTO.getPlayer1BetChip();
		int betChip2 = indianGameDTO.getPlayer2BetChip();
		log.info("DRAW BetChip1 " + betChip1);
		log.info("DRAW BetChip2 " + betChip2);
		jsonMap.put("card1", cardArr[0]);
		jsonMap.put("card2", cardArr[1]);
		jsonMap.put("player", indianRoom.getPlayers().get(0).getUserId());
		jsonMap.put("player1Chip", getPlayer1Chip(indianRoom,indianGameDTO));
		jsonMap.put("player2Chip", getPlayer2Chip(indianRoom,indianGameDTO));
		jsonMap.put("betChip1", betChip1);
		jsonMap.put("betChip2", betChip2);
		jsonMap.put("message", nextTurn(indianRoom) + "님의 차례입니다 ");
		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		return indianDealerDTO;
	}
	
	public int getPlayer1Chip(IndianGameRoom indianRoom,IndianGameDTO indianGameDTO) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		int betChip1 = indianGameDTO.getPlayer1BetChip();
		int betChip2 = indianGameDTO.getPlayer2BetChip();
		if(betChip2 > players.get(1).getChip()) {	// 두번째 플레이어가 패배하고, betChip2가 player2의 보유칩보다 클때
			String RoundWinner = roundWinner(indianRoom).substring(0, roundWinner(indianRoom).indexOf("님이"));
			if (RoundWinner.equals(players.get(0).getUserId())) {
				log.info("First Player Win! Return 0");
				return 0;
			}
		}
		return players.get(0).getChip() - betChip1;
	}
	
	public int getPlayer2Chip(IndianGameRoom indianRoom,IndianGameDTO indianGameDTO) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		int betChip1 = indianGameDTO.getPlayer1BetChip();
		int betChip2 = indianGameDTO.getPlayer2BetChip();
		if(betChip1 > players.get(0).getChip()) {	// 첫번째 플레이어가 패배하고, betChip1이 player1의 보유칩보다 클때
			String RoundWinner = roundWinner(indianRoom).substring(0, roundWinner(indianRoom).indexOf("님이"));
			if (RoundWinner.equals(players.get(1).getUserId())) {
				log.info("Second Player Win! Return 0");
				return 0;
			}
		}
		return players.get(1).getChip() - betChip2;
	}

	// 무승부시 그대로 베팅칩을 유지한다.
	public IndianDealerDTO draw(IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = processingMap(MessageType.DRAW, indianRoom.getRoomId());
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		int[] cardNums = parsingCard();
		int betChip1 = indianGameDTO.getPlayer1BetChip();
		int betChip2 = indianGameDTO.getPlayer2BetChip();
		log.info("BetChip1 " + betChip1);
		log.info("BetChip2 " + betChip2);
		jsonMap.put("card1", cardNums[0]);
		jsonMap.put("card2", cardNums[1]);
		jsonMap.put("player1Chip", getPlayer1Chip(indianRoom,indianGameDTO));
		jsonMap.put("player2Chip", getPlayer2Chip(indianRoom,indianGameDTO));
		jsonMap.put("player", indianRoom.getPlayers().get(0).getUserId());
		jsonMap.put("betChip1", betChip1);
		jsonMap.put("betChip2", betChip2);
		jsonMap.put("message", "무승부 입니다. 게임을 계속 진행합니다");
		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		return indianDealerDTO;
	}

	public void roundChipSetting(IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		String currentPlayer = indianGameDTO.getSender();
		log.info("roundCurrentPlayer:" + currentPlayer);
		int playerBetChip1 = indianGameDTO.getPlayer1BetChip();
		int playerBetChip2 = indianGameDTO.getPlayer2BetChip();
		log.info("playerBetChip1: " + playerBetChip1);
		log.info("playerBetChip2: " + playerBetChip2);

		if (currentPlayer.equals(players.get(0).getUserId())) {
			players.get(0).setBetChip(playerBetChip1);
			log.info("Player1 SetBetChip" + playerBetChip1);
		} else if (currentPlayer.equals(players.get(1).getUserId())) {
			players.get(1).setBetChip(playerBetChip2);
			log.info("Player2 SetBetChip" + playerBetChip2);
		}
		if (players.get(0).getBetChip() == players.get(1).getBetChip()) {
			log.info("Same chip!!");
			loseChip(indianGameDTO, indianRoom);
		}
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
		}
		return "Error";
	}

	public String finalWinner(IndianGameRoom indianRoom) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		String winner = "";
		if (players.get(0).getChip() > 0 && players.get(1).getChip() == 0) {
			winner = "인디언 게임 승자는 " + players.get(0).getUserId() + "입니다 ";
		} else if (players.get(1).getChip() > 0 && players.get(0).getChip() == 0) {
			winner = "인디언 게임 승자는 " + players.get(1).getUserId() + "입니다 ";
		}
		return winner;
	}

	public String announceWinner(IndianGameRoom indianRoom) {
		log.info("player size " + indianRoom.getPlayers().size());
		if (indianRoom.getPlayers().size() < 2) {
			indianRoom.getPlayers().get(0).setWinLose(WinLoseType.WIN);
			return indianRoom.getPlayers().get(0).getUserId();
		}
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		String winner = "";

		if (players.get(0).getChip() > 0 && players.get(1).getChip() == 0) {
			winner = players.get(0).getUserId();
			players.get(0).setWinLose(WinLoseType.WIN);
			players.get(1).setWinLose(WinLoseType.LOSE);
		} else if (players.get(1).getChip() > 0 && players.get(0).getChip() == 0) {
			winner = players.get(1).getUserId();
			players.get(0).setWinLose(WinLoseType.WIN);
			players.get(1).setWinLose(WinLoseType.LOSE);
		}
		return winner;
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
		loseTenChip(indianGameDTO, indianRoom, cardNums[0], cardNums[1]);
		giveUpLoseChip(indianGameDTO, indianRoom);
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

	public void loseChip(IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		String RoundWinner = roundWinner(indianRoom).substring(0, roundWinner(indianRoom).indexOf("님이"));
		int playerBetChip1 = indianGameDTO.getPlayer1BetChip();
		int playerBetChip2 = indianGameDTO.getPlayer2BetChip();
		log.info("RoundWinner " + RoundWinner);
		log.info("PlayerBetChip1 " + playerBetChip1 + " " + "playerChip1 " + players.get(0).getChip());
		log.info("PlayerBetChip2 " + playerBetChip2 + " " + "playerChip2 " + players.get(1).getChip());
		if (RoundWinner.equals(players.get(0).getUserId())) {
			// 첫번째 유저 Win!
			upAndDownChip(indianRoom, playerBetChip2, -playerBetChip2);
		} else if (RoundWinner.equals(players.get(1).getUserId())) {
			// 두번째 유저 Win!
			upAndDownChip(indianRoom, -playerBetChip1, playerBetChip1);
		}
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

	public void giveUpLoseChip(IndianGameDTO indianGameDTO, IndianGameRoom indianRoom) {
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		String currentPlayer = indianGameDTO.getSender();
		int playerBetChip1 = indianGameDTO.getPlayer1BetChip();
		int playerBetChip2 = indianGameDTO.getPlayer2BetChip();
		if (currentPlayer.equals(players.get(0).getUserId())) {
			// 첫번째 유저 포기 -> 두번째 유저 Win!
			upAndDownChip(indianRoom, -playerBetChip1, playerBetChip1);
		} else if (currentPlayer.equals(players.get(1).getUserId())) {
			// 두번째 유저 포기 -> 첫번째 유저 Win
			upAndDownChip(indianRoom, playerBetChip2, -playerBetChip2);
		}
	}

	public void loseTenChip(IndianGameDTO indianGameDTO, IndianGameRoom indianRoom, int cardNum1, int cardNum2) {
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
		jsonMap.put("player", whoseTurn); // turn Player로 비교
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

	public IndianDealerDTO endGame(IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = processingMap(MessageType.END, indianRoom.getRoomId());
		int[] cardNums = parsingCard();
		List<IndianPlayerDTO> players = indianRoom.getPlayers();
		String winnerR = announceWinner(indianRoom);
		log.info("Winner" + winnerR);
		jsonMap.put("card1", cardNums[0]);
		jsonMap.put("card2", cardNums[1]);
		jsonMap.put("player1Chip", players.get(0).getChip());
		jsonMap.put("player2Chip", players.get(1).getChip());
		jsonMap.put("message", "게임을 종료합니다");
		jsonMap.put("winner", finalWinner(indianRoom));
		log.info("EndGame");
		recordService.IndianRecordHistory(indianRoom);
		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		return indianDealerDTO;
	}

	public IndianDealerDTO stopGame(IndianGameRoom indianRoom) {
		Map<String, Object> jsonMap = processingMap(MessageType.STOP, indianRoom.getRoomId());
		jsonMap.put("message", "플레이어가 나가 인디언 게임이 종료 되었습니다");
		log.info("jsonMap " + jsonMap);
		IndianDealerDTO indianDealerDTO = processing(jsonMap);
		
		indianRoom.setPlaying(false);
		
		return indianDealerDTO;
	}

}
