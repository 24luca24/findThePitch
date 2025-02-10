package com.fl.findthepitch.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static int PORT = 8999;
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException{
        try {
            serverSocket = new ServerSocket(PORT, 50, InetAddress.getByName("localhost"));
            System.out.println("Server started on port " + PORT);

            while (true) {
                System.out.println("Waiting for client...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected");

                // Assign new connection to a ServerSlave thread
                new ServerSlave(clientSocket).start();
            }
        } finally {
            serverSocket.close();
            }
    }
}
