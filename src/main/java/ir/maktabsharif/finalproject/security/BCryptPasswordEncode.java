package ir.maktabsharif.finalproject.security;


import at.favre.lib.crypto.bcrypt.BCrypt;

public class BCryptPasswordEncode {

    public static String encodeBcryptPassword(String password) {

        BCrypt.Hasher hasher = BCrypt.withDefaults();

        return hasher.hashToString(12, password.toCharArray());

    }

    public static Boolean validateBcryptPassword(String password, String hashedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedPassword.toCharArray());
        return result.verified;
    }
}
