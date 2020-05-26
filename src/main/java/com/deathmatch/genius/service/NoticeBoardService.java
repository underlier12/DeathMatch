package com.deathmatch.genius.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deathmatch.genius.dao.NoticeBoardDAO;
import com.deathmatch.genius.domain.Criteria;
import com.deathmatch.genius.domain.NoticeBoardDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;

@Log4j
@RequiredArgsConstructor
@Service
public class NoticeBoardService {

	private final NoticeBoardDAO noticeDAO;
	
	public void insert(NoticeBoardDTO noticeBoardDTO) {
		noticeDAO.insert(noticeBoardDTO);
	}

	
	public int update(NoticeBoardDTO NoticeBoardDTO) {
		noticeDAO.update(NoticeBoardDTO);
		log.info("SuggestionService Update Success");
		return 1;
	}

	@Transactional
	public int delete(int bno) {
		noticeDAO.delete(bno);
		return 1;
	}

	public NoticeBoardDTO read(int num) {
		return noticeDAO.read(num);
	}

	public void increaseViews(int bno) {
		noticeDAO.increaseViews(bno);
	}

	public int totalCount(Criteria cri) {
		return noticeDAO.totalCount(cri);
	}

	public List<NoticeBoardDTO> getListWithPaging(Criteria cri) {
		return noticeDAO.getListWithPaging(cri);
	}

}
