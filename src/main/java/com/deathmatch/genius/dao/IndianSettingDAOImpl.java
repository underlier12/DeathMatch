package com.deathmatch.genius.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import com.deathmatch.genius.domain.IndianCardDTO;

@Repository
public class IndianSettingDAOImpl implements IndianSettingDAO {

	private String namespace = "com.deathmatch.genius.mapper.IndianSettingMapper";
	private final SqlSession sqlSession;

	public IndianSettingDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<IndianCardDTO> problemList() {
		List<IndianCardDTO> cardList = sqlSession.selectList(namespace + ".cardList");
		List<IndianCardDTO> problemList = new ArrayList<>();
		problemList.addAll(cardList);
		problemList.addAll(cardList);
		Collections.shuffle(problemList);
		return problemList;
	}

}
