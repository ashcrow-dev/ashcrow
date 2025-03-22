package server.ashcrow.common;

import java.io.IOException;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import server.ashcrow.dao.MemberDao;
import server.ashcrow.dto.MemberDto;
import server.ashcrow.dto.ResultCodeMessageDto;

@SuppressWarnings("null")
@Slf4j
@Component
public class Interceptor implements HandlerInterceptor {

    @Autowired
    MemberDao memberDao;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean runNextStep = true;
        
        String uri = request.getRequestURI();
        log.info("URI = {}", uri);
        HttpSession httpSession = request.getSession(false);
        if("/member/login".equals(uri) || "/member/signup".equals(uri)) {
            if(isLoginUser(httpSession)) {
                String message = "잘못된 접근 차단. 로그인 상태에서 실행 불가.";
                log.info(message);

                sendErrorResponse(response, message);
                
                runNextStep = false;
            }
        } else {
            if(!isLoginUser(httpSession)) {
                String message = "비로그인 사용자 요청 차단. 로그인 페이지로 리다이렉트.";
                log.info(message);
                
                sendErrorResponse(response, message);

                runNextStep = false;
            } else {
                MemberDto loginUser = (MemberDto) httpSession.getAttribute("loginUser");
                memberDao.updateLastAccessDateTime(loginUser);
            }
        }

        return runNextStep;
    }

    private boolean isLoginUser(HttpSession httpSession) {
        boolean isLoginUser = false;
        if(httpSession != null
         && httpSession.getAttribute("loginUser") != null) {
            isLoginUser = true;
        }

        return isLoginUser;
    }
    
    private void sendErrorResponse(HttpServletResponse response, String message) {
        ResultCodeMessageDto resultCodeMessageDto = new ResultCodeMessageDto();
        resultCodeMessageDto.setResultCode(-1);
        resultCodeMessageDto.setResultMessage(message);

        JSONObject json = new JSONObject();
        json.put("response", resultCodeMessageDto.toMap());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try {
            response.getWriter().write(json.toString());
        } catch (IOException e) {
            log.error("응답 작성 중 오류 발생: {}", e.getMessage());
        }
    }
}
