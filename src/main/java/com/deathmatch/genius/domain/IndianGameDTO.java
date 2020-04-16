package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndianGameDTO {
	
	public enum MessageType{
		TALK, JOIN, READY,LOAD;
	}
	
	private MessageType type;
	private String roomId;
	private String sender;
	private String message;
	
}
