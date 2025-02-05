package com.fl.findthepitch.model;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    //Hash a password before storing
    public static String hashPassword(String password) {
        //Ensure correct bcrypt hashing
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    //Check if the entered password matches the stored hash
    public static boolean checkPassword(String enteredPassword, String storedHashedPassword) {
        if (storedHashedPassword == null || storedHashedPassword.length() < 10) {
            System.err.println("Invalid stored password hash.");
            return false;
        }
        return BCrypt.checkpw(enteredPassword, storedHashedPassword);
    }
}

