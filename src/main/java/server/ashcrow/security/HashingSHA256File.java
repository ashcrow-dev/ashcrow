package server.ashcrow.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashingSHA256File implements Hashing {
    private static final HashingSHA256File instance = new HashingSHA256File();
    private HashingSHA256File() {}
    public static HashingSHA256File getInstance() {
        return instance;
    }
    
    private static final String SHA256_ALGORITHM = "SHA-256";
    private static final String HEX_FORMAT = "%02x";

    /**
     * @param filePath file path
     * @return hashed file
     */
    @Override
    public String execute(String filePath) {
        String hash = null;

        try {
            MessageDigest digest = MessageDigest.getInstance(SHA256_ALGORITHM);

            try (FileInputStream fis = new FileInputStream(filePath)) {
                long fileSize = new File(filePath).length();
                int bufferSize = fileSize > 10485760 ? 1048576 : 65536;  //버퍼크기(byte) : 파일크기 10 MB 초과 시 1 MB, 그 외 64 KB

                byte[] buffer = new byte[bufferSize];
                int bytesRead;
                while((bytesRead = fis.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                log.warn(e.getMessage());
            }

            byte[] hashBytes = digest.digest();

            try(Formatter formatter = new Formatter()) {
                for(byte b : hashBytes) {
                    formatter.format(HEX_FORMAT, b);
                }
                hash = formatter.toString();
            }
            
        } catch (NoSuchAlgorithmException e) {
            log.warn(e.getMessage());
        }
        
        return hash;
    }

    /**
     * compare files
     * @param filePath1 file path
     * @param filePath2 another file path
     * @return if equals 'true', if different 'false'
     */
    @Override
    public boolean verify(String filePath1, String filePath2) {
        return execute(filePath1).equals(execute(filePath2));
    }

    
}