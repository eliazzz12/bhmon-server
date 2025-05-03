package org.bhmon.server.model.user;

import org.bhmon.server.model.exceptions.NoRejectionsLeftException;
import org.bhmon.server.model.mon.Pokemon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.bhmon.server.io.OutputSender.*;
import static org.bhmon.server.model.Match.*;

public class Player extends User {
    private int points, rejections;
    private List<Pokemon> team;
    private Pokemon battleMon, activeAbilityMon;




    public Player(User user) throws IOException {
        super(user.getReceiver(), user.getSender(), user.getUsername(), user.getPassword(), user.getPicture());
        team = new ArrayList<>();
        setPoints(0);
        rejections = 3;
    }

    public Move getMove(){
        Move move = receiver.getMove();
        move.setPlayer(this);
        return move;
    }


    public void setBattleMon(Pokemon mon, boolean yours) throws IOException {
        if(yours){
            battleMon = mon;
        }
        int id = mon!=null ? mon.getNumber() : -1;
        sender.updateBattleMon(id, yours);
    }

    public void setActiveAbilityMon(int index) throws IOException {
        if(index >= 0){
            activeAbilityMon = team.get(index);
            activeAbilityMon.setActiveAbility(true);
            sender.setActiveAbility(index);
        }
    }

    public void addToTeam(Pokemon mon, boolean yours) throws IOException {
        team.addLast(mon);
        int id = mon!=null ? mon.getNumber() : -1;
        sender.addTeamMon(id, yours);
    }

    /**
     * Elimina el Pokemon del equipo y lo devuelve
     * @param target el índice del Pokemon dentro del equipo
     * @return el Pokemon eliminado
     */
    public Pokemon removeMon(int target, boolean yours) throws IOException {
        sender.removeMon(target, yours);
        return team.remove(target);
    }


    public Pokemon getBattleMon() {
        return battleMon;
    }

    public Pokemon getActiveAbilityMon() {
        return activeAbilityMon;
    }

    public int getActiveAbilityMonIndex(){
        return team.indexOf(activeAbilityMon);
    }

    public void updateMonPower(int dif, boolean yours) throws IOException {
        int newDif = battleMon.updatePower(dif);
        if(dif == newDif){
            sender.updateMonPower(dif, yours);
        } else {
            sender.updateMonPower(dif, yours);
            sender.updateMonPower(newDif, yours);
        }
    }

    public void updateWeather(int weather) throws IOException {
        sender.updateWeather(weather);
    }

    public int getPoints() {
        return points;
    }

    public List<Pokemon> getTeam(){
        return team;
    }

    public void updatePoints(int points, boolean yours) throws IOException {
        if(yours){
            this.points += points;
        }
        sender.addPoints(points, yours);
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setBattleResult(int winner) throws IOException {
        sender.setBattleWinner(winner);
    }

    public void drawMon(int number) throws IOException {
        sender.drawMon(number);
    }

    public void reject(boolean yours) throws IOException {
        if(yours){
            if(rejections <= 0){
                throw new NoRejectionsLeftException("Player "+username+" doesn't have any rejections left");
            }
            rejections--;
        }
        sender.reject(yours);
    }

    public void reset(){
        team = new ArrayList<>();
        battleMon = null;
        activeAbilityMon = null;
        rejections = 3;
    }

    public int getTarget() {
        return receiver.getTarget();
    }
}
