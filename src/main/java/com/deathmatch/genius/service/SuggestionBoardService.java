package com.deathmatch.genius.service;

import java.util.List;
import com.deathmatch.genius.domain.SuggestionBoardDTO;
import com.deathmatch.genius.util.Criteria;

public interface SuggestionBoardService {

	public void insert(SuggestionBoardDTO suggestionBoardDTO);
	
	public int update(SuggestionBoardDTO suggestionBoardDTO);
	
	public int delete(int num);
	
	public SuggestionBoardDTO read(int num);
	
	public int totalCount(Criteria cri);
	
	public List<SuggestionBoardDTO> list();
	
	public List<SuggestionBoardDTO> getListWithPaging(Criteria cri);

}
