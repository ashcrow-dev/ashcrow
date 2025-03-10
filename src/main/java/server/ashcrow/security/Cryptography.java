package server.ashcrow.security;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public interface Cryptography {

    /**
     * 키 유효성 검사(키 크기 확인)
     * @param key
     * @return
     */
    boolean validateKey(byte[] key);
    
    /**
     * 새로운 비밀키 생성
     * @return
     */
    SecretKey generateSecretKey();
    
    /**
     * 고정값 비밀키 생성
     * @return
     */
    SecretKey generateSecretKeySpec();
    
    /**
     * 초기화 벡터 생성
     * @return
     */
    IvParameterSpec generateInitializationVector();
    
    /**
     * @param plainString 원문
     * @return iv를 포함한 암호문
     */
    String encrypt(String plainString);
    
    /**
     * @param plainString 원문
     * @param secretKey 생성한 secret key
     * @return iv를 포함한 암호문
     */
    String encrypt(String plainString, SecretKey secretKey);
    
    /**
     * @param plainString 원문
     * @param secretKey 생성한 secret key
     * @param iv 생성한 초기화 벡터 Initialization Vector
     * @return iv를 포함한 암호문
     */
    String encrypt(String plainString, SecretKey secretKey, IvParameterSpec iv);
    
    /**
     * @param encryptedStringWithIv iv를 포함한 암호문
     * @return 원문
     */
    String decrypt(String encryptedStringWithIv);
    
    /**
     * @param encryptedStringWithIv iv를 포함한 암호문
     * @param secretKey 생성한 secret key
     * @return 원문
     */
    String decrypt(String encryptedStringWithIv, SecretKey secretKey);
    
    /**
     * @param encryptedString 암호문
     * @param secretKey 생성한 secret key
     * @param iv 암호문의 초기화 벡터 Initialization Vector
     * @return 원문
     */
    String decrypt(String encryptedString, SecretKey secretKey, IvParameterSpec iv);
    
    /**
     * @param plainBytes 원본 파일의 바이트 배열
     * @param secretKey 생성한 secret key
     * @param iv 생성한 초기화 벡터 Initialization Vector
     * @return 암호화된 파일의 바이트 배열
     */
    byte[] encrypt(byte[] plainBytes, SecretKey secretKey, IvParameterSpec iv);
    
    /**
     * @param decodedBytes 암호화 파일의 디코딩된 바이트 배열
     * @param secretKey 생성한 secret key
     * @param iv 암호화된 파일의 초기화 벡터 Initialization Vector
     * @return 원본 파일의 바이트 배열
     */
    byte[] decrypt(byte[] decodedBytes, SecretKey secretKey, IvParameterSpec iv);
}
