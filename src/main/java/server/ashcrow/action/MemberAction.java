package server.ashcrow.action;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import server.ashcrow.dao.MemberDao;
import server.ashcrow.dto.MemberDto;
import server.ashcrow.dto.ResultCodeMessageDto;
import server.ashcrow.security.Cryptography;
import server.ashcrow.security.CryptographyAES;
import server.ashcrow.security.Hashing;
import server.ashcrow.security.HashingBcrypt;

@Slf4j
@Service
public class MemberAction {

    @Autowired
    MemberDao memberDao;

    public ResultCodeMessageDto signup(MemberDto paramMemberDto) {
        String resultMessage;
        int resultCode = 0;
        if(paramMemberDto.validate()) {
            String name = paramMemberDto.getMemberName();
            String nickname = paramMemberDto.getMemberNickname();
            String pw = paramMemberDto.getMemberPw();
    
            Cryptography cryptography = CryptographyAES.getInstance();
            SecretKey key = cryptography.generateSecretKeySpec();
            String encryptedName = cryptography.encrypt(name, key);
            String encryptedNickname = cryptography.encrypt(nickname, key);
    
            Hashing hashing = HashingBcrypt.getInstance();
            String hashedPw = hashing.execute(pw);
    
            paramMemberDto.setMemberName(encryptedName);
            paramMemberDto.setMemberNickname(encryptedNickname);
            paramMemberDto.setMemberPw(hashedPw);
            
            int insertedRowCount = memberDao.signup(paramMemberDto);
            resultMessage = insertedRowCount > 0 ? "회원가입에 성공하였습니다." : "회원가입에 실패하였습니다.";
        } else {
            resultCode = -1;
            resultMessage = "필수정보를 입력하지 않았습니다.";
            log.warn(resultMessage);
        }

        ResultCodeMessageDto responseDto = new ResultCodeMessageDto();
        responseDto.setResultCode(resultCode);
        responseDto.setResultMessage(resultMessage);
        
        return responseDto;
    }

    public ResultCodeMessageDto login(MemberDto paramMemberDto, HttpSession httpSession) {
        log.info(paramMemberDto.toString());
        
        String id = paramMemberDto.getMemberId();
        
        MemberDto memberDto = memberDao.retrieveMemberById(id);
        if(memberDto != null) {
            Cryptography cryptography = CryptographyAES.getInstance();
            Hashing hashing = HashingBcrypt.getInstance();
            
            String rawPw = paramMemberDto.getMemberPw();
            String hashedPw = memberDto.getMemberPw();
            
            boolean isLoginYn = hashing.verify(rawPw, hashedPw);
            if(isLoginYn) {
                String encryptedName = memberDto.getMemberName();
                String encryptedNickname = memberDto.getMemberNickname();

                String name = cryptography.decrypt(encryptedName);
                String nickname = cryptography.decrypt(encryptedNickname);
                memberDto.setMemberName(name);
                memberDto.setMemberNickname(nickname);

                httpSession.setAttribute("loginUser", memberDto);
                httpSession.setMaxInactiveInterval(3600);   //1 hour
            } else {
                httpSession.invalidate();
            }
        }
        
        ResultCodeMessageDto resultCodeMessageDto = new ResultCodeMessageDto();
        
        return resultCodeMessageDto;
    }

}
