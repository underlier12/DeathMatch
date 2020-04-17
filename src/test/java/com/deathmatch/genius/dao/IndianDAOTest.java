package com.deathmatch.genius.dao;


import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.deathmatch.genius.domain.IndianCardDTO;
import com.deathmatch.genius.service.IndianDealerService;

import lombok.extern.log4j.Log4j;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/root-context.xml" })
public class IndianDAOTest {
	
	@Autowired
	private IndianSettingDAO indianDAO;
	@Autowired
	private IndianDealerService indianService;
	
	List<IndianCardDTO> cardDeck = new ArrayList<>();
	String[] cardArr;
	int cardIndex = 0;
	
	@Before
	public void getCardDeck() {
		cardDeck = indianDAO.problemList();
		log.info("cardDeck size() : " + cardDeck.size());
		for(IndianCardDTO card : cardDeck) {
			log.info(card);
		}
	}
	
	@Test
	public void draw() {
		cardArr = new String[2];
		cardArr = indianService.drawCard(cardDeck);
		log.info("card1 : " + cardArr[0]);
		log.info("card2 : " + cardArr[1]);
	}
	
}
