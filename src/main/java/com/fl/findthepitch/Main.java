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

        // Add a delay to allow the server to start before the client tries to connect
        try {
            Thread.sleep(2000); // Wait for 2 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread interrupted while waiting for server startup.");
        }

        // Start the first client in a separate thread
        Thread clientThread1 = new Thread(new Client());
        clientThread1.setDaemon(true);
        clientThread1.start();

        // Launch JavaFX Application
        Application.launch(MainView.class, args);
    }
}

