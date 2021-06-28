package com.deathmatch.genius.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.deathmatch.genius.domain.LoginDTO;
import com.deathmatch.genius.domain.UserDTO;
import com.deathmatch.genius.service.UserService;
import com.deathmatch.genius.util.Email;
import com.deathmatch.genius.util.EmailSender;
import com.deathmatch.genius.util.KakaoConnectionUtil;
import lombok.extern.log4j.Log4j;

import java.security.Principal;

@Log4j
@Controller
@RequestMapping("/auth/user")
public class UserController {

	private final KakaoConnectionUtil kakaoLoginService;
	private final UserService userService;
	private final EmailSender emailSender;
	private final BCryptPasswordEncoder passwordEncoder;

	public UserController(KakaoConnectionUtil kakaoLoginService, UserService userService, EmailSender emailSender, BCryptPasswordEncoder passwordEncoder) {
		this.kakaoLoginService = kakaoLoginService;
		this.userService = userService;
		this.emailSender = emailSender;
		this.passwordEncoder = passwordEncoder;
	}
	
	//@GetMapping("/login")
	@RequestMapping("/login")
	public String loginHome(HttpSession session, Model model, HttpServletRequest request,HttpServletResponse response) {
		log.info("Request " + request.getParameter("loginFailMsg"));
		//model.addAttribute("loginFailMsg",)
		return "/user/login";
	}

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
	
	@GetMapping("/withdrawal")
	public String withdrawl() {
		return "/user/withdrawal";
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
			Model model,Principal principal) {
		UserDTO currentUser = UserDTO.builder()
						.userEmail(principal.getName())
						.build();
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
		log.info("Security Logout");
		/*Object currentUser = session.getAttribute("login");
		if (currentUser != null) {
			session.removeAttribute("login");
			session.invalidate();
		}*/
		return "redirect:/";
	}

	@GetMapping("/securityLogin")
	public String securityLogin(String error, String logout, Model model, Principal principal) {
		log.info("Error " + error);
		log.info("Logout " + logout);

		if(error != null){
			model.addAttribute("error", "계정을 확인해 주세요");
		}
		if(logout != null){
			model.addAttribute("logout","로그아웃");
		}

		return "/user/login";
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
	public String deleteMember(UserDTO userDTO,HttpSession session,RedirectAttributes rttr,Principal principal) {
		log.info("Get User DTO " +userDTO.toString());
		UserDTO currentUser = UserDTO.builder()
				.userEmail(principal.getName())
				.build();
		userDTO.setUserEmail(principal.getName());

		if(userService.checkPw(currentUser, userDTO.getPw())){
			log.info("회원 탈퇴 성공");
			userService.deleteMember(userDTO);
		}else {
			rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다");
			return "redirect:/auth/user/withdrawal";
		}
		session.invalidate();
		return "redirect:/auth/user/login";
	}
}
