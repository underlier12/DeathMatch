package com.deathmatch.genius.domain;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IndianDealerDTO {

	private List<IndianCardDTO> cardDeck;
	private String roomId;	// roomId로  해당 방에 들어감
	private String gameId; 
	private String sender;	
	private String message;
	private int score;

}
