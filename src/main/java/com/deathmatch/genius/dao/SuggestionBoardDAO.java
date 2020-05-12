package com.deathmatch.genius.dao;

import java.util.List;

import com.deathmatch.genius.domain.SuggestionBoardDTO;
import com.deathmatch.genius.util.Criteria;

public interface SuggestionBoardDAO {
	
	public void insert(SuggestionBoardDTO suggestionBoardDTO);
	
	public int delete(int bno);
	
	public void update(SuggestionBoardDTO suggestionBoardDTO);
	
	public SuggestionBoardDTO read(int bno);
	
	public int totalCount(Criteria cri);
	
	public List<SuggestionBoardDTO> getList();
	
	public List<SuggestionBoardDTO> getListWithPaging(Criteria cri);
	
}
