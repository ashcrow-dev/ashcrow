package server.ashcrow.common;

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
            if(httpSession != null && httpSession.getAttribute("loginUser") != null) {
                String message = "잘못된 접근 차단. 로그인 상태에서 실행 불가.";
                log.info(message);

                ResultCodeMessageDto resultCodeMessageDto = new ResultCodeMessageDto();
                resultCodeMessageDto.setResultCode(-1);
                resultCodeMessageDto.setResultMessage(message);

                JSONObject json = new JSONObject();
                json.put("response", resultCodeMessageDto.toMap());

                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html");
                response.getWriter().write(json.toString());
                
                runNextStep = false;
            }

        } else {
            if(httpSession == null || httpSession.getAttribute("loginUser") == null) {
                String message = "비로그인 사용자 요청 차단. 로그인 페이지로 리다이렉트.";
                log.info(message);
                
                ResultCodeMessageDto responseDto = new ResultCodeMessageDto();
                responseDto.setResultCode(-1);
                responseDto.setResultMessage(message);
                
                JSONObject json = new JSONObject();
                json.put("response", responseDto.toMap());
                
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/html");
                response.getWriter().write(json.toString());
                runNextStep = false;
            } else {
                MemberDto loginUser = (MemberDto) httpSession.getAttribute("loginUser");
                memberDao.updateLastAccessDateTime(loginUser);
            }
        }

        return runNextStep;
    }

}
