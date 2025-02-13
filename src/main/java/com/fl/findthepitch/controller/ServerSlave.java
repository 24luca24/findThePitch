package com.fl.findthepitch.controller;
import com.fl.findthepitch.model.PitchData;
import com.fl.findthepitch.model.UserData;

import java.io.*;
import java.net.Socket;

public class ServerSlave extends Thread {

    private final Socket clientSocket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;


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
                        try {
                            UserData userData = (UserData) in.readObject();
                            boolean registrationSuccessful = dbManager.registerUser(userData);
                            String response = registrationSuccessful ? "SUCCESS" : "FAIL";
                            out.writeObject(response);
                            out.flush();
                        } catch (Exception e) {
                            callException("REGISTER", e);
                        }
                        break;

                    case "LOGIN":
                        try {
                            UserData userData = (UserData) in.readObject();
                            boolean loginSuccessful = dbManager.validateLogin(userData.getUsername(), userData.getHashPassword());
                            String response = loginSuccessful ? "SUCCESS" : "FAIL";
                            out.writeObject(response);
                            out.flush();
                        } catch (Exception e) {
                            callException("LOGIN", e);
                        }
                        break;

                    case "CREATEPITCH":
                        //TODO: ADD LOGIC
                        try {
                            PitchData pitchData = (PitchData) in.readObject();
                            boolean creationPitchSuccessful = dbManager.createPitch(pitchData);
                            String response = creationPitchSuccessful ? "SUCCESS" : "FAIL";
                            out.writeObject(response);
                            out.flush();
                        } catch (Exception e) {
                            callException("CREATEPITCH", e);
                        }
                        break;
                    default:
                        out.writeObject("UNKNOWN_COMMAND");
                        out.flush();
                        System.err.println("Unknown command received: " + command);
                        break;
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

    private void callException(String command, Exception e) throws IOException {
        System.out.println("Error when executing: " + command + "cause this exception: " + e.getMessage());
        e.printStackTrace();
        out.writeObject("ERROR" + e.getMessage());
        out.flush();
    }


}

