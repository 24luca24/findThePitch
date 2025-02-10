package com.fl.findthepitch.controller;
import java.io.*;
import java.net.Socket;

public class ServerSlave extends Thread {

    private Socket clientSocket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public ServerSlave(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new ObjectOutputStream(clientSocket.getOutputStream());

            while (true) {
                System.out.println("Waiting for client command...");
                String command = (String) in.readObject();  // Read the client's operation request
                System.out.println("Received: " + command);

                switch (command) {
                    case "LOGIN":

                        break;
                    case "REGISTER":

                        break;
                    case "EXIT":
                        System.out.println("Client disconnected.");
                        return;
                    default:
                        out.writeObject("UNKNOWN_COMMAND");
                        out.flush();
                        System.err.println("Unknown command received: " + command);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeConnections();
        }
    }

    //Close all resources when the client disconnects
    private void closeConnections() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (clientSocket != null) clientSocket.close();
            System.out.println("Client socket closed.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

