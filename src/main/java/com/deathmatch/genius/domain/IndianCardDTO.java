package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IndianCardDTO {
	
	private int cardNum;
	
	public IndianCardDTO(int cardNum) {
		this.cardNum = cardNum;
	}
	
}
