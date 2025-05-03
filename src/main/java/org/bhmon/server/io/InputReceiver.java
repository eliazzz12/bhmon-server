package org.bhmon.server.io;

import org.bhmon.server.model.user.Move;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class InputReceiver {
    private DataInputStream in;
    private Move move;

    public InputReceiver(DataInputStream in){
        setIn(in);
    }

    public static String readString(DataInputStream in) throws IOException {
        int length = in.readInt();
        byte[] bytes = new byte[length];
        in.read(bytes, 0, length);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public Move getMove() {
        Move move;
        try {
            int action = in.readInt();
            int target = in.readInt();
            move = new Move(action, target);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return move;
    }

    public int getTarget(){
        int target = 0;
        // TODO solicitar y leer target

        return target;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }
}