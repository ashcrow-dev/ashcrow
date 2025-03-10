package server.ashcrow.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class HashingArgon2 implements Hashing {
    private static final HashingArgon2 instance = new HashingArgon2();
    private HashingArgon2() {}
    public static HashingArgon2 getInstance() {
        return instance;
    }
    
    @Override
    public String execute(String raw) {
        Argon2 argon2 = Argon2Factory.create();
        String hashedString = argon2.hash(3, 65536, 2, raw.getBytes());

        return hashedString;
    }

    @Override
    public boolean verify(String raw, String hash) {
        return Argon2Factory.create().verify(hash, raw.toCharArray());
    }
}
