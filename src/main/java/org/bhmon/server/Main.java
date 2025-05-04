package org.bhmon.server;

import org.bhmon.server.io.comms.ClientConnection;
import org.bhmon.server.model.user.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    private static final Map<String, User> users = new ConcurrentHashMap<>();
    public static void main(String[] args) {
        final int port = 10120;
        try(ServerSocket serverSocket = new ServerSocket(port)){
            while(!Thread.currentThread().isInterrupted()) {
                try {
                    Socket socket = serverSocket.accept();
                    new Thread(new ClientConnection(socket, users)).start();
                } catch (IOException e) {
                    System.out.println("Client connection error");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
