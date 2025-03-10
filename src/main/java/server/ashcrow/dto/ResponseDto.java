package server.ashcrow.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class ResponseDto implements Dto {
    private int resultCode;
    private String resultMessage;

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();

        map.put("resultCode", this.resultCode);
        map.put("resultMessage", this.resultMessage);
        
        return map;
    }

    @Override
    public boolean validate() {
        return true;
    }
}
