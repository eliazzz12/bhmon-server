package org.bhmon.server.model.user;

public class Action {
    private Player player;
    private int move;
    private int target;
    private String data;


    public Action(int move, int target, String data){
        setMove(move);
        setTarget(target);
        setData(data);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getMove() {
        return move;
    }

    public void setMove(int move) {
        this.move = move;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
