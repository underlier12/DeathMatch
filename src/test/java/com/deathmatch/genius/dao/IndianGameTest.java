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
import com.deathmatch.genius.domain.IndianPlayerDTO;
import com.deathmatch.genius.service.IndianDealerService;

import lombok.extern.log4j.Log4j;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/root-context.xml" })
public class IndianGameTest {

	@Autowired
	private IndianSettingDAO indianDAO;

	List<IndianCardDTO> cardDeck = new ArrayList<>();

	String[] cardArr = new String[2];
	int cardIndex = 0;
	int cnt = 0;

	@Before
	public void getCardDeck() {
		cardDeck = indianDAO.problemList();
		log.info("cardSize() !!!: " + cardDeck.size());
		for (IndianCardDTO card : cardDeck) {
			log.info(card);
		}
	}

	@Test
	public void draw() {
		log.info("cardIndex() : " + cardIndex);
		log.info("cardSize() : " + cardDeck.size());
		while (cardIndex != cardDeck.size()) {
			log.info("first Index: " + cardIndex);
			cardArr[0] = cardDeck.get(cardIndex++).getCardNum();
			log.info("first Card : " + cardArr[0]);
			log.info("two index: " + cardIndex);
			cardArr[1] = cardDeck.get(cardIndex++).getCardNum();
			log.info("second Card : " + cardArr[1]);
		}
		if(cardIndex >=20) {
			getCardDeck();
			cardIndex = 0;
		}
		while (cardIndex != cardDeck.size()) {
			log.info("first Index: " + cardIndex);
			cardArr[0] = cardDeck.get(cardIndex++).getCardNum();
			log.info("first Card : " + cardArr[0]);
			log.info("two index: " + cardIndex);
			cardArr[1] = cardDeck.get(cardIndex++).getCardNum();
			log.info("second Card : " + cardArr[1]);
		}
	}

}
