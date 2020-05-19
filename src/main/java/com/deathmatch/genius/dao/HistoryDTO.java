package com.deathmatch.genius.dao;

import com.deathmatch.genius.domain.RecordDTO;

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
	
	public void embedUserRecord(RecordDTO record) {
		this.gameType = record.getGameType();
		this.userId = record.getUserId();
		this.userScore = record.getScore();
		this.winLose = record.getWinLose();
	}
	
	public void embedOpponentRecord(RecordDTO record) {
		this.opponentId = record.getUserId();
		this.opponentScore = record.getScore();
	}
}
