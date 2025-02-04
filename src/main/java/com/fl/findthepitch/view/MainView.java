package com.fl.findthepitch.view;

import com.fl.findthepitch.controller.dbManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainView extends Application {

    @Override
    public void start(Stage primaryStage) {
        dbManager db = new dbManager();
        db.createUserTable();
        db.createPitchTable();
        primaryStage.setTitle("Find The Pitch");
        primaryStage.show();
    }
}
