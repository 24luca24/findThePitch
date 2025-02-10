package com.fl.findthepitch;

import com.fl.findthepitch.controller.ServerRunner;
import com.fl.findthepitch.view.MainView;
import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        // Start the server in a separate thread
        Thread serverThread = new Thread(new ServerRunner());
        serverThread.setDaemon(true);
        serverThread.start();

        // Launch JavaFX Application
        Application.launch(MainView.class, args);
    }
}