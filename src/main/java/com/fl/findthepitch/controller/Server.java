package com.fl.findthepitch.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static int PORT = 8999;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName("localhost"));
            System.out.println("Server started on port " + PORT);
            dbManager db = new dbManager();
            db.createUserTable();
            db.createPitchTable();
            db.createMunicipalityTable();
            db.uploadDataFromCSV("src/main/resources/CitiesCoordinates/gi_comuni_cap.csv");



            while (true) {
                System.out.println("Waiting for client...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                new ServerSlave(clientSocket).start();  // FIX: Run in a separate thread
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                    System.out.println("Server socket closed.");
                }
            } catch (IOException e) {
                System.err.println("Error closing server socket: " + e.getMessage());
            }
        }
    }

}
