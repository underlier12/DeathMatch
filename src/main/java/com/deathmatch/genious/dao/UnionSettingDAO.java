package com.deathmatch.genious.dao;

import java.util.List;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionCardDTO;

public interface UnionSettingDAO {

	// select all union card
//	public List<UnionCardDTO> selectAllCard();
	
	// make union problem
	public List<UnionCardDTO> makeUnionProblem();
	
	// select union problem
	public List<UnionCardDTO> selectUnionProblem(GameRoom gameRoom);
	
	// insert union problem
	public void insertProblem(GameRoom gameRoom, int idx, String cardName);
	
	// insert union answer
	public void inserAnswer(GameRoom gameRoom, String answer);
}
