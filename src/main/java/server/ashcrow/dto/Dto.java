package server.ashcrow.dto;

import java.util.Map;

public interface Dto {
    Map<String, Object> toMap();
    boolean validate();
}
