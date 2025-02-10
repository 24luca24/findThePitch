package com.fl.findthepitch.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static int PORT = 8999;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                // Assign new connection to a ServerSlave thread
                new ServerSlave(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
