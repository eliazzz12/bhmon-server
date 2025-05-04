package org.bhmon.server.model.user;

import org.bhmon.server.io.comms.InputReceiver;
import org.bhmon.server.io.comms.OutputSender;

import java.io.IOException;
import java.util.Objects;

public class User {
    protected InputReceiver receiver;
    protected OutputSender sender;
    protected String username;
    protected String password;
    protected int picture;

    public User(InputReceiver receiver, OutputSender sender, String username, String password, int picture){
        setReceiver(receiver);
        setSender(sender);
        setUsername(username);
        setPassword(password);
        setPicture(picture);
    }

    public String readString() throws IOException {
        return receiver.readString();
    }

    public InputReceiver getReceiver() {
        return receiver;
    }

    public void setReceiver(InputReceiver receiver) {
        this.receiver = receiver;
    }

    public OutputSender getSender() {
        return sender;
    }

    public void setSender(OutputSender sender) {
        this.sender = sender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiver, sender, username, password, picture);
    }
}
