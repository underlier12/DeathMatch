package com.deathmatch.genius.dao;


import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.deathmatch.genius.domain.IndianCardDTO;
import lombok.extern.log4j.Log4j;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/root-context.xml" })
public class IndianDAOTest {
	
	@Autowired
	private IndianSettingDAO indianDAO;
	
	@Test
	public void getCardDeck() {
		List<IndianCardDTO> cardDeck = indianDAO.problemList();
		log.info("cardDeck size() : " + cardDeck.size());
		for(IndianCardDTO card : cardDeck) {
			log.info(card);
		}
	}
	
}
