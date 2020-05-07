package com.deathmatch.genius.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class IndianPlayerDTO {
	
	public enum StatusType {
		HOST, OPPONENT, GUEST
	}
	
	private String userId;
	private String roomId;
	private StatusType status;
	private Boolean ready;
	private Boolean turn;
	private int chip;
	private int betChip;
	private int num;
	
	@Builder
	public IndianPlayerDTO(String userId,String roomId,StatusType status,
			Boolean ready,Boolean turn,int chip,int betChip) {
		this.userId = userId;
		this.roomId = roomId;
		this.status = status;
		this.ready = ready;
		this.turn = turn;
		this.chip = chip;
		this.betChip = betChip;
	}

}
