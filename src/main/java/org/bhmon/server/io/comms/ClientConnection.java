package org.bhmon.server.io.comms;

import org.bhmon.server.io.persistance.BHMonDAO_DB_MySQL;
import org.bhmon.server.model.user.User;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;

import static org.bhmon.codes.Codes.*;
import static org.bhmon.codes.Codes.Requests.*;

public class ClientConnection implements Runnable{
    protected User user;
    protected Socket socket;
    protected Map<String, User> users;
    protected static BHMonDAO_DB_MySQL dbManager;
    private static boolean loggedIn = false, registered = false;


    public ClientConnection(Socket socket, Map<String, User> users) throws IOException {
        setSocket(socket);
        setUsers(users);
        setDbManager();
    }

    public ClientConnection(ClientConnection cm){
        setSocket(cm.socket);
        setUsers(cm.users);
        setUser(cm.user);
        setDbManager();
    }

    @Override
    public void run() {
        while(!loggedIn){
            try {
                user = readUser(socket);
                notifyLogin(user);
            } catch (Exception _){
                Thread.currentThread().interrupt();
            }
        }
        new Thread(new UserConnection(this)).start();
        users.put(user.getUsername(), user);
        try {
            notifyLogin(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static User readUser(Socket socket) throws IOException {
        DataInputStream in = new DataInputStream(socket.getInputStream());
        InputReceiver receiver = new InputReceiver(new DataInputStream(socket.getInputStream()));
        OutputSender sender = new OutputSender(new DataOutputStream(socket.getOutputStream()));

        sender.request(REQUEST_REGIS_OR_LOGIN);
        int actionValue = in.readInt();
        UserActions userAction = userActionsMap.getKey(actionValue);

        sender.request(REQUEST_STR_USERNAME);
        String username = InputReceiver.readString(in);

        sender.request(REQUEST_STR_PASS);
        String password = InputReceiver.readString(in);

        sender.request(REQUEST_IMG);
        int picture = in.readInt();

        User user = new User(receiver, sender, username, password, picture);

        try{
            switch (userAction) {
                case LOGIN -> attemptLogin(user);
                case REGISTER -> attemptRegister(user);
            }
        } catch (Exception _){
            return null;
        }

        return user;
    }

    private static void attemptLogin(User user){
        loggedIn = dbManager.login(user);
    }

    private static void attemptRegister(User user) throws IOException {
        registered = dbManager.register(user);
        if(registered){
            notifyRegister(user);
            attemptLogin(user);
        }
    }

    private static void notifyLogin(User user) throws IOException {
        user.getSender().sendLoginStatus(loggedIn);
    }

    private static void notifyRegister(User user) throws IOException {
        user.getSender().sendRegisterStatus(registered);
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public void setUser(User user){
        this.user = user;
    }

    public void setDbManager() {
        dbManager = BHMonDAO_DB_MySQL.getInstance();
    }
}
