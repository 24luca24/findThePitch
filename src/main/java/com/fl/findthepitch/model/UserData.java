package com.fl.findthepitch.model;

public class UserData {

    private String name;
    private String surname;
    private String username;
    private String email;
    private String hashPassword;
    private String city;
    private String googleID;

    //Constructor using mail and password
    public UserData(String name, String surname, String username, String email, String hashPassword, String city) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.hashPassword = hashPassword;
        this.city = city;
        this.googleID = null;
    }

    //Constructor using googleID
    public UserData(String name, String surname, String username, String email, Integer age, String googleID) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.hashPassword = null;
        this.city = city;
        this.googleID = googleID;
    }

    //Constructor for login
    public UserData(String username, String password) {
        this.username = username;
        this.hashPassword = password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return email;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public String getCity() {
        return city;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMail(String mail) {
        this.email = email;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public void setAge(String city) {
        this.city = city;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }
}
