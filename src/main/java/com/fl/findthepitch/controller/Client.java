package com.fl.findthepitch.controller;

import java.io.IOException;

public class Client implements Runnable {

        @Override
        public void run() {
            try {
                // Connect to the server at localhost on port 8999
                ServerConnection.connectToServer("localhost", 8999);
                System.out.println("ClientThread: Successfully connected to the server.");

                // Optionally, you can send a test command once connected:
                // For example, sending a "HELLO" command without additional data:
                // String response = ServerConnection.sendCommand("HELLO", null);
                // System.out.println("Server response: " + response);

                // If you need to continuously interact with the server,
                // consider adding a loop here, or have further methods to handle communication.

            } catch (IOException ex) {
                System.err.print("Error: " + ex.getMessage());
            }
        }
    }

