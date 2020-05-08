package com.deathmatch.genius.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IndianCardDTO {
	
	String cardNum;
	
	public IndianCardDTO() {};
	
	public IndianCardDTO(String cardNum) {
		this.cardNum = cardNum;
	}
	
}
