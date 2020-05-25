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

//	public NoticeBoardService(SuggestionBoardDAO dao) {
//		this.dao = dao;
//	}

	public void insert(NoticeBoardDTO noticeBoardDTO) {
		// 새 글의 GroupNum 설정
//		Integer getGroupNum = noticeDAO.getGroupNum();
//		log.info("Group Num: " + getGroupNum);
//		int ref = getGroupNum != null ? getGroupNum + 1 : 1;
//		noticeBoardDTO.setRef(ref);
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
//		noticeDAO.deleteAllReply(bno);
		return 1;
	}

	
	public NoticeBoardDTO read(int num) {
		return noticeDAO.read(num);
	}

	public void increaseViews(int bno) {
		noticeDAO.increaseViews(bno);
	}

//	public List<NoticeBoardDTO> list() {
//		return noticeDAO.getList();
//	}

	public int totalCount(Criteria cri) {
		return noticeDAO.totalCount(cri);
	}

	public List<NoticeBoardDTO> getListWithPaging(Criteria cri) {
		return noticeDAO.getListWithPaging(cri);
	}

//	public void insertReply(SuggestionReplyDTO suggestionReplyDTO) {
//		noticeDAO.insertReply(suggestionReplyDTO);
//	}
//
//	@Override
//	public void deleteReply(int rno) {
//		noticeDAO.deleteReply(rno);
//	}
//
//	@Override
//	public List<SuggestionReplyDTO> getReplyList(int bno) {
//		return noticeDAO.getReplyList(bno);
//	}
//
//	@Override
//	public void registerAnswer(NoticeBoardDTO NoticeBoardDTO) {
//
//		log.info("registAnaswer Start ");
//
//		NoticeBoardDTO getParentBoard = noticeDAO.read(NoticeBoardDTO.getBno());
//
//		log.info("Service getParentBoard : " + getParentBoard.toString());
//
//		log.info("Service Register Answer " + NoticeBoardDTO.toString());
//
//		NoticeBoardDTO registerAnswer = NoticeBoardDTO.builder()
//				.bno(NoticeBoardDTO.getBno())
//				.userId(NoticeBoardDTO.getUserId())
//				.title(NoticeBoardDTO.getTitle())
//				.content(NoticeBoardDTO.getContent())
//				.hit(NoticeBoardDTO.getHit()).ref(getParentBoard.getRef())
//				.depth(getParentBoard.getDepth() + 1)
//				.step(getParentBoard.getStep() + 1)
//				.pw(getParentBoard.getPw())
//				.build();
//
//		noticeDAO.increaseGroupStep(getParentBoard.getRef(), getParentBoard.getStep());
//		noticeDAO.insertAnswer(registerAnswer);
//
//		log.info("Service Register End Answer");
//
//	}

}
