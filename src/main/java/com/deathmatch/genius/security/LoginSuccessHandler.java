package com.deathmatch.genius.security;

import lombok.extern.log4j.Log4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// 로그인 성공시 작동 하는 Handler
@Log4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("현재 유저 권한 " + authentication.getAuthorities());
        final List<String> authNames = new ArrayList<String>();

        authentication.getAuthorities().forEach(authority -> {
            authNames.add(authority.getAuthority());
        });         // Split으로 쪼개면, 안되는 현상(?), lambda 활용


        if(authNames.contains("ROLE_ADMIN")){
            log.info("Admin 계정 입니다");
            response.sendRedirect("/notice");
            return;
        }

        if(authNames.contains("ROLE_MEMBER")){
            log.info("Member 계정 입니다");
            response.sendRedirect("/rooms");
            return;
        }
    }
}