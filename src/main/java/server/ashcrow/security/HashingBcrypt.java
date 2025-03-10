package server.ashcrow.security;

import java.security.SecureRandom;

import org.mindrot.jbcrypt.BCrypt;

public class HashingBcrypt implements Hashing {
    private static final HashingBcrypt instance = new HashingBcrypt();
    private HashingBcrypt() {}
    public static HashingBcrypt getInstance() {
        return instance;
    }
    
    @Override
    public String execute(String raw) {
        int log_rounds = 12;   //10 ~ 14 권장
        SecureRandom random = new SecureRandom();

        String salt = BCrypt.gensalt(log_rounds, random);
        String hashedString = BCrypt.hashpw(raw, salt);
        
        return hashedString;
    }

    @Override
    public boolean verify(String raw, String hash) {
        return BCrypt.checkpw(raw, hash);
    }
}