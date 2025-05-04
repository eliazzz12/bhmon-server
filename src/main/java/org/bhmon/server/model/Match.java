package org.bhmon.server.model;

import org.bhmon.server.model.exceptions.InvalidStolenMonException;
import org.bhmon.server.model.exceptions.MonNotFoundException;
import org.bhmon.server.model.exceptions.MonStolenException;
import org.bhmon.server.model.mon.MonGenerator;
import org.bhmon.server.model.mon.Pokemon;
import org.bhmon.server.model.user.Action;
import org.bhmon.server.model.user.Player;
import org.bhmon.server.model.user.User;

import java.io.IOException;
import java.util.*;

import static org.bhmon.codes.Codes.*;
import static org.bhmon.codes.Codes.Results.*;
import static org.bhmon.codes.Codes.Types.FIRE;
import static org.bhmon.codes.Codes.Types.WATER;
import static org.bhmon.codes.Codes.Weather.RAIN_WEATHER;
import static org.bhmon.codes.Codes.Weather.SUN_WEATHER;

public class Match implements Runnable{
    // TODO campo id
    private final int turns, pointsToWin;
    private Weather weather;
    private final Player p1, p2;
    private Player matchWinner, battleWinner, turnPlayer;
    private Stack<Pokemon> stack;
    private final Stack<Pokemon> STACK_INITIAL;
    private Pokemon drawedMon;
    private boolean rejected, stolen;
    private Queue<Action> actions;


    public Match(User u1, User u2, int turns, int pointsToWin) throws IOException {
        this.turns = turns;
        this.pointsToWin = pointsToWin;
        p1 = new Player(u1);
        p2 = new Player(u2);
        STACK_INITIAL = MonGenerator.getMons();
        stack = STACK_INITIAL;
        turnPlayer = selectRandomPlayer();
        rejected = false;
        weather = Weather.NO_WEATHER;
    }

    @Override
    public void run() {
        boolean matchEnded = false;
        try {
            while (!matchEnded) { // Jugar batallas mientras no se termine la partida
                p1.reset();
                p2.reset();
                resetBattle();

                // Sacar el primer Pokemon de cada jugador
                drawMon(turnPlayer);
                drawMon(otherPlayer(turnPlayer));

                playTurns(turns * 2);
                battle();
                sendBattleWinnerInfo();
                matchEnded = checkMatchEnded();
            }
            matchWinner = determineMatchWinner();
            sendMatchWinnerInfo();
            saveMatchData();
        } catch(IOException e){
            throw new RuntimeException();
        }
    }

    private void playTurns(int turns) throws IOException {
        for(int i=0; i<turns; i++){
            do{
                drawMon(turnPlayer);
                Action action = turnPlayer.getAction();
                execute(action);
                actions.add(action);
            } while(rejected);
            changeTurn();
        }
    }

    private void setActiveAbilities(){

    }

    private void battle() throws IOException {
        // Activar habilidades activas
        activateActiveAbilities();

        // Activar habilidades pasivas
        activatePasiveAbilities();

        // Comprobar el tiempo (sumar/restar a los de agua y fuego)
        checkWeather();

        // Combatir (Comparar power de ambos mon)
        int p1Power = p1.getBattleMon().getPower();
        int p2Power = p2.getBattleMon().getPower();
        if(p1Power > p2Power){
            battleWinner = p1;
        } else if(p2Power > p1Power){
            battleWinner = p2;
        } else {
            battleWinner = null;
        }
    }

    private void execute(Action action){
        PlayerActions move = playerActionsMap.getKey(action.getMove());
        rejected = false;
        try {
            switch (move) {
                case PlayerActions.SET_BATTLE_MON:
                    Pokemon newTeamMon = turnPlayer.replaceBattleMon(drawedMon, true);
                    otherPlayer(turnPlayer).replaceBattleMon(drawedMon, false);
                    turnPlayer.addToTeam(newTeamMon, true);
                    otherPlayer(turnPlayer).addToTeam(newTeamMon, false);
                    break;
                case PlayerActions.ADD_TO_TEAM:
                    turnPlayer.addToTeam(drawedMon, true);
                    otherPlayer(turnPlayer).addToTeam(drawedMon, false);
                    break;
                case PlayerActions.REJECT:
                    rejected = true;
                    turnPlayer.reject(true);
                    otherPlayer(turnPlayer).reject(false);
                    break;
                case PlayerActions.SET_ACTIVE_MON:
                    turnPlayer.setActiveAbilityMon(boardElementsMap.getKey(action.getTarget()));
                    break;
            }
        } catch(IOException e){
            throw new RuntimeException();
        }
    }

