package com.deathmatch.genious.interceptor;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.deathmatch.genious.domain.UserDTO;

@Log4j
public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	private static final String LOGIN = "login";

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HttpSession session = request.getSession();

        ModelMap modelMap = modelAndView.getModelMap();
        Object userDTO = modelMap.get("userDTO");
        log.info("Post Handle");

        if(userDTO != null){
            log.info("Login Success!");
            session.setAttribute("login",userDTO);
            UserDTO currentDTO = (UserDTO)session.getAttribute("login");
            String currentUser = currentDTO.getUserEmail();
            log.info("currentUser: "+ currentUser);
            response.sendRedirect("/user/gameHome");
        }
    }

    // 기존에 정보가 있는 경우에 삭제한다
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        HttpSession session = request.getSession();
        log.info("preHandle");
        if(session.getAttribute("login")!=null){
            session.invalidate();
            log.info("LOGOUT");
        }
        return true;
    }
	

}
