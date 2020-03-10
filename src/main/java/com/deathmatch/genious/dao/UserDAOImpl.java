package com.deathmatch.genious.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import com.deathmatch.genious.domain.LoginDTO;
import com.deathmatch.genious.domain.UserDTO;

@Repository
public class UserDAOImpl implements UserDAO {

	private String namespace = "com.deathmatch.geniouse.mapper.MemberMapper";

	private SqlSession sqlSession;

	public UserDAOImpl(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	@Override
	public void insertMember(UserDTO userDTO) {
		sqlSession.insert(namespace + ".insertMember", userDTO);
	}

	@Override
	public UserDTO login(LoginDTO loginDTO) {
		return sqlSession.selectOne(namespace + ".login", loginDTO);
	}

	@Override
	public int deleteMember() {
		return 0;
	}

	@Override
	public int deleteAllMember() {
		return 0;
	}

	@Override
	public int modifyPw(UserDTO userDTO) {
		return 0;
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

}
