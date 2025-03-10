package server.ashcrow.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Data;

@Data
public class MemberDto implements Dto {
    private long memberSerial;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberNickname;
    private LocalDateTime creationDateTime;
    private LocalDateTime lastAccessDateTime;
    private boolean delYn;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("memberSerial", this.memberSerial);
        map.put("memberId", this.memberId);
        map.put("memberPw", this.memberPw);
        map.put("memberName", this.memberName);
        map.put("memberNickname", this.memberNickname);
        map.put("creationDateTime", this.creationDateTime);
        map.put("lastAccessDateTime", this.lastAccessDateTime);
        map.put("delYn", this.delYn);
        
        return map;
    }

    @Override
    public boolean validate() {
        boolean isValid = true;

        String[] requiredElements = {"memberId", "memberPw", "memberName"};
        Map<String, Object> map = this.toMap();

        for(String key : requiredElements) {
            Optional<Object> checker = Optional.ofNullable(map.get(key));
            if(checker.isEmpty()) {
                isValid = false;
                break;
            }
        }
        
        return isValid;
    }
}
