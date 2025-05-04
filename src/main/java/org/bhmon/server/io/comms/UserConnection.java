package org.bhmon.server.io.comms;

import org.bhmon.server.model.user.Action;
import org.bhmon.server.model.user.User;

import java.io.IOException;
import java.util.Set;

import static org.bhmon.codes.Codes.Requests;
import static org.bhmon.codes.Codes.Requests.*;
import static org.bhmon.codes.Codes.userActionsMap;

public class UserConnection extends ClientConnection implements Runnable{
    InputReceiver receiver;
    OutputSender sender;

    public UserConnection(ClientConnection cc){
        super(cc);
        sender = user.getSender();
        receiver = user.getReceiver();
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()){
            try {
                sender.request(Requests.REQUEST_MENU_ACTION);
                Action action = receiver.getAction();

                handleAction(action);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void handleAction(Action action) throws IOException {
        switch (userActionsMap.getKey(action)){
            case REQUEST_ONLINE_USERS -> requestOnlineUsers();
            case LOGOUT -> logout();
            case CHANGE_IMG -> changeImg(action.getTarget());
            case CHANGE_PASSWORD -> changePassword();
            case SEND_MATCH_REQUEST -> sendMatchRequest();
            case CANCEL_MATCH_REQUEST -> cancelMatchRequest();
            case REQUEST_RECEIVED_REQUESTS -> requestReceivedRequests();
            case ACCEPT_GAME_REQUEST -> acceptGameRequest();
            case REJECT_GAME_REQUEST -> rejectGameRequest();
        }
    }

    public void requestOnlineUsers(){
        Set<String> userList = users.keySet();
        sender.sendOnlineUsers(userList);
    }

    public void logout(){
        users.remove(user);
        Thread.currentThread().interrupt();
    }

    public void changeImg(int img){
        user.setPicture(img);
        dbManager.updatePicture(img);
    }

    public void changePassword() throws IOException {
        sender.request(REQUEST_STR_PASS);
        String newPass = receiver.readString();
        user.setPassword(newPass);
        dbManager.updatePassword(newPass);
    }

    public void sendMatchRequest() throws IOException {
        sender.request(REQUEST_STR_USERNAME);
        String requestedUser = receiver.readString();

    }
    public void cancelMatchRequest(){

    }
    public void requestReceivedRequests(){

    }
    public void acceptGameRequest(){

    }
    public void rejectGameRequest(){

    }

    public void setUser(User user) {
        if(user == null){
            throw new IllegalArgumentException();
        }
        this.user = user;
    }




    public void getUserList(){
        dbManager.getUserList();
    }
}
