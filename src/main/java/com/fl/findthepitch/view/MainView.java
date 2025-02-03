package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.FirebaseManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainView extends Application {

    @Override
    public void start(Stage primaryStage) {
        FirebaseManager fb = new FirebaseManager();
        primaryStage.setTitle("Find The Pitch");
        primaryStage.show();
    }
}
