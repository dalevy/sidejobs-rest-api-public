package com.sidejobs.api.util;

import java.util.function.Function;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptPasswordUtil {

    public String hash(String password, int rounds) {
        return BCrypt.hashpw(password, BCrypt.gensalt(rounds));
    }
    public boolean verifyHash(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }
}