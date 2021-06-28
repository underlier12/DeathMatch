package com.deathmatch.genius.dao;

import lombok.extern.log4j.Log4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.deathmatch.genius.domain.LoginDTO;
import com.deathmatch.genius.domain.UserDTO;

@Log4j
@Repository
public class UserDAOImpl implements UserDAO {

	private String namespace = "com.deathmatch.genius.mapper.MemberMapper";

	private SqlSession sqlSession;

	public UserDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public void insertMember(UserDTO userDTO) {
		sqlSession.insert(namespace + ".insertMember", userDTO);
	}

	@Override
	public void insertAuth(UserDTO userDTO) {
		sqlSession.insert(namespace + ".insertAuth",userDTO);
	}

	@Override
	public UserDTO login(LoginDTO loginDTO) {
		return sqlSession.selectOne(namespace + ".login", loginDTO);
	}

	@Override
	public void deleteMember(UserDTO userDTO) {
		sqlSession.insert(namespace + ".delete", userDTO);
	}

	@Override
	public int deleteAllMember() {
		return 0;
	}

	@Override
	public int changePw(UserDTO userDTO) {
		int result = 0;
		sqlSession.update(namespace + ".changePw",userDTO);
		result = 1;
		return result;
	}
	
	@Override
	public String getUserPassword(UserDTO userDTO) {
		return sqlSession.selectOne(namespace+".checkPw",userDTO);
	}

	@Override
	public UserDTO findId(UserDTO userDTO) {
		return null;
	}

	@Override
	public UserDTO findPw(UserDTO userDTO) {
		return null;
	}

	@Override
	public void insertKakaoMember(UserDTO userDTO) {
		sqlSession.insert(namespace + ".insertSnsMember", userDTO);
	}

	@Override
	public UserDTO selectKakaoMember(UserDTO userDTO) {
		return sqlSession.selectOne(namespace + ".selectMember", userDTO);
	}

	@Override
	public void insertNaverMember(UserDTO userDTO) {
		sqlSession.insert(namespace +".insertSnsMember",userDTO);
	}

	@Override
	public UserDTO selectNaverMember(UserDTO userDTO) {
		return sqlSession.selectOne(namespace + ".selectMember",userDTO);
	}

	@Override
	public int countMember(UserDTO userDTO) {
		return sqlSession.selectOne(namespace + ".countUser", userDTO);
	}
	
	@Override
	public UserDTO selectUser(UserDTO userDTO) {
		return sqlSession.selectOne(namespace+ ".selectMember",userDTO);
	}

	@Override
	public UserDTO checkUserEmail(UserDTO userDTO) {
		return sqlSession.selectOne(namespace + ".countUser",userDTO);
	}
}
