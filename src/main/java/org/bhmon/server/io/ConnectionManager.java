package org.bhmon.server.io;

import org.bhmon.server.model.user.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

public class ConnectionManager implements Runnable{
    private static final int LOGIN = 1;
    private static final int REGISTER = 2;


    private User user;
    private Socket socket;
    private Map<String, User> users;
    private boolean loggedIn = false;


    public ConnectionManager(Socket socket, Map<String, User> users) throws IOException {
        setSocket(socket);
    }

    @Override
    public void run() {
        while(!loggedIn){
            try {
                user = readUser(socket);
            } catch (IOException _){
                Thread.currentThread().interrupt();
            }
        }
        new Thread(new UserConnection(user)).start();
        users.put(user.getUsername(), user);
        notifyLogin();
        while(!Thread.currentThread().isInterrupted()) {
        }
    }

    private static User readUser(Socket socket) throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        InputReceiver receiver = new InputReceiver(new DataInputStream(socket.getInputStream()));
        OutputSender sender = new OutputSender(new DataOutputStream(socket.getOutputStream()));

        int action = in.readInt();
        String username = InputReceiver.readString(in);
        String password = InputReceiver.readString(in);
        int picture = in.readInt();
        User user = new User(receiver, sender, username, password, picture);

        switch(action){
            case LOGIN -> attemptLogin(user);
            case REGISTER -> attemptRegister(user);
        }

        return user;
    }

    private static void attemptLogin(User user){
        // TODO hacer log in
    }

    private static void attemptRegister(User user){
        // TODO hacer registro
    }

    private static void notifyLogin(){
//        user.getSender()
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
