package com.fl.findthepitch.controller;


public class SessionManager {
    private static String currentUsername;

    public static void setCurrentUsername(String username) {
        currentUsername = username;
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static void clearUsername() {
        currentUsername = null;
    }
}