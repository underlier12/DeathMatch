package com.deathmatch.genius.dao;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.deathmatch.genius.domain.IndianCardDTO;

import lombok.extern.log4j.Log4j;

@Log4j
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/root-context.xml" })
public class IndianDAOTest {
	
	@Autowired
	private IndianSettingDAO indianDAO;
	
	private List<IndianCardDTO> cardDeck;
	
	@Before
	public void setUp() {
		for(int i=1; i<=10; i++) {
			IndianCardDTO card = new IndianCardDTO(i);
			cardDeck.add(card);
			log.info(card.toString());
		}
		for(int j=1; j<=10; j++) {
			IndianCardDTO card = new IndianCardDTO(j);
			cardDeck.add(card);
			log.info(card.toString());
		}
	}
	
	@Test
	public void makeCardDeck() {
		//assertThat(d, assertion);
	}
	
}
