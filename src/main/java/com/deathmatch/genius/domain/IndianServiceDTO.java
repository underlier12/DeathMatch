package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndianServiceDTO {
	
	public enum MessageType{
		JOIN,READY,LOAD,RESULT,LEAVE
	}
	private MessageType type;
	private String roomId;
	private String player;
	private String message;
	private String firstTurn;
	private int card1;
	private int card2;
}
