package server.ashcrow.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashingSHA256String implements Hashing {
    private static final HashingSHA256String instance = new HashingSHA256String();
    private HashingSHA256String() {}
    public static HashingSHA256String getInstance() {
        return instance;
    }

    private static final String SHA256_ALGORITHM = "SHA-256";
    private static final String HEX_FORMAT = "%02x";

    @Override
    public String execute(String raw) {
        String hash = null;
        
        if(raw != null && !raw.isEmpty()) {
            try {
                MessageDigest digest = MessageDigest.getInstance(SHA256_ALGORITHM);
                byte[] encodedHash = digest.digest(raw.getBytes());
                
                // byte[]을 16진수 문자열로 변환
                StringBuilder hexString = new StringBuilder();
                for(byte b : encodedHash) {
                    String hex = String.format(HEX_FORMAT, b);
                    hexString.append(hex);
                }
                
                hash = hexString.toString();
            } catch (NoSuchAlgorithmException e) {
                log.warn(e.getMessage());
            }
        }
        
        return hash;
    }

    @Override
    public boolean verify(String raw, String hash) {
        return execute(raw).equals(hash);
    }
}
