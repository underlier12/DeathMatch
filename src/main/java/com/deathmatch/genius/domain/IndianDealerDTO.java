package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndianDealerDTO {

	public enum MessageType{
		START,TURN,GIVEUP,BETRESULT,NEXT,DRAW,NEXTDRAW,END
	}
	
	private MessageType type;
	private String roomId;	// roomId로  해당 방에 들어감
	private String gameId; 
	private String sender;
	private String player;
	private String checkPlayer;
	private String message;
	private String winner;
	private String chipMessage;
	private String card1;
	private String card2;
	private String firstTurn;
	private int player1Chip;
	private int player2Chip;
	private int betChip1;
	private int betChip2;
}
