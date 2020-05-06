package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndianGameDTO {
	
	public enum MessageType{
		TALK,JOIN,READY,BETTING,GIVEUP,ROUND
	}
	
	private MessageType type;
	private String roomId;
	private String sender;
	private String message;
	private int player1Chip;
	private int player2Chip;
	private int betChip;	// 배팅한 칩의 개수
	//private int playerBetChip;	// 플레이어 각각의 배팅 칩 개수
	
	// result시 betChip
	private int player1BetChip;
	private int player2BetChip;
	
}
