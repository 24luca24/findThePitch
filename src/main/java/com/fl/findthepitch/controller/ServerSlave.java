package com.fl.findthepitch.controller;
import com.fl.findthepitch.model.UserData;

import java.io.*;
import java.net.Socket;

public class ServerSlave extends Thread {

    private final Socket clientSocket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    public ServerSlave(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.out = new ObjectOutputStream(clientSocket.getOutputStream());
        this.out.flush();
        this.in = new ObjectInputStream(clientSocket.getInputStream());

    }

    @Override
    public void run() {
        try {
            System.out.println("Client connected. Ready to receive commands.");
            while (true) {
                System.out.println("Waiting for client command...");
                String command = (String) in.readObject();
                System.out.println("Received: " + command);

                switch (command) {

                    case "REGISTER":
                        UserData userData = (UserData) in.readObject();
                        boolean registrationSuccessful = dbManager.registerUser(userData);
                        if (registrationSuccessful) {
                            out.writeObject("SUCCESS");
                            out.flush();
                        } else {
                            out.writeObject("FAIL");
                        }
                        break;

                    default:
                        out.writeObject("UNKNOWN_COMMAND");
                        out.flush();
                        System.err.println("Unknown command received: " + command);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Errore nel serverSlave");
        } finally {
            closeConnections();
        }
    }

    //Close all resources when the client disconnects
    // In ServerSlave class
    private void closeConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            System.out.println("Client socket closed.");
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

}

