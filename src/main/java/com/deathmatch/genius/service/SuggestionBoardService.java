package com.deathmatch.genius.service;

import java.util.List;

import com.deathmatch.genius.domain.Criteria;
import com.deathmatch.genius.domain.SuggestionBoardDTO;
import com.deathmatch.genius.domain.SuggestionReplyDTO;

public interface SuggestionBoardService {

	public void insert(SuggestionBoardDTO suggestionBoardDTO);
	
	public int update(SuggestionBoardDTO suggestionBoardDTO);
	
	public int delete(int num);
	
	public SuggestionBoardDTO read(int num);
	
	public int totalCount(Criteria cri);
	
	public void increaseViews(int bno);
	
	public List<SuggestionBoardDTO> list();
	
	public List<SuggestionBoardDTO> getListWithPaging(Criteria cri);
	
	public void insertReply(SuggestionReplyDTO suggestionReplyDTO);
	
	public void deleteReply(int rno);
	
	public List<SuggestionReplyDTO> getReplyList(int bno);
	
	public void registerAnswer(SuggestionBoardDTO suggestionBoardDTO);

}
