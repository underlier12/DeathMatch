package com.deathmatch.genius.dao;

import java.util.List;

import com.deathmatch.genius.domain.Criteria;
import com.deathmatch.genius.domain.SuggestionBoardDTO;
import com.deathmatch.genius.domain.SuggestionReplyDTO;

public interface SuggestionBoardDAO {
	
	public void insert(SuggestionBoardDTO suggestionBoardDTO);
	
	public int delete(int bno);
	
	public void update(SuggestionBoardDTO suggestionBoardDTO);
	
	public SuggestionBoardDTO read(int bno);
	
	public int totalCount(Criteria cri);
	
	public void increaseViews(int bno);
	
	public List<SuggestionBoardDTO> getList();
	
	public List<SuggestionBoardDTO> getListWithPaging(Criteria cri);
	
	public void insertReply(SuggestionReplyDTO suggestionReplyDTO);
	
	public void deleteReply(int rno);
	
	public void deleteAllReply(int bno);
	
	public List<SuggestionReplyDTO> getReplyList(int bno);
	
	public Integer getGroupNum();
	
	public void insertAnswer(SuggestionBoardDTO suggestionBoardDTO);
	
	public void increaseGroupStep(int ref, int step);
	
}
