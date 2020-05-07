package com.deathmatch.genius.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.deathmatch.genius.domain.LoginDTO;
import com.deathmatch.genius.domain.UserDTO;
import com.deathmatch.genius.service.UserService;
import com.deathmatch.genius.util.Email;
import com.deathmatch.genius.util.EmailSender;
import com.deathmatch.genius.util.KakaoConnectionUtil;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
@RequestMapping("/auth/user")
public class UserController {

	private final KakaoConnectionUtil kakaoLoginService;
	private final UserService userService;
	private final EmailSender emailSender;

	public UserController(KakaoConnectionUtil kakaoLoginService, UserService userService, EmailSender emailSender) {
		this.kakaoLoginService = kakaoLoginService;
		this.userService = userService;
		this.emailSender = emailSender;
	}
	
	@GetMapping("/login")
	public String loginHome(HttpSession session, Model model, HttpServletRequest request) {
		return "/user/login";
	}
	
	/*
	 * @GetMapping("/login") public void loginGet(@ModelAttribute("loginDTO")
	 * LoginDTO loginDTO) {
	 * 
	 * }
	 */

	@GetMapping("/mypage")
	public String myPage() {
		return "/user/mypage";
	}

	@GetMapping("/pw-change")
	public String changePwGet() {
		return "/user/pw-change";
	}
	
	@GetMapping("/info-modify")
	public String modifyInfo() {
		return "/user/info-modify";
	}
	
	
	@ResponseBody
	@PostMapping("/registration")
	public ResponseEntity<String> joinMember(@RequestBody UserDTO userDTO) {
		userService.insertMember(userDTO);
		return new ResponseEntity<>("Success",HttpStatus.OK);
	}

	// 비밀번호 변경
	@PostMapping("/pw-change")
	public String changePw(@RequestParam String currentPw, @RequestParam String changePw, HttpSession session,
			Model model) {
		UserDTO currentUser = (UserDTO)session.getAttribute("login");
		if(userService.checkPw(currentUser, currentPw)) {
			userService.changePw(currentUser,changePw);
		}else {
			model.addAttribute("msg","비밀번호가 일치하지 않습니다");
			return "/user/pw-change";
		}
		return "/user/mypage";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		Object currentUser = session.getAttribute("login");
		if (currentUser != null) {
			session.removeAttribute("login");
			session.invalidate();
		}
		return "redirect:/";
	}
	
	// LocalUser Login
	@PostMapping("/local-login")
	public String loginPost(LoginDTO loginDTO, HttpSession session, Model model) {
		UserDTO localUserDTO = userService.login(loginDTO);
		log.info("loginPost");
		if (localUserDTO == null) {
			log.info("Login User is Empty");
			model.addAttribute("msg", "등록되지 않은 회원이거나,아이디 비밀번호가 일치하지 않습니다");
			return "user/login";
		} else {
			log.info("Local User :" + localUserDTO.toString());
			model.addAttribute("userDTO", localUserDTO);
			return "user/local-login";
		}
	}

	// kakaoUser 로그인
	@GetMapping("/kakaoLogin")
	public String kakaoLogin(@RequestParam("code") String code, Model model, HttpSession session) {
		String accessToken = kakaoLoginService.getAccessToken(code);
		UserDTO kakaoUser = kakaoLoginService.getUserInfo(accessToken);
		log.info("login Info: " + kakaoUser.toString());
		// 유저 정보가 없으면 새로운 vo, 있으면 기존의 정보를 불러온다.
		userService.kakaoLogin(kakaoUser);
		model.addAttribute("userDTO", kakaoUser);
		return "main/rooms";
	}

	// 이메일 확인하기
	@ResponseBody
	@PostMapping("/check-email")
	public ResponseEntity<Integer> checkMember(@RequestBody UserDTO userDTO) {
		return new ResponseEntity<>(userService.checkUserEmail(userDTO),HttpStatus.OK);
	}

	// 비밀번호 찾기
	@ResponseBody
	@PostMapping("/pw-find")
	public ResponseEntity<String> findPw(@RequestBody UserDTO userDTO) {
		UserDTO findPwUser = userService.findPw(userDTO); // 새로운 Pw를 생성한 User
		Email email = userService.sendEmail(findPwUser);
		try {
			emailSender.SendEmail(email);
			return new ResponseEntity<>("Success",HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Error",HttpStatus.BAD_REQUEST);
		}
	}
	
	// 회원 탈퇴하기
	@PostMapping("/delete")
	public String deleteMember() {
		
		return "redirect:/";
	}

}
