package com.deathmatch.genious.dao;

import java.util.List;
import java.util.Set;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionCardDTO;

public interface UnionSettingDAO {

	// make union problem
	public List<UnionCardDTO> makeUnionProblem();
	
	// select union problem
	public List<UnionCardDTO> selectUnionProblem(GameRoom gameRoom);
	
	// insert union problem
	public void insertProblem(GameRoom gameRoom, int idx, String cardName);
	
	// insert union answer
	public void insertAnswer(GameRoom gameRoom, Set<String> answerSet);
}
