package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndianDealerDTO {

	public enum MessageType{
		START,TURN,GIVEUP
	}
	
	private MessageType type;
	private String roomId;	// roomId로  해당 방에 들어감
	private String gameId; 
	private String sender;
	private String player;
	private String message;
	private String winner;
	private String chipMessage;
	private String card1;
	private String card2;
	private int chip1;
	private int chip2;
	private int score;

}
