package com.deathmatch.genius.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deathmatch.genius.dao.SuggestionBoardDAO;
import com.deathmatch.genius.domain.SuggestionBoardDTO;
import com.deathmatch.genius.domain.SuggestionReplyDTO;
import com.deathmatch.genius.util.Criteria;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class SuggestionBoardServiceImpl implements SuggestionBoardService {
	
	private final SuggestionBoardDAO dao;
	
	public SuggestionBoardServiceImpl(SuggestionBoardDAO dao) {
		this.dao = dao;
	}

	@Override
	public void insert(SuggestionBoardDTO suggestionBoardDTO) {
		dao.insert(suggestionBoardDTO);
	}

	@Override
	public int update(SuggestionBoardDTO suggestionBoardDTO) {
		dao.update(suggestionBoardDTO);
		log.info("SuggestionService Update Success");
		return 1;
	}

	@Transactional
	@Override
	public int delete(int bno) {
		dao.delete(bno);
		dao.deleteAllReply(bno);
		return 1;
	}

	@Override
	public SuggestionBoardDTO read(int num) {
		return dao.read(num);
	}

	@Override
	public void increaseViews(int bno) {
		dao.increaseViews(bno);
	}

	@Override
	public List<SuggestionBoardDTO> list() {
		return dao.getList();
	}
	
	@Override
	public int totalCount(Criteria cri) {
		return dao.totalCount(cri);
	}
	
	@Override
	public List<SuggestionBoardDTO> getListWithPaging(Criteria cri) {
		return dao.getListWithPaging(cri);
	}

	@Override
	public void insertReply(SuggestionReplyDTO suggestionReplyDTO) {
		dao.insertReply(suggestionReplyDTO);
	}

	@Override
	public void deleteReply(int rno) {
		dao.deleteReply(rno);
	}

	@Override
	public List<SuggestionReplyDTO> getReplyList(int bno) {
		return dao.getReplyList(bno);
	}

	@Override
	public void registerAnswer(SuggestionBoardDTO suggestionBoardDTO) {
		
		// 
		SuggestionBoardDTO answerBoard = dao.read(suggestionBoardDTO.getPno());
		
		
		
		dao.increaseGroupStep(suggestionBoardDTO.getRef(), suggestionBoardDTO.getStep());
		dao.insertAnswer(suggestionBoardDTO);
	}
	
}
