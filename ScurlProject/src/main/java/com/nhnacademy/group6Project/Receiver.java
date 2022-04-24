package com.nhnacademy.group6Project;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class Receiver {
    private Socket socket;
    private InputStream in;

    public Receiver(Socket socket) throws IOException {
        this.socket = socket;

        in = socket.getInputStream();
    }

    public InputStream getIn() {
        return in;
    }
}
