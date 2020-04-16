package com.deathmatch.genius.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IndianPlayerDTO {
	
	private String userId;
	private String roomId;
	private Boolean ready;
	private Boolean turn;
	private int chip;
	
	@Builder
	public IndianPlayerDTO(String userId,String roomId,Boolean ready,Boolean turn,int chip) {
		this.userId = userId;
		this.roomId = roomId;
		this.ready = ready;
		this.turn = turn;
		this.chip = chip;
	}

}
