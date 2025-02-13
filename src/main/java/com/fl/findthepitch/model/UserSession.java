package com.fl.findthepitch.model;

public class UserSession {
    private static UserSession instance;
    private UserData userData;
    
    private UserSession() { }
    
    public static UserSession getInstance() {
        if(instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public void clearSession() {
        userData = null;
    }
}
