package com.motyldrogi.bot.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.stream.Stream;

import reactor.core.publisher.Flux;

public class IRCConnection {
    
    private String host;
    private int port;
    private Socket sock;
    private PrintWriter out;
    private BufferedReader in;
    private String channel;

    Flux<String> messages;

    public IRCConnection(String host, int port, String channel) {
        this.host = host;
        this.port = port;
        this.channel = channel;
    }

    /**
     * Connect to the IRC server and create a Flux to stream messages to
     * any subscribers
     * @return True if able to connect. Otherwise, return false.
     */
    public boolean connect() {
        try {
            this.sock = new Socket(this.host, this.port);
            this.out = new PrintWriter(sock.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            Stream<String> inputStream = this.in.lines();
            this.messages = Flux.fromStream(inputStream);
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
            return false;
        }

        return true;
    }

    public boolean isConnected() {
        return this.sock.isConnected();
    }

    public Flux<String> getMessagesFlux() {
        return this.messages;
    }

    public void send(String message) {
        this.out.println(message);
    }

    public void sendMessage(String message) {
        this.send("PRIVMSG #" + this.channel + " :" + message);
    }

    public void joinChannel() {
        this.send("JOIN #" + this.channel);
        System.out.println("Joined to #" + this.channel);
    }
}