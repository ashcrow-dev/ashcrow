package server.ashcrow.security;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CryptographyAES implements Cryptography {
    private static final CryptographyAES instance = new CryptographyAES();
    private CryptographyAES() {}
    public static CryptographyAES getInstance() {
        return instance;
    }

    @Value("${AES_SECRET_KEY}")
    private static String AES_SECRET_KEY;  //환경변수 이용
    private static final int AES_KEY_SIZE = 256; // 128, 192, 256 bit 크기 == 16, 24, 32 byte 길이
    private static final String AES_ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    @Override
    public boolean validateKey(byte[] key) {
        boolean validYn = key.length == AES_KEY_SIZE / 8;
        return validYn;
    }

    @Override
    public SecretKey generateSecretKey() {
        SecretKey secretKey = null;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
            keyGenerator.init(AES_KEY_SIZE);
    
            secretKey = keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            log.warn(e.getMessage());
        }
        return secretKey;
    }

    @Override
    public SecretKey generateSecretKeySpec() {
        return new SecretKeySpec(AES_SECRET_KEY.getBytes(), AES_ALGORITHM);
    }

    @Override
    public IvParameterSpec generateInitializationVector() {
        byte[] iv = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(iv);

        return new IvParameterSpec(iv);
    }

    @Override
    public String encrypt(String plainString) {
        SecretKey secretKey = generateSecretKeySpec();
        return encrypt(plainString, secretKey);
    }

    @Override
    public String encrypt(String plainString, SecretKey secretKey) {
        IvParameterSpec iv = generateInitializationVector();
        return encrypt(plainString, secretKey, iv);
    }

    @Override
    public String encrypt(String plainString, SecretKey secretKey, IvParameterSpec iv) {
        byte[] plainBytes = plainString.getBytes();
        byte[] encryptedBytes = encrypt(plainBytes, secretKey, iv);
        String encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
        String ivString = Base64.getEncoder().encodeToString(iv.getIV());

        return String.join(":", ivString, encryptedString);
    }

    @Override
    public String decrypt(String encryptedStringWithIv) {
        SecretKey secretKey = generateSecretKeySpec();
        return decrypt(encryptedStringWithIv, secretKey);
    }

    @Override
    public String decrypt(String encryptedStringWithIv, SecretKey secretKey) {
        String[] parts = encryptedStringWithIv.split(":");
        String ivString = parts[0];
        String encryptedString = parts[1];

        byte[] ivBytes = Base64.getDecoder().decode(ivString);
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        return decrypt(encryptedString, secretKey, iv);
    }

    @Override
    public String decrypt(String encryptedString, SecretKey secretKey, IvParameterSpec iv) {
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedString);

        byte[] decryptedBytes = decrypt(decodedBytes, secretKey, iv);
        String decryptedString = new String(decryptedBytes);

        return decryptedString;
    }

    @Override
    public byte[] encrypt(byte[] plainBytes, SecretKey secretKey, IvParameterSpec iv) {
        byte[] encryptedBytes = null;
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
    
            encryptedBytes = cipher.doFinal(plainBytes);
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            log.warn(e.getMessage());
        }

        return encryptedBytes;
    }

    @Override
    public byte[] decrypt(byte[] decodedBytes, SecretKey secretKey, IvParameterSpec iv) {
        byte[] decryptedBytes = null;
        try {
            Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            
            decryptedBytes = cipher.doFinal(decodedBytes);
            
        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            log.warn(e.getMessage());
        }
        
        return decryptedBytes;
    }
}
