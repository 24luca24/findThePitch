package com.fl.findthepitch.controller;

public class ServerRunner implements Runnable {

    @Override
    public void run() {
        Server.main(new String[]{});
    }
}