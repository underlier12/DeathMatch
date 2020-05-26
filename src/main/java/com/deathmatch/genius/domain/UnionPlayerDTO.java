package com.deathmatch.genius.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnionPlayerDTO {

	public enum StatusType {
		HOST, OPPONENT, GUEST
	}
	
	public enum WinLoseType{
		WIN, DRAW, LOSE
	}
	
	private String userId;
	private String roomId;
	private StatusType status;
	private WinLoseType winLose;
	private Boolean ready;
	private Boolean turn;
	private int score;
	
	@Builder
	public UnionPlayerDTO(String userId, String roomId, StatusType status,
			Boolean ready, Boolean turn, int score) {
		this.userId = userId;
		this.roomId = roomId;
		this.status = status;
		this.ready = ready;
		this.turn = turn;
		this.score = score;
	}
}
