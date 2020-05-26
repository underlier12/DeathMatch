package com.deathmatch.genius.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.deathmatch.genius.domain.Criteria;
import com.deathmatch.genius.domain.SuggestionBoardDTO;
import com.deathmatch.genius.domain.SuggestionReplyDTO;

import lombok.extern.log4j.Log4j;

@Log4j
@Repository
public class SuggestionBoardDAOImpl implements SuggestionBoardDAO {
	
	private String namespace = "com.deathmatch.genius.mapper.SuggestionBoardMapper";
	private final SqlSession sqlSession;
	
	public SuggestionBoardDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	@Override
	public void insert(SuggestionBoardDTO suggestionBoardDTO) {
		sqlSession.insert(namespace + ".insert" ,suggestionBoardDTO);
	}

	@Override
	public int delete(int bno) {
		sqlSession.delete(namespace + ".delete",bno);
		return 1;
	}

	@Override
	public void update(SuggestionBoardDTO suggestionBoardDTO) {
		log.info(" update SuggestionBoardDTO" );
		sqlSession.update(namespace + ".update",suggestionBoardDTO);
	}

	@Override
	public SuggestionBoardDTO read(int bno) {
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
	public List<SuggestionBoardDTO> getList(){
		return sqlSession.selectList(namespace + ".listAll");
	}

	@Override
	public List<SuggestionBoardDTO> getListWithPaging(Criteria cri) {
		return sqlSession.selectList(namespace + ".getListWithPaging",cri);
	}

	@Override
	public void insertReply(SuggestionReplyDTO suggestionReplyDTO) {
		sqlSession.insert(namespace + ".insertReply",suggestionReplyDTO);
	}

	@Override
	public void deleteReply(int rno) {
		sqlSession.delete(namespace + ".deleteReply",rno);
	}
	
	@Override
	public void deleteAllReply(int bno) {
		sqlSession.delete(namespace + ".deleteAllReply",bno);
	}

	@Override
	public List<SuggestionReplyDTO> getReplyList(int bno) {
		return sqlSession.selectList(namespace + ".getReplyList",bno);
	}

	@Override
	public Integer getGroupNum() {
		return sqlSession.selectOne(namespace + ".selectMaxBno");
	}

	@Override
	public void insertAnswer(SuggestionBoardDTO suggestionBoardDTO) {
		sqlSession.insert(namespace + ".insertAnswer",suggestionBoardDTO);
		log.info("DAO insertAnswer");
	}

	@Override
	public void increaseGroupStep(int ref, int step) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("ref", ref);
		map.put("step",step);
		sqlSession.update(namespace + ".increaseGroup",map);
		log.info("DAO increaseGroupStep ");
	}

}
