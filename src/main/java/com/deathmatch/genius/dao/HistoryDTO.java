package com.deathmatch.genius.dao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryDTO {

	private String gameType;
	private String userId;
	private String opponentId;
	private int userScore;
	private int opponentScore;
	private String winLose;
	
}
