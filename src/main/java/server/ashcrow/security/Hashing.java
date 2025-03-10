package server.ashcrow.security;

public interface Hashing {
    /**
     * @param raw raw string
     * @return hashed string
     */
    String execute(String raw);
    
    /**
     * @param raw raw string
     * @param hash hashed string
     * @return if 'raw' and 'hash' are same, return 'true'. the other case will return 'false'
     */
    boolean verify(String raw, String hash);
}
