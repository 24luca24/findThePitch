package com.fl.findthepitch.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static int PORT = 8999;
    private static ServerSocket serverSocket;

    public static Server() {

        JLabel titolo = new JLabel("Termina servizio"); //JLabel che crea il titolo nella finestra
        JLabel testo = new JLabel("Spegni server"); //JLabel contente il motto dell'applicazione
        JButton poweroff = new JButton("Power OFF"); //permette di cercare le canzoni
        poweroff.addActionListener(this::actionListenerpoweroff);
    }

    private void actionListenerpoweroff(){
        try {
            serverSocket.close();
        } catch (IOException ex) {
            System.err.println("Error during shutdown of server: " + ex.getMessage());
        }
        System.out.println("Server Power off");
        System.exit(0);

    }

    public static void main(String[] args)throws IOException {
        serverSocket= new ServerSocket(PORT);
        System.out.println("Server started");
        try{
            while(true){
                Socket s = serverSocket.accept();
                System.out.println("Server accetta connessioni");
                new ServerSlave(s,credenziali).start();
            }

        }finally{
            serverSocket.close();
        }
    }
}






