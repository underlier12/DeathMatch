package com.deathmatch.genius.dao;

import java.util.List;

import com.deathmatch.genius.domain.UnionLoadingDTO;

public interface UnionLoadingDAO {

	// select union problem (only card names)
	public List<String> selectUnionProblemCardNames(UnionLoadingDTO loadingDTO);
	
	// select union answer sheet (only submitted)
	public List<String> selectUnionAnswerSheet(UnionLoadingDTO loadingDTO);
}
