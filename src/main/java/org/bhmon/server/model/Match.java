package org.bhmon.server.model;

import org.bhmon.server.model.mon.MonGenerator;
import org.bhmon.server.model.mon.Pokemon;
import org.bhmon.server.model.user.Move;
import org.bhmon.server.model.user.Player;
import org.bhmon.server.model.user.User;

import java.io.IOException;
import java.util.*;

import static org.bhmon.server.model.mon.Pokemon.*;

public class Match implements Runnable{
    public static final int TIE = 0;
    public static final int YOU = 1;
    public static final int RIVAL = 2;

    private final static int WEATHER = 100;
    public final static int NO_WEATHER = 101;
    public final static int SUN_WEATHER = 102;
    public final static int RAIN_WEATHER = 103;


    // TODO campo id
    private final int turns, pointsToWin;
    private int weather;
    private final Player p1, p2;
    private Player matchWinner, battleWinner, turnPlayer;
    private Stack<Pokemon> stack;
    private final Stack<Pokemon> STACK_INITIAL;
    private Pokemon drawedMon;
    private boolean rejected, stolen;
    private Queue<Move> moves;


    public Match(User u1, User u2, int turns, int pointsToWin) throws IOException {
        this.turns = turns;
        this.pointsToWin = pointsToWin;
        p1 = new Player(u1);
        p2 = new Player(u2);
        STACK_INITIAL = MonGenerator.getMons();
        stack = STACK_INITIAL;
        turnPlayer = selectRandomPlayer();
        rejected = false;
        weather = NO_WEATHER;
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
                Move move = turnPlayer.getMove();
                execute(move);
                moves.add(move);
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

    private void execute(Move move){
        int action = move.getAction();
        rejected = false;

        try {
            switch (action) {
                case Move.SET_BATTLE_MON:
                    turnPlayer.setBattleMon(drawedMon, true);
                    otherPlayer(turnPlayer).setBattleMon(null, false);
                    break;
                case Move.ADD_TO_TEAM:
                    turnPlayer.addToTeam(drawedMon, true);
                    otherPlayer(turnPlayer).addToTeam(null, false);
                    break;
                case Move.REJECT:
                    rejected = true;
                    turnPlayer.reject(true);
                    otherPlayer(turnPlayer).reject(false);
                    break;
                case Move.SET_ACTIVE_MON:
                    turnPlayer.setActiveAbilityMon(move.getTarget());
                    break;
            }
        } catch(IOException e){
            throw new RuntimeException();
        }
    }

    private void drawMon(Player player) throws IOException {
        drawedMon = stack.pop();
        player.drawMon(drawedMon.getNumber());
    }

    private void activateActiveAbilities() throws IOException {
        Pokemon p1ActiveMon = p1.getActiveAbilityMon();
        Pokemon p2ActiveMon = p2.getActiveAbilityMon();

        if(p1ActiveMon != null && p2ActiveMon != null){
            // Se activa primero la del pokemon con número mayor
            if(p1ActiveMon.getNumber() >= p2ActiveMon.getNumber()){
                activateActiveAbility(p1);
                if(stolen){
                    if(p1.getActiveAbilityMon() != null){
                        activateActiveAbility(p1);
                    }
                } else {
                    activateActiveAbility(p2);
                }
            } else {
                activateActiveAbility(p2);
                if(stolen){
                    if(p2.getActiveAbilityMon() != null){
                        activateActiveAbility(p2);
                    }
                } else {
                    activateActiveAbility(p1);
                }
            }
        } else if(p1ActiveMon != null){
            activateActiveAbility(p1);
        } else if(p2ActiveMon != null){
            activateActiveAbility(p2);
        }
    }

    private void activateActiveAbility(Player p) throws IOException {
        Pokemon mon = p.getActiveAbilityMon();
        if(mon != null){
            int index = p.getActiveAbilityMonIndex();
            p1.activateActiveMon(index, isSamePlayer(p, p1));
            p2.activateActiveMon(index, isSamePlayer(p, p2));

            switch(mon.getNumber()){
                case MEOWTH_NUM:
                    stolen = true;
                    stealMon(mon);
                    break;
                case FARIGIRAF_NUM:
                    p1.updateMonPower(2, isSamePlayer(p, p1));
                    p2.updateMonPower(2, isSamePlayer(p, p2));
                    break;
                case PELIPPER_NUM:
                    p1.updateWeather(RAIN_WEATHER);
                    p2.updateWeather(RAIN_WEATHER);
                    break;
                case TORKOAL_NUM:
                    p1.updateWeather(SUN_WEATHER);
                    p2.updateWeather(SUN_WEATHER);
                    break;
                case DELIBIRD_NUM:
                    p1.updatePoints(1, isSamePlayer(p, p1));
                    p2.updatePoints(1, isSamePlayer(p, p2));
                    break;
            }
            mon.setActiveAbility(false);
        }
    }

    private void stealMon(Player trainer){
        int target = trainer.getTarget();


        Pokemon stolenMon = otherPlayer(turnPlayer).removeMon(target);
        turnPlayer.addToTeam(stolenMon);

        stolen = true;
    }

    private void activatePasiveAbilities() throws IOException {
        // Se activan de menor a mayor número
        List<Pokemon> monsInBattle = new ArrayList<>(p1.getTeam());
        monsInBattle.addAll(p2.getTeam());
        Collections.sort(monsInBattle);

        for(Pokemon mon : monsInBattle){
            int number = mon.getNumber();
            Player trainer = (p1.getTeam().contains(mon) ? p1 : p2);

            switch(number){
                case TAUROS_NUM:
                    // El pokémon combatiente del rival recibe un -1
                    trainer.getSender().setActiveElement(trainer.getIndex(mon));

                    trainer.updateMonPower(-1, false);
                    otherPlayer(trainer).updateMonPower(-1, true);
                    break;
                // Kingambit tiene habilidad pasiva pero se activa sola si es necesario
                case AZUMARRILL_NUM:
                    // Azumarrill tiene un +1
                    trainer.updateMonPower(1, true);
                    otherPlayer(trainer).updateMonPower(1, false);
                    break;
                case FUECOCO_NUM:
                    // Restablece los cambios en el poder del rival
                    Pokemon rivalMon = otherPlayer(trainer).getBattleMon();
                    int dif = rivalMon.getNumber() - rivalMon.getPower();
                    trainer.updateMonPower(dif, false);
                    otherPlayer(trainer).updateMonPower(dif, true);
            }
        }

    }

    private void checkWeather() throws IOException {
        // Indicar a los clientes que se está comprobando el tiempo
        try {
            p1.getSender().setActiveElement(WEATHER);
            p2.getSender().setActiveElement(WEATHER);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        checkWeatherEffect(weather, p1);
        checkWeatherEffect(weather, p2);


    }

    public void checkWeatherEffect(int weather, Player p) throws IOException {
        Pokemon.types bmonType = p.getBattleMon().getType();
        int dif = 0;

        switch(weather){
            case SUN_WEATHER -> {
                if(bmonType == Pokemon.types.FIRE){
                    dif = 1;
                } else if(bmonType == Pokemon.types.WATER){
                    dif = -1;
                }
            }
            case RAIN_WEATHER -> {
                if(bmonType == Pokemon.types.WATER){
                    dif = 1;
                } else if(bmonType == Pokemon.types.FIRE){
                    dif = -1;
                }
            }
        }

        if(dif != 0){
            p1.updateMonPower(dif, isSamePlayer(p, p1));
            p2.updateMonPower(dif, isSamePlayer(p, p2));
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
            p1.setBattleResult(YOU);
            p2.setBattleResult(RIVAL);
        } else if(battleWinner == p2){
            p2.setBattleResult(YOU);
            p1.setBattleResult(RIVAL);
        } else if(battleWinner == null){
            p1.setBattleResult(TIE);
            p2.setBattleResult(TIE);
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
