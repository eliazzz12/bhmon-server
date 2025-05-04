package org.bhmon.server.io.comms;

import org.bhmon.server.model.user.Action;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.bhmon.codes.Codes.BoardElements;

public class InputReceiver {
    private DataInputStream in;

    public InputReceiver(DataInputStream in){
        setIn(in);
    }

    public static String readString(DataInputStream in) throws IOException {
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.read(bytes, 0, length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public String readString() throws IOException {
        return readString(in);
    }

    public int readInt() throws IOException {
        return in.readInt();
    }

    public Action getAction() throws IOException {
        Action action;
        int actionId = in.readInt();
        int target = in.readInt();
        String data = readString();
        action = new Action(actionId, target, data);
        return action;
    }

    public BoardElements getTarget(){
        BoardElements target;
        // TODO solicitar y leer target
        throw new UnsupportedOperationException();

//        return target;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }
}