package org.bhmon.server.io;

import java.io.DataOutputStream;
import java.io.IOException;

public class OutputSender {
    public static final int YOUR_TEAM = 1;
    private static final int RIVAL_TEAM = 2;

    private static final int LOGIN_STATUS = 100;
    private static final int REGISTER_STATUS = 101;

    private static final int UPDATE_BATTLE_MON = 201;
    private static final int NEW_TEAM_MON = 202;
    private static final int REMOVE_MON = 203;
    private static final int UPDATE_MON_POWER = 204;
    private static final int SET_ACTIVE_ABILITY = 205;

    private static final int DRAW_MON = 301;
    private static final int REJECT_DRAWED_MON = 302;
    private static final int SET_ACTIVE_ELEMENT = 303;
    private static final int UPDATE_WEATHER = 304;


    private static final int ADD_POINTS = 401;
    private static final int BATTLE_WINNER = 402;
    private static final int MATCH_WINNER = 403;

    private DataOutputStream out;


    public OutputSender(DataOutputStream out){
        setOut(out);
    }

    /**
     * Envia al cliente la respuesta a su intento de login
     * @param success si ha tenido éxito el login
     */
    public void sendLoginStatus(boolean success) throws IOException {
        out.write(LOGIN_STATUS);
        out.writeBoolean(success);
    }

    /**
     * Envia al cliente la respuesta a su intento de registro
     * @param success si ha tenido éxito el registro
     */
    public void sendRegisterStatus(boolean success) throws IOException {
        out.write(REGISTER_STATUS);
        out.writeBoolean(success);
    }

    /**
     * Envia al cliente la información para establecer un nuevo battle mon
     * @param id el id del nuevo Pokemon
     * @param yours si el nuevo battle mon es tuyo o no
     */
    public void updateBattleMon(int id, boolean yours) throws IOException {
        out.write(UPDATE_BATTLE_MON);
        out.write(id);
        out.write((yours ? YOUR_TEAM : RIVAL_TEAM));
    }

    /**
     * Envia al cliente la información para añadir un nuevo mon a un equipo
     * @param id el id del nuevo Pokemon
     * @param yours si el nuevo mon es tuyo o no
     */
    public void addTeamMon(int id, boolean yours) throws IOException {
        out.write(NEW_TEAM_MON);
        out.write(id);
        out.write((yours ? YOUR_TEAM : RIVAL_TEAM));
    }

    /**
     * Envia al cliente la información para quitar un mon de un equipo
     * @param id el id del Pokemon
     * @param yours si el mon a quitar es tuyo o no
     */
    public void removeMon(int id, boolean yours) throws IOException {
        out.write(REMOVE_MON);
        out.write(id);
        out.write((yours ? YOUR_TEAM : RIVAL_TEAM));
    }

    /**
     * Envia al cliente la información para cambiar el número de un battle mon
     * @param dif el cambio en el número del Pokemon
     * @param yours si el mon es tuyo o no
     */
    public void updateMonPower(int dif, boolean yours) throws IOException {
        out.write(UPDATE_MON_POWER);
        out.write(dif);
        out.write((yours ? YOUR_TEAM : RIVAL_TEAM));
    }

    /**
     * Envia al cliente la información para indicar que un pokémon tiene su habilidad activa
     * @param index el índice del Pokemon
     */
    public void setActiveAbility(int index) throws IOException {
        out.write(SET_ACTIVE_ABILITY);
        out.write(index);
    }

    /**
     * Envia al cliente la información del pokemon sacado de la pila
     * @param number el número del nuevo Pokemon
     */
    public void drawMon(int number) throws IOException {
        out.write(DRAW_MON);
        out.write(number);
    }

    /**
     * Envia al cliente la información para indicar que se ha rechazado el Pokémon sacado
     * @param yours si el nuevo battle mon es tuyo o no
     */
    public void reject(boolean yours) throws IOException {
        out.write(REJECT_DRAWED_MON);
        out.write((yours ? YOUR_TEAM : RIVAL_TEAM));
    }

    /**
     * Envia al cliente la información para indicar que un pokémon tiene su habilidad activa
     * @param id el id del Pokemon (-1 si se trata de la activación del clima)
     * @param yours si el nuevo battle mon es tuyo o no
     */
    public void setActiveElement(int id, boolean yours) throws IOException {
        out.write(SET_ACTIVE_ELEMENT);
        out.write(id);
        out.write((yours ? YOUR_TEAM : RIVAL_TEAM));
    }

    /**
     * Envia al cliente la información para indicar que un elemento (No pokémon) está activo
     * @param id el id del Elemento
     */
    public void setActiveElement(int id) throws IOException {
        out.write(SET_ACTIVE_ELEMENT);
        out.write(id);
        out.write(0);
    }

    /**
     * Envia al cliente la información para establecer un nuevo clima
     * @param weather el código del nuevo clima
     */
    public void updateWeather(int weather) throws IOException {
        out.write(UPDATE_WEATHER);
        out.write(weather);
    }

    /**
     * Envia al cliente los puntos que ha ganado de un jugador
     * @param points los puntos a añadir al jugador
     * @param yours si los puntos son tuyos o no
     */
    public void addPoints(int points, boolean yours) throws IOException {
        out.write(ADD_POINTS);
        out.write(points);
        out.write((yours ? YOUR_TEAM : RIVAL_TEAM));
    }

    /**
     * Envia al cliente la información para establecer el ganador del combate
     * @param winner 1 si el jugador es el ganador, 2 si el jugador rival es el ganador, 0 en caso de empate
     */
    public void setBattleWinner(int winner) throws IOException {
        out.write(BATTLE_WINNER);
        out.write(winner);
    }

    /**
     * Envia al cliente la información para establecer el ganador de la partida
     * @param winner 1 si el jugador es el ganador, 2 si el jugador rival es el ganador, 0 en caso de empate
     */
    public void setMatchWinner(int winner) throws IOException {
        out.write(MATCH_WINNER);
        out.write(winner);
    }

    public void setOut(DataOutputStream out) {
        if(out == null) {
            throw new IllegalArgumentException("Data output stream cannot be null");
        }
        this.out = out;
    }
}
