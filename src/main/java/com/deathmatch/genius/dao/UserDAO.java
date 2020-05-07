package com.deathmatch.genius.dao;

import com.deathmatch.genius.domain.LoginDTO;
import com.deathmatch.genius.domain.UserDTO;

public interface UserDAO {
	// 회원가입
    public void insertMember(UserDTO userDTO);
    // 로그인
    public UserDTO login(LoginDTO loginDTO);
    // 회원 탈퇴
    public int deleteMember(UserDTO userDTO);
    // 회원 전부 탈퇴
    public int deleteAllMember();
    // 비밀번호 변경
    public int changePw(UserDTO userDTO);
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
    // 유저 이메일 체크하기
    public UserDTO checkUserEmail(UserDTO userDTO);
    // 패스워드 조회
    public String getUserPassword(UserDTO userDTO);
    // 유저 조회하기
    public UserDTO selectUser(UserDTO userDTO);
}
