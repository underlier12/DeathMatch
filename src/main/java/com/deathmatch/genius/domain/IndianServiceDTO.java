package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndianServiceDTO {
	
	public enum MessageType{
		JOIN,READY,LOAD
	}
	private MessageType type;
	private String roomId;
	private String player;
	private String message;
	//private String player1;
	//private String player2;
	private String[] cards;
	private int chip;
}
