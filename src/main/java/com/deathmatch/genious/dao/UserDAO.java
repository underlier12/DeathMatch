package com.deathmatch.genious.dao;

import com.deathmatch.genious.domain.LoginDTO;
import com.deathmatch.genious.domain.UserDTO;

public interface UserDAO {
	// 회원가입
    public void insertMember(UserDTO userDTO);
    // 로그인
    public UserDTO login(LoginDTO loginDTO);
    // 회원 탈퇴
    public int deleteMember();
    // 회원 전부 탈퇴
    public int deleteAllMember();
    // 비밀번호 변경
    public int modifyPw(UserDTO userDTO);
    //아이디 찾기
    public UserDTO findId(UserDTO userDTO);
    //비밀번호 찾기
    public UserDTO findPw(UserDTO userDTO);
    // 카카오 회원 가입
    public void insertKakaoMember(UserDTO userDTO);
    // 카카오 회원 조회하기
    public UserDTO selectKakaoMember(UserDTO userDTO);
    // 네이버 회원 가입
    public void insertNaverMember(UserDTO userDTO);
    // 네이버 회원 조회하기
    public UserDTO selectNaverMember(UserDTO userDTO);
    // 유저수 확인하기
    public int countMember(UserDTO userDTO);
}
