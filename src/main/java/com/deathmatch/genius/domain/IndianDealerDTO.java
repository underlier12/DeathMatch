package com.deathmatch.genius.domain;

import java.util.List;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndianDealerDTO {

	public enum MessageType{
		DRAW
	}
	
	private List<IndianCardDTO> cardDeck;
	private MessageType type;
	private String roomId;	// roomId로  해당 방에 들어감
	private String gameId; 
	private String sender;	
	private String message;
	private String card1;
	private String card2;
	private int score;

}
