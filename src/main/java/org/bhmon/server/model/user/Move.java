package org.bhmon.server.model.user;

public class Move {
    // Actions
    public static final int SET_BATTLE_MON = 1;
    public static final int ADD_TO_TEAM = 2;
    public static final int REJECT = 3;
    public static final int SET_ACTIVE_MON = 4;
    public static final int STEAL_MON = 5;

    private Player player;
    private int action;
    private int target;


    public Move(int action, int target){
        setAction(action);
        setTarget(target);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }
}
