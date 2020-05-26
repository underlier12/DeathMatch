package com.deathmatch.genius.dao;

import java.util.List;

import com.deathmatch.genius.domain.UnionDatabaseDTO;

public interface UnionLoadingDAO {

	// select union problem (only card names)
	public List<String> selectUnionProblemCardNames(UnionDatabaseDTO dbDTO);
	
	// select union answer sheet (only submitted)
	public List<String> selectUnionAnswerSheet(UnionDatabaseDTO dbDTO);
}