    private void drawMon(Player player) throws IOException {
        drawedMon = stack.pop();
        player.drawMon(monsMap.getKey(drawedMon.getNumber()));
    }

    private void activateActiveAbilities() throws IOException {
        Pokemon p1ActiveMon = p1.getActiveAbilityMon();
        Pokemon p2ActiveMon = p2.getActiveAbilityMon();
        Player first = null, second = null;

        if(p1ActiveMon != null && p2ActiveMon != null){
            if(p1ActiveMon.getNumber() >= p2ActiveMon.getNumber()) {
                first = p1;
                second = p2;
            } else {
                first = p2;
                second = p1;
            }
        } else if(p1ActiveMon != null){
            first = p1;
        } else if(p2ActiveMon != null){
            first = p2;
        }

        try{
            activateActiveAbility(first);
            activateActiveAbility(second);
        } catch (MonStolenException _){
            activateActiveAbility(first);
        }
    }

    private void activateActiveAbility(Player p) throws IOException, MonStolenException {
        if(p != null){
            try {
                Pokemon mon = p.getActiveAbilityMon();
                mon.setActiveAbility(false);
                Mons monName = monsMap.getKey(mon.getNumber());

                switch (monName) {
                    case MEOWTH:
                        stealMon(p);
                        throw new MonStolenException();
                    case FARIGIRAF:
                        Pokemon battleMon = p.getBattleMon();
                        p1.updateMonPower(battleMon, 2, isSamePlayer(p, p1));
                        p2.updateMonPower(battleMon, 2, isSamePlayer(p, p2));
                        break;
                    case PELIPPER:
                        p1.updateWeather(RAIN_WEATHER);
                        p2.updateWeather(RAIN_WEATHER);
                        break;
                    case TORKOAL:
                        p1.updateWeather(SUN_WEATHER);
                        p2.updateWeather(SUN_WEATHER);
                        break;
                    case DELIBIRD:
                        p1.updatePoints(1, isSamePlayer(p, p1));
                        p2.updatePoints(1, isSamePlayer(p, p2));
                        break;
                }
            } catch (MonNotFoundException _){}
        }
    }

    private void stealMon(Player trainer) throws IOException, InvalidStolenMonException, MonStolenException {
        BoardElements target = trainer.getTarget();
        int index = boardElementsMap.get(target);
        if(index < 1 || index > 3){
            throw new InvalidStolenMonException();
        }

        // Mostrar visualmente
        BoardElements stolenMonElement = boardElementsMap.getKey(index);
        Pokemon stolenMon = otherPlayer(trainer).removeMon(stolenMonElement, true);
        trainer.addToTeam(stolenMon, true);
        otherPlayer(trainer).addToTeam(stolenMon, false);
    }

