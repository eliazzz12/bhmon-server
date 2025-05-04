package org.bhmon.server.model.user;

import static org.bhmon.codes.Codes.*;

import org.bhmon.server.model.exceptions.MonNotFoundException;
import org.bhmon.server.model.exceptions.NoRejectionsLeftException;
import org.bhmon.server.model.mon.Pokemon;

import java.io.IOException;

public class Player extends User {
    private int points, rejections;
    private Pokemon[] team = new Pokemon [5];

    public Player(User user) throws IOException {
        super(user.getReceiver(), user.getSender(), user.getUsername(), user.getPassword(), user.getPicture());
        setPoints(0);
        rejections = 3;
    }

    public Action getAction() throws IOException {
        sender.request(Requests.REQUEST_GAME_ACTION);
        Action action = receiver.getAction();
        action.setPlayer(this);
        return action;
    }

    /**
     * Establece un nuevo battle mon y devuelve el anterior battle mon para añadirlo al equipo
     * @param mon El nuevo battlemon
     * @param yours si está en tu equipo o no
     * @return el anterior battle mon
     * @throws IOException
     */
    public Pokemon replaceBattleMon(Pokemon mon, boolean yours) throws IOException {
        Pokemon newTeamMon = team[0];

        sender.updateBattleMon(monsMap.getKey(mon.getNumber()), yours);
        if(yours){
            team[0] = mon;
            return newTeamMon;
        }

        return null;
    }

    public void setActiveAbilityMon(BoardElements mon) throws IOException {
        int index = boardElementsMap.get(mon);
        if(index >= 0 && index < 5){
            team[index].setActiveAbility(true);
            sender.setActiveAbility(mon);
        }
    }

    public void addToTeam(Pokemon mon, boolean yours) throws IOException {
        for(int i=0; i< team.length; i++){
            if(team[i] == null){
                team[i] = mon;
                break;
            }
        }
        sender.addTeamMon(monsMap.getKey(mon.getNumber()), yours);
    }

    /**
     * Elimina el Pokemon del equipo
     * @param target el elemento en el tablero
     */
    public Pokemon removeMon(BoardElements target, boolean yours) throws IOException {
        sender.removeMon(target, yours);
        int index = boardElementsMap.get(target);
        Pokemon removedMon = team[index];
        removeIndex(index);
        return removedMon;
    }

    private void removeIndex(int index){
        team[index] = null;
    }

    public int getMonIndex(Pokemon mon) throws MonNotFoundException{
        for(int i=0; i<team.length; i++){
            if(team[i].equals(mon)){
                return i;
            }
        }
        throw new MonNotFoundException();
    }

    public void updateMonPower(Pokemon mon, int dif, boolean yours) throws IOException {
        int newDif = mon.updatePower(dif);
        if(dif == newDif){
            sender.updateMonPower(dif, yours);
        } else { // Es kingambit activando su habilidad
            sender.updateMonPower(dif, yours);
            sender.updateMonPower(newDif, yours);
        }
    }

    public void updateWeather(Weather weather) throws IOException {
        sender.updateWeather(weather);
    }

    public int getPoints() {
        return points;
    }

    public Pokemon[] getTeam(){
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

    public void setBattleResult(Results result) throws IOException {
        sender.setBattleWinner(result);
    }

    public void drawMon(Mons number) throws IOException {
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
        team = new Pokemon[5];
        rejections = 3;
    }

    public BoardElements getTarget() throws IOException {
        sender.request(Requests.REQUEST_TARGET);
        return receiver.getTarget();
    }

    public boolean hasMon(Pokemon mon) {
        for (Pokemon pokemon : team) {
            if (pokemon.equals(mon)) {
                return true;
            }
        }
        return false;
    }

    public Pokemon getBattleMon() {
        return team[0];
    }

    public Pokemon getActiveAbilityMon() throws MonNotFoundException{
        for (Pokemon pokemon : team) {
            if (pokemon.isActiveAbility()) {
                return pokemon;
            }
        }
        throw new MonNotFoundException();
    }
}
