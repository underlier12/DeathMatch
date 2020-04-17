package com.deathmatch.genius.dao;


import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import com.deathmatch.genius.domain.IndianCardDTO;
import lombok.extern.log4j.Log4j;

@Log4j
@Repository
public class IndianSettingDAOImpl implements IndianSettingDAO {

	private String namespace = "com.deathmatch.genius.mapper.IndianSettingMapper";
	private final SqlSession sqlSession;

	public IndianSettingDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public List<IndianCardDTO> problemList() {;
		log.info("Access test data ");
		return sqlSession.selectList(namespace+".cardList");
	}


}