    private void activatePasiveAbilities() throws IOException {
        // Se activan de menor a mayor número
        List<Pokemon> monsInBattle = new ArrayList<>();
        monsInBattle.addAll(List.of(p1.getTeam()));
        monsInBattle.addAll(List.of(p2.getTeam()));
        Collections.sort(monsInBattle);

        for(Pokemon mon : monsInBattle){
            Player trainer = (p1.hasMon(mon) ? p1 : p2);
            Mons monNumber = monsMap.getKey(mon.getNumber());

            // Marcar como activo al mon
            int boardElemId = trainer.getMonIndex(mon);
            trainer.getSender().setActiveMon(boardElementsMap.getKey(boardElemId), true);
            otherPlayer(trainer).getSender().setActiveMon(boardElementsMap.getKey(boardElemId), false);

            Pokemon battleMon;
            switch(monNumber){
                case TAUROS:
                    // El pokémon combatiente del rival recibe un -1
                    battleMon = otherPlayer(trainer).getBattleMon();
                    trainer.updateMonPower(battleMon, -1, false);
                    otherPlayer(trainer).updateMonPower(battleMon, -1, true);
                    break;
                // Kingambit tiene habilidad pasiva pero se activa sola si es necesario //TODO mostrar activo cuando sucede
                case AZUMARRILL:
                    // Azumarrill tiene un +1
                    battleMon = trainer.getBattleMon();
                    trainer.updateMonPower(battleMon, 1, true);
                    otherPlayer(trainer).updateMonPower(battleMon, 1, false);
                    break;
                case FUECOCO:
                    // Restablece los cambios en el poder del rival
                    battleMon = otherPlayer(trainer).getBattleMon();
                    int dif = battleMon.getNumber() - battleMon.getPower();
                    trainer.updateMonPower(battleMon, dif, false);
                    otherPlayer(trainer).updateMonPower(battleMon, dif, true);
            }
        }

    }

    private void checkWeather() throws IOException {
        // Indicar a los clientes que se está comprobando el tiempo
        p1.getSender().setActiveElement(BoardElements.WEATHER);
        p2.getSender().setActiveElement(BoardElements.WEATHER);

        checkWeatherEffect(p1);
        checkWeatherEffect(p2);


    }

    public void checkWeatherEffect(Player p) throws IOException {
        Pokemon battleMon = p.getBattleMon();
        Types bmonType = battleMon.getType();
        int dif = 0;

        switch(weather){
            case SUN_WEATHER -> {
                if(bmonType == FIRE){
                    dif = 1;
                } else if(bmonType == WATER){
                    dif = -1;
                }
            }
            case RAIN_WEATHER -> {
                if(bmonType == WATER){
                    dif = 1;
                } else if(bmonType == FIRE){
                    dif = -1;
                }
            }
        }

        if(dif != 0){
            p1.updateMonPower(battleMon, dif, isSamePlayer(p, p1));
            p2.updateMonPower(battleMon, dif, isSamePlayer(p, p2));
        }

    }

    private boolean checkMatchEnded(){
        return p1.getPoints() >= pointsToWin || p2.getPoints() >= pointsToWin;
    }

    private Player determineMatchWinner(){
        Player winner = null;
        if(p1.getPoints() == p2.getPoints()){
            winner = null;
        } else{
            if(p1.getPoints() >= pointsToWin){
                winner = p1;
            } else if(p2.getPoints() >= pointsToWin){
                winner = p2;
            }
        }
        return winner;
    }

    private void sendBattleWinnerInfo() throws IOException {
        if(battleWinner == p1){
            p1.setBattleResult(Results.YOU);
            p2.setBattleResult(Results.RIVAL);
        } else if(battleWinner == p2){
            p2.setBattleResult(Results.YOU);
            p1.setBattleResult(Results.RIVAL);
        } else if(battleWinner == null){
            p1.setBattleResult(Results.TIE);
            p2.setBattleResult(Results.TIE);
        }
    }

    private void sendMatchWinnerInfo() throws IOException {
        if(matchWinner == p1){
            p1.getSender().setMatchWinner(YOU);
            p2.getSender().setMatchWinner(RIVAL);
        } else if(matchWinner == p2){
            p2.getSender().setMatchWinner(YOU);
            p1.getSender().setMatchWinner(RIVAL);
        } else if(matchWinner == null){
            p1.getSender().setMatchWinner(TIE);
            p2.getSender().setMatchWinner(TIE);
        }
    }

    private void changeTurn(){
        turnPlayer = (turnPlayer ==p1 ? p2 : p1);
    }

    private Player otherPlayer(Player p){
        return (p == p1 ? p2 : p1);
    }

    private boolean isSamePlayer(Player p1, Player p2){
        return p1 == p2;
    }

    private Player selectRandomPlayer(){
        Player player;
        double random = (Math.random()*100);
        if(random < 50){
            player = p1;
        } else {
            player = p2;
        }
        return player;
    }

    private void resetBattle(){
        rejected = false;
        stolen = false;
    }

    private void saveMatchData(){

    }
}
