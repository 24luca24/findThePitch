package com.fl.findthepitch.model;

public class UserData {

    private String name;
    private String surname;
    private String username;
    private String mail;
    private String hashPassword;
    private Integer age;
    private String googleID;

    //Constructor using mail and password
    public UserData(String name, String surname, String username, String mail, String hashPassword, Integer age) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.mail = mail;
        this.hashPassword = hashPassword;
        this.age = age;
        this.googleID = null;
    }

    //Constructor using googleID
    public UserData(String name, String surname, String username, String mail, Integer age, String googleID) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.mail = mail;
        this.hashPassword = null;
        this.age = age;
        this.googleID = googleID;
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
        return mail;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public int getAge() {
        return age;
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
        this.mail = mail;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }
}
