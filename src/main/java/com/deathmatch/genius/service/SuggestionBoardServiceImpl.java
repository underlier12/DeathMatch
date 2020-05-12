package com.deathmatch.genius.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deathmatch.genius.dao.SuggestionBoardDAO;
import com.deathmatch.genius.domain.SuggestionBoardDTO;
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

	@Override
	public int delete(int num) {
		dao.delete(num);
		return 1;
	}

	@Override
	public SuggestionBoardDTO read(int num) {
		return dao.read(num);
	}

	@Override
	public List<SuggestionBoardDTO> list() {
		return dao.getList();
	}

	@Override
	public int totalCount(Criteria cri) {
		// TODO Auto-generated method stub
		return 0;
	}

}
