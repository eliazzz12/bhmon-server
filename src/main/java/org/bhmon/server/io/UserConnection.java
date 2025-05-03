package org.bhmon.server.io;

import org.bhmon.server.model.user.User;

public class UserConnection implements Runnable{
//    private static final int

    private User user;
    InputReceiver receiver;
    OutputSender sender;

    public UserConnection(User user){
        setUser(user);
        sender = this.user.getSender();
        receiver = this.user.getReceiver();
    }

    @Override
    public void run() {

    }

    public void setUser(User user) {
        if(user == null){
            throw new IllegalArgumentException();
        }
        this.user = user;
    }
}
