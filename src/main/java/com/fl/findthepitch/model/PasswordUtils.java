package com.fl.findthepitch.model;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

        // Method to hash the password
        public static String hashPassword(String password) {
            return BCrypt.hashpw(password, BCrypt.gensalt());
        }

        // Method to check if the entered password matches the stored hashed password
        public static boolean checkPassword(String enteredPassword, String storedHashedPassword) {
            return BCrypt.checkpw(enteredPassword, storedHashedPassword);
        }
    }

