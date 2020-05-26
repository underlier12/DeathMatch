package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndianGameDTO {
	
	public enum MessageType{
		TALK,JOIN,READY,BETTING,GIVEUP,ROUND,NEXTDRAW
	}
	
	private MessageType type;
	private String roomId;
	private String sender;
	private String message;
	private int player1Chip;
	private int player2Chip;
	private int betChip;	// 배팅한 칩의 개수

	private int player1BetChip;
	private int player2BetChip;
	
}
