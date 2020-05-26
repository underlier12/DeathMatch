package com.deathmatch.genius.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deathmatch.genius.dao.SuggestionBoardDAO;
import com.deathmatch.genius.domain.Criteria;
import com.deathmatch.genius.domain.SuggestionBoardDTO;
import com.deathmatch.genius.domain.SuggestionReplyDTO;

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
		// 새 글의 GroupNum 설정
		Integer getGroupNum = dao.getGroupNum();
		log.info("Group Num: " + getGroupNum);
		int ref = getGroupNum != null ? getGroupNum + 1 : 1;
		suggestionBoardDTO.setRef(ref);
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

		log.info("registAnaswer Start ");

		SuggestionBoardDTO getParentBoard = dao.read(suggestionBoardDTO.getBno());

		log.info("Service getParentBoard : " + getParentBoard.toString());

		log.info("Service Register Answer " + suggestionBoardDTO.toString());

		SuggestionBoardDTO registerAnswer = SuggestionBoardDTO.builder()
				.bno(suggestionBoardDTO.getBno())
				.userId(suggestionBoardDTO.getUserId())
				.title(suggestionBoardDTO.getTitle())
				.content(suggestionBoardDTO.getContent())
				.hit(suggestionBoardDTO.getHit()).ref(getParentBoard.getRef())
				.depth(getParentBoard.getDepth() + 1)
				.step(getParentBoard.getStep() + 1)
				.pw(getParentBoard.getPw())
				.build();

		dao.increaseGroupStep(getParentBoard.getRef(), getParentBoard.getStep());
		dao.insertAnswer(registerAnswer);

		log.info("Service Register End Answer");

	}

}
