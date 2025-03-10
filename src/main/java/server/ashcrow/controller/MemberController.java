package server.ashcrow.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import server.ashcrow.action.MemberAction;
import server.ashcrow.dto.MemberDto;
import server.ashcrow.dto.ResponseDto;

@Slf4j
@CrossOrigin(allowCredentials = "true", originPatterns = { "http://localhost:5173" })
@RestController
public class MemberController {
    @Autowired
    private MemberAction memberAction;

    @PostMapping("/member/signup")
    public String signup(@RequestBody MemberDto paramMemberDto) {
        log.info(paramMemberDto.toString());

        
        ResponseDto responseDto = memberAction.signup(paramMemberDto);
        
        JSONObject resultJsonObj = new JSONObject();
        resultJsonObj.put("responseDto", responseDto.toMap());

        return resultJsonObj.toString();
    }

    @PostMapping("/member/login")
    public String login(@RequestBody MemberDto paramMemberDto, HttpSession httpSession) {
        log.info(paramMemberDto.toString());

        ResponseDto responseDto = memberAction.login(paramMemberDto, httpSession);

        JSONObject resultJsonObj = new JSONObject();
        resultJsonObj.put("response", responseDto.toMap());

        return resultJsonObj.toString();
    }
}
