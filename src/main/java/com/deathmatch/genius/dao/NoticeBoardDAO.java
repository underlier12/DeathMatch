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

	public List<NoticeBoardDTO> getListWithPaging(Criteria cri);
	
}
