package com.fl.findthepitch.controller;

import com.fl.findthepitch.model.FirebaseConnection;

import java.io.IOException;

public class FirebaseManager {


    public FirebaseManager() {
        try {
            FirebaseConnection.initializeFirebase();
        } catch (IOException e) {
            System.err.println("Error initializing Firebase: " + e.getMessage());
        }
    }
}
