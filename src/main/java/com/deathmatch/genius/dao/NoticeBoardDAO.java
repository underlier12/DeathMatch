package com.deathmatch.genius.dao;

import java.util.List;

import com.deathmatch.genius.domain.Criteria;
import com.deathmatch.genius.domain.NoticeBoardDTO;

public interface NoticeBoardDAO {
	
	public void insert(NoticeBoardDTO noticeBoardDTO);
	
	public int delete(int bno);
	
	public void update(NoticeBoardDTO noticeBoardDTO);
	
	public NoticeBoardDTO read(int bno);
	
	public int totalCount(Criteria cri);
	
	public void increaseViews(int bno);
	
//	public List<NoticeBoardDTO> getList();
	
	public List<NoticeBoardDTO> getListWithPaging(Criteria cri);
	
//	public void insertReply(SuggestionReplyDTO suggestionReplyDTO);
//	
//	public void deleteReply(int rno);
//	
//	public void deleteAllReply(int bno);
//	
//	public List<SuggestionReplyDTO> getReplyList(int bno);
	
//	public Integer getGroupNum();
	
//	public void insertAnswer(NoticeBoardDTO NoticeBoardDTO);
	
//	public void increaseGroupStep(int ref, int step);
	
}
