package com.deathmatch.genius.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.deathmatch.genius.domain.Criteria;
import com.deathmatch.genius.domain.NoticeBoardDTO;

import lombok.extern.log4j.Log4j;

@Log4j
@Repository
public class NoticeBoardDAOImpl implements NoticeBoardDAO {
	
	private String namespace = "com.deathmatch.genius.mapper.NoticeBoardMapper";
	private final SqlSession sqlSession;
	
	public NoticeBoardDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	@Override
	public void insert(NoticeBoardDTO noticeBoardDTO) {
		sqlSession.insert(namespace + ".insert" ,noticeBoardDTO);
	}

	@Override
	public int delete(int bno) {
		sqlSession.delete(namespace + ".delete",bno);
		return 1;
	}

	@Override
	public void update(NoticeBoardDTO noticeBoardDTO) {
		log.info(" update NoticeBoardDTO" );
		sqlSession.update(namespace + ".update",noticeBoardDTO);
	}

	@Override
	public NoticeBoardDTO read(int bno) {
		return sqlSession.selectOne(namespace + ".read",bno);
	}
	
	@Override
	public int totalCount(Criteria cri) {
		return sqlSession.selectOne(namespace + ".countList",cri);
	}

	@Override
	public void increaseViews(int bno) {
		log.info("increse Views " );
		sqlSession.update(namespace + ".increaseViews",bno);
	}
	
	@Override
	public List<NoticeBoardDTO> getListWithPaging(Criteria cri) {
		return sqlSession.selectList(namespace + ".getListWithPaging",cri);
	}

}
