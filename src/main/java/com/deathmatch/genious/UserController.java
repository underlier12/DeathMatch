package com.deathmatch.genious;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
	
	private final KakaoConnectionUtil kakaoLoginService;
    private final UserService userService;
    private final NaverLoginBO naverLoginService;

    private String apiResult = null;

    public UserController(KakaoConnectionUtil kakaoLoginService, UserService userService,NaverLoginBO naverLoginService) {
        this.kakaoLoginService = kakaoLoginService;
        this.userService = userService;
        this.naverLoginService = naverLoginService;
    }
    
    @GetMapping("/loginHome")
    public String loginHome(HttpSession session,Model model){
        // 이러면 또 문제 발생할수도 있음
        /*if(session != null){
            session.invalidate();
        }*/
        String naverAuthUrl = naverLoginService.getAuthorizationUrl(session);
        log.info("naver :" +naverAuthUrl);
        model.addAttribute("url",naverAuthUrl);
        return "user/loginHome";
    }
    
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response ,HttpSession session){
        Object currentObj = session.getAttribute("login");
        if(currentObj != null){
            UserDTO userDTO = (UserDTO)currentObj;
            session.removeAttribute("login");
            session.invalidate();
        }
        return "index";
    }
    
    @GetMapping("/login")
    public void loginGet(@ModelAttribute("loginDTO")LoginDTO loginDTO){

    }
    
    @PostMapping("/loginPost")
    public void loginPost(LoginDTO loginDTO , HttpSession session, Model model){
        UserDTO userDTO = userService.login(loginDTO);
        if(userDTO == null){
            log.info("Login User is Empty");
            return;
        }
        model.addAttribute("userDTO",userDTO);
    }
    
    @GetMapping("/kakaoLogin")
    public String kakaoLogin(@RequestParam("code") String code, Model model, HttpSession session) {
        String accessToken = kakaoLoginService.getAccessToken(code);
        UserDTO userDTO = kakaoLoginService.getUserInfo(accessToken);
        log.info("AccessToken: " + accessToken);
        log.info("login Info: " + userDTO.toString());

        // 유저 정보가 없으면 새로운 vo, 있으면 기존의 정보를 불러온다.
        userService.kakaoLogin(userDTO);
        model.addAttribute("userDTO", userDTO);
        return "gameHome";
    }
    
    @RequestMapping(value ="/naverLogin", method ={RequestMethod.GET,RequestMethod.POST})
    public String naverLogin(Model model, @RequestParam String code,@RequestParam String state,HttpSession session) throws IOException, ParseException {
        OAuth2AccessToken oauthToken;
        oauthToken = naverLoginService.getAccessToken(session, code, state);
        UserDTO naverUser = new UserDTO();
        //1. 로그인 사용자 정보를 읽어온다.
        apiResult = naverLoginService.getUserProfile(oauthToken); //String형식의 json데이터

        //2. String형식인 apiResult를 json형태로 바꿈
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(apiResult);
       /* Object obj = parser.parse(apiResult);*/
        JSONObject jsonObj = (JSONObject) obj;

        //3. 데이터 파싱
        //Top레벨 단계 _response 파싱
        JSONObject response_obj = (JSONObject) jsonObj.get("response");
        //response의 nickname값 파싱
        String nickname = (String)response_obj.get("nickname");
        String email = (String)response_obj.get("email");

        log.info("nickName : " + nickname);
        log.info("email : " + email);
        naverUser.setUserEmail(email);
        naverUser.setName(nickname);

        log.info(apiResult);
        log.info(naverUser.toString());

        //4.파싱 닉네임 세션으로 저장
       /* session.setAttribute("sessionId",nickname);
        model.addAttribute("result", apiResult);*/
        return "gameHome";
    }
    
    @GetMapping("/join")
    public String join(){
        return "user/join";
    }

    @PostMapping("/join")
    public String joinMember(UserDTO userDTO){
        userService.insertMember(userDTO);
        return "gameHome";
    }

    @GetMapping("/gameHome")
    public String GameHome(){
        return "gameHome";
    }    
	
	
}
