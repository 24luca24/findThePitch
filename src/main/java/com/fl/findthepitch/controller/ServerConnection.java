package com.fl.findthepitch.controller;

import java.io.*;
import java.net.Socket;

public class ServerConnection {

    private static Socket socket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    //Initialize the server connection (only called once)
    public static void connectToServer(String serverAddress, int serverPort) throws IOException {
        if (socket == null || socket.isClosed()) {
            socket = new Socket(serverAddress, serverPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server.");
        }
    }

    //Method to send command to server
    public static String sendCommand(String command, Object data) throws IOException, ClassNotFoundException {
        if (socket == null || socket.isClosed()) {
            throw new IOException("Socket is not connected.");
        }

        System.out.println("Sending command and data to the server");
        out.writeObject(command);
        out.writeObject(data);
        out.flush();

        // Add this to ensure the server is responding
        System.out.println("Waiting for the response from the server...");
        try {
            Object response = in.readObject();
            if (response instanceof String) {
                System.out.println("Received response from server: " + response);
                return (String) response;
            } else {
                throw new IOException("Unexpected response from server.");
            }
        } catch (EOFException e) {
            // Handle unexpected EOF
            System.out.println("EOFException: Server closed connection unexpectedly.");
            throw new IOException("Connection to the server was closed unexpectedly.");
        }
    }


    //Method to close the connection when done
    public static void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Connection closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
