package org.bhmon.server.io.comms;

import org.bhmon.codes.Codes;
import org.bhmon.codes.exceptions.InvalidCodeException;

import java.io.DataOutputStream;
import java.io.IOException;

import static org.bhmon.codes.Codes.BattleOrders.*;
import static org.bhmon.codes.Codes.*;
import static org.bhmon.codes.Codes.MatchOrders.*;
import static org.bhmon.codes.Codes.Requests.REQUEST_DATA;
import static org.bhmon.codes.Codes.UserActions.LOGIN_STATUS;
import static org.bhmon.codes.Codes.UserActions.REGISTER_STATUS;

public class OutputSender {
    private DataOutputStream out;


    public OutputSender(DataOutputStream out){
        setOut(out);
    }

    public void request(Requests request) throws IOException {
        out.write(requestsMap.get(REQUEST_DATA));
        out.write(requestsMap.get(request));
    }

    /**
     * Envia al cliente la respuesta a su intento de login
     * @param success si ha tenido éxito el login
     */
    public void sendLoginStatus(boolean success) throws IOException {
        out.write(userActionsMap.get(LOGIN_STATUS));
        out.writeBoolean(success);
    }

    /**
     * Envia al cliente la respuesta a su intento de registro
     * @param success si ha tenido éxito el registro
     */
    public void sendRegisterStatus(boolean success) throws IOException {
        out.write(userActionsMap.get(REGISTER_STATUS));
        out.writeBoolean(success);
    }

    /**
     * Envia al cliente la información para establecer un nuevo battle mon
     * @param mon el número del Pokemon
     * @param yours si el nuevo battle mon es tuyo o no
     */
    public void updateBattleMon(Mons mon, boolean yours) throws IOException {
        out.write(battleOrdersMap.get(UPDATE_BATTLE_MON));
        if(yours){
            out.write(monsMap.get(mon));
        } else {
            out.write(-1);
        }
        out.writeBoolean(yours);
    }

    /**
     * Envia al cliente la información para añadir un nuevo mon a un equipo
     * @param mon el número del Pokemon
     * @param yours si el nuevo mon es tuyo o no
     */
    public void addTeamMon(Mons mon, boolean yours) throws IOException {
        out.write(battleOrdersMap.get(NEW_TEAM_MON));
        out.write(monsMap.get(mon));
        out.writeBoolean(yours);
    }

    /**
     * Envia al cliente la información para quitar un mon de un equipo
     * @param element el elemento Pokemon
     * @param yours si el mon a quitar es tuyo o no
     */
    public void removeMon(BoardElements element, boolean yours) throws IOException {
        out.write(battleOrdersMap.get(REMOVE_MON));
        out.write(boardElementsMap.get(element));
        out.writeBoolean(yours);
    }

    /**
     * Envia al cliente la información para cambiar el número de un battle mon
     * @param dif el cambio en el número del Pokemon
     * @param yours si el mon es tuyo o no
     */
    public void updateMonPower(int dif, boolean yours) throws IOException {
        setActiveMon(BoardElements.BATTLE_MON, yours);

        out.write(battleOrdersMap.get(UPDATE_BATTLE_MON_POWER));
        out.write(dif);
        out.writeBoolean(yours);
    }

    /**
     * Envia al cliente la información para indicar que un pokémon tiene su habilidad activa
     * @param mon el elemento Pokemon
     */
    public void setActiveAbility(BoardElements mon) throws IOException {
        out.write(battleOrdersMap.get(SET_ACTIVE_ABILITY));
        out.write(boardElementsMap.get(mon));
        out.writeBoolean(true); // Sólo se pueden ver las habilidades activas de tu equipo
    }

    /**
     * Envia al cliente la información del pokemon sacado de la pila
     * @param number el número del nuevo Pokemon
     */
    public void drawMon(Mons number) throws IOException {
        out.write(battleOrdersMap.get(DRAW_MON));
        out.write(monsMap.get(number));
    }

    /**
     * Envia al cliente la información para indicar que se ha rechazado el Pokémon sacado
     * @param yours si el rechazo es tuyo o no
     */
    public void reject(boolean yours) throws IOException {
        out.write(battleOrdersMap.get(REJECT_DRAWED_MON));
        out.writeBoolean(yours);
    }

    /**
     * Envia al cliente la información para indicar que un elemento Pokémon está activo
     * @param element el elemento Pokémon a marcar activo
     * @param yours si el elemento Pokémon es de tu equipo o no
     */
    public void setActiveMon(BoardElements element, boolean yours) throws IOException {
        int elementValue = boardElementsMap.get(element);
        if(elementValue < 0 || elementValue > 4){
            throw new InvalidCodeException("BoardElement has to be a Mon");
        }
        out.write(battleOrdersMap.get(SET_ACTIVE_ELEMENT));
        out.write(elementValue);
        out.writeBoolean(yours);
    }

    /**
     * Envia al cliente la información para indicar que un elemento (No pokémon) está activo
     * @param element el Elemento a marcar activo
     */
    public void setActiveElement(BoardElements element) throws IOException {
        int elementValue = boardElementsMap.get(element);
        if(elementValue < 5){
            throw new InvalidCodeException("BoardElement can't be a Mon");
        }
        out.write(battleOrdersMap.get(SET_ACTIVE_ELEMENT));
        out.write(elementValue);
    }

    /**
     * Envia al cliente la información para establecer un nuevo clima
     * @param weather el código del nuevo clima
     */
    public void updateWeather(Weather weather) throws IOException {
        out.write(battleOrdersMap.get(UPDATE_WEATHER));
        out.write(weatherMap.get(weather));
    }

    /**
     * Envia al cliente los puntos que ha ganado de un jugador
     * @param points los puntos a añadir al jugador
     * @param yours si los puntos son tuyos o no
     */
    public void addPoints(int points, boolean yours) throws IOException {
        out.write(matchOrdersMap.get(ADD_POINTS));
        out.write(points);
        out.writeBoolean(yours);
    }

    /**
     * Envia al cliente la información para establecer el ganador del combate
     * @param result el resultado del combate
     */
    public void setBattleWinner(Codes.Results result) throws IOException {
        out.write(matchOrdersMap.get(BATTLE_WINNER));
        out.write(resultsMap.get(result));
    }

    /**
     * Envia al cliente la información para establecer el ganador de la partida
     * @param result el resultado de la partida
     */
    public void setMatchWinner(Codes.Results result) throws IOException {
        out.write(matchOrdersMap.get(MATCH_WINNER));
        out.write(resultsMap.get(result));
    }

    public void setOut(DataOutputStream out) {
        if(out == null) {
            throw new IllegalArgumentException("Data output stream cannot be null");
        }
        this.out = out;
    }
}
