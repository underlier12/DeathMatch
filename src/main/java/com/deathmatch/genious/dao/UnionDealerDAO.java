package com.deathmatch.genious.dao;

import java.util.Set;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionGameDTO;

public interface UnionDealerDAO {

	// select all answer
	public Set<String> selectAllAnswer(GameRoom gameRoom);
	
	// select all correct submitted answer
	public Set<String> selecAllCorrectSubmittedAnswer(GameRoom gameRoom);
	
	// insert submitted answer
	public void insertSubmittedAnswer(UnionGameDTO gameDTO, GameRoom gameRoom);
}
