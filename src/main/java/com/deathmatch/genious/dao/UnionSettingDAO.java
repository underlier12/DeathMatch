package com.deathmatch.genious.dao;

import java.util.List;
import java.util.Set;

import com.deathmatch.genious.domain.UnionCardDTO;
import com.deathmatch.genious.domain.UnionDatabaseDTO;

public interface UnionSettingDAO {

	// make union problem
	public List<UnionCardDTO> makeUnionProblem();
	
	// select union problem
	public List<UnionCardDTO> selectUnionProblem(UnionDatabaseDTO dbDTO);
	
	// insert union problem
	public void insertProblem(UnionDatabaseDTO dbDTO, List<String> problemCardNames);
	
	// insert union answer
	public void insertAnswer(UnionDatabaseDTO dbDTO, Set<String> answerSet);
}
