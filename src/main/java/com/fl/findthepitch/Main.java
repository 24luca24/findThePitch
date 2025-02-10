package com.fl.findthepitch;

import com.fl.findthepitch.controller.Client;
import com.fl.findthepitch.controller.ServerRunner;
import com.fl.findthepitch.view.MainView;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        // Start the server in a separate thread
        Thread serverThread = new Thread(new ServerRunner());
        serverThread.setDaemon(true);
        serverThread.start();

        // Start the first client in a separate thread
        Thread clientThread1 = new Thread(new Client());
        clientThread1.setDaemon(true);
        clientThread1.start();

        Thread clientThread2 = new Thread(new Client());
        clientThread2.setDaemon(true);
        clientThread2.start();

        // Launch JavaFX Application
        Application.launch(MainView.class, args);
    }
}
