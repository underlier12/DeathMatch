package com.deathmatch.genious.dao;

import java.util.List;

import com.deathmatch.genious.domain.GameRoom;
import com.deathmatch.genious.domain.UnionCardDTO;

public interface UnionSettingDAO {

	// select all union card
	public List<UnionCardDTO> selectAllCard();
	
	// select union problem
	public List<UnionCardDTO> selectUnionProblem(GameRoom gameRoom);
	
	// insert union problem
	public void insertProblem(GameRoom gameRoom, List<UnionCardDTO> problem);
	
	// insert union answer
	public void inserAnswer(GameRoom gameRoom, List<UnionCardDTO> answer);
}
