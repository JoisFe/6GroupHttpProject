package com.nhnacademy.group6Project;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Sender {
    private Socket socket;
    private PrintStream out;

    public Sender(Socket socket) throws IOException {
        this.socket = socket;

        this.out = new PrintStream(socket.getOutputStream());
    }

    public PrintStream getOut() {
        return this.out;
    }
}
