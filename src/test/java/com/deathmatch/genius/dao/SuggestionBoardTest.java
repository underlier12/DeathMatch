package com.deathmatch.genius.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.deathmatch.genius.domain.Criteria;
import com.deathmatch.genius.domain.SuggestionBoardDTO;
import com.deathmatch.genius.domain.SuggestionReplyDTO;

import lombok.extern.log4j.Log4j;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:../../../../../../main/webapp/WEB-INF/spring/applicationContext.xml"})
public class SuggestionBoardTest {
	
	@Autowired
	private SuggestionBoardDAO dao;
	
	@Test
	public void testListCriteria() {
		Criteria cri = new Criteria();
		cri.setPage(2);
		cri.setPerPageNum(10);
		log.info("TestList");
		
		List<SuggestionBoardDTO> pageList = dao.getListWithPaging(cri);
		
		for(SuggestionBoardDTO board :pageList) {
			log.info(board.getBno() + " : " + board.getTitle() );
		}
	}
	
	@Test
	public void testSearch() {
		Criteria cri = new Criteria();
		cri.setKeyword("p1234"); 
		cri.setType("W");
		
		List<SuggestionBoardDTO> list = dao.getListWithPaging(cri);
		list.forEach(SuggestionBoardDTO -> log.info(SuggestionBoardDTO));
		
	}
	
	@Test
	public void testReply() {
		log.info("TestReply");
		List<SuggestionReplyDTO> list = dao.getReplyList(275);
		list.forEach(SuggestionReplyDTO -> log.info(SuggestionReplyDTO));
	}
}
