package org.bhmon.codes;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import java.util.Collections;

public abstract class Codes {

    // Enums
    public enum UserActions {
        LOGIN, REGISTER, LOGIN_STATUS, REGISTER_STATUS, REQUEST_ONLINE_USERS, CHANGE_PASSWORD, CHANGE_IMG, LOGOUT,
        SEND_MATCH_REQUEST, CANCEL_MATCH_REQUEST, REQUEST_RECEIVED_REQUESTS, ACCEPT_GAME_REQUEST, REJECT_GAME_REQUEST
    }
    public enum PlayerActions {
        SET_BATTLE_MON, ADD_TO_TEAM, REJECT, SET_ACTIVE_MON
    }
    public enum Requests{
        REQUEST_DATA, REQUEST_REGIS_OR_LOGIN, REQUEST_STR_USERNAME, REQUEST_STR_PASS, REQUEST_IMG, REQUEST_TARGET,
        REQUEST_GAME_ACTION, REQUEST_MENU_ACTION
    }
    public enum Results{
        TIE, YOU, RIVAL
    }
    public enum BoardElements {
        BATTLE_MON, MON_1, MON_2, MON_3, MON_4, WEATHER
    }
    public enum Weather {
        NO_WEATHER, SUN_WEATHER, RAIN_WEATHER
    }
    public enum BattleOrders {
        DRAW_MON, REJECT_DRAWED_MON, UPDATE_BATTLE_MON, UPDATE_BATTLE_MON_POWER, NEW_TEAM_MON, REMOVE_MON,
        SET_ACTIVE_ABILITY, SET_ACTIVE_ELEMENT, UPDATE_WEATHER
    }
    public enum MatchOrders{
        ADD_POINTS, BATTLE_WINNER, MATCH_WINNER
    }
    public enum Mons {
        DELIBIRD, TAUROS, TORKOAL, PELIPPER, FARIGIRAF, KINGAMBIT, MEOWTH, AZUMARRILL, FUECOCO
    }
    public enum Types{
        FIRE, WATER, OTHER
    }

    // BidiMaps, permiten encontrar la clave usando el valor
    public static final BidiMap<PlayerActions, Integer> playerActionsMap = initPlayerActionsMap();
    public static final BidiMap<UserActions, Integer> userActionsMap = initUserActionsMap();
    public static final BidiMap<Requests, Integer> requestsMap = initRequestsMap();
    public static final BidiMap<Results, Integer> resultsMap = initResultsMap();
    public static final BidiMap<BoardElements, Integer> boardElementsMap = initboardElementsMap();
    public static final BidiMap<Weather, Integer> weatherMap = initWeatherMap();
    public static final BidiMap<BattleOrders, Integer> battleOrdersMap = initBattleOrdersMap();
    public static final BidiMap<MatchOrders, Integer> matchOrdersMap = initMatchOrdersMap();
    public static final BidiMap<Mons, Integer> monsMap = initMonsMap();
    public static final BidiMap<Types, Integer> typesMap = initTypesMap();


    // Creación de los Maps
    private static BidiMap<UserActions, Integer> initUserActionsMap(){
        BidiMap<UserActions, Integer> map = new DualHashBidiMap<>();
        map.put(UserActions.LOGIN, 1);
        map.put(UserActions.REGISTER, 2);
        map.put(UserActions.LOGIN_STATUS, 3);
        map.put(UserActions.REGISTER_STATUS, 4);
        map.put(UserActions.REQUEST_ONLINE_USERS, 5);
        map.put(UserActions.CHANGE_PASSWORD, 6);
        map.put(UserActions.CHANGE_IMG, 7);
        map.put(UserActions.LOGOUT, 8);
        map.put(UserActions.SEND_MATCH_REQUEST, 9);
        map.put(UserActions.CANCEL_MATCH_REQUEST, 10);
        map.put(UserActions.REQUEST_RECEIVED_REQUESTS, 11);
        map.put(UserActions.ACCEPT_GAME_REQUEST, 12);
        map.put(UserActions.REJECT_GAME_REQUEST, 13);
        return (BidiMap<UserActions, Integer>) Collections.unmodifiableMap(map);
    }

    private static BidiMap<PlayerActions, Integer> initPlayerActionsMap(){
        BidiMap<PlayerActions, Integer> map = new DualHashBidiMap<>();
        map.put(PlayerActions.SET_BATTLE_MON, 1);
        map.put(PlayerActions.ADD_TO_TEAM, 2);
        map.put(PlayerActions.REJECT, 3);
        map.put(PlayerActions.SET_ACTIVE_MON, 4);
        return (BidiMap<PlayerActions, Integer>) Collections.unmodifiableMap(map);
    }

    private static BidiMap<Requests, Integer> initRequestsMap(){
        BidiMap<Requests, Integer> map = new DualHashBidiMap<>();
        map.put(Requests.REQUEST_DATA, 1);
        map.put(Requests.REQUEST_REGIS_OR_LOGIN, 2);
        map.put(Requests.REQUEST_STR_USERNAME, 3);
        map.put(Requests.REQUEST_STR_PASS, 4);
        map.put(Requests.REQUEST_IMG, 5);
        map.put(Requests.REQUEST_TARGET, 6);
        map.put(Requests.REQUEST_MENU_ACTION, 7);
        map.put(Requests.REQUEST_GAME_ACTION, 8);
        return (BidiMap<Requests, Integer>) Collections.unmodifiableMap(map);
    }

    private static BidiMap<Results, Integer> initResultsMap(){
        BidiMap<Results, Integer> map = new DualHashBidiMap<>();
        map.put(Results.TIE, 1);
        map.put(Results.YOU, 2);
        map.put(Results.RIVAL, 3);
        return (BidiMap<Results, Integer>) Collections.unmodifiableMap(map);
    }

    private static BidiMap<BoardElements, Integer> initboardElementsMap(){
        BidiMap<BoardElements, Integer> map = new DualHashBidiMap<>();
        map.put(BoardElements.BATTLE_MON, 0);
        map.put(BoardElements.MON_1, 1);
        map.put(BoardElements.MON_2, 2);
        map.put(BoardElements.MON_3, 3);
        map.put(BoardElements.MON_4, 4);
        map.put(BoardElements.WEATHER, 5);
        return (BidiMap<BoardElements, Integer>) Collections.unmodifiableMap(map);
    }

    private static BidiMap<Weather, Integer> initWeatherMap(){
        BidiMap<Weather, Integer> map = new DualHashBidiMap<>();
        map.put(Weather.NO_WEATHER, 1);
        map.put(Weather.SUN_WEATHER, 2);
        map.put(Weather.RAIN_WEATHER, 3);
        return (BidiMap<Weather, Integer>) Collections.unmodifiableMap(map);
    }

    private static BidiMap<BattleOrders, Integer> initBattleOrdersMap(){
        BidiMap<BattleOrders, Integer> map = new DualHashBidiMap<>();
        map.put(BattleOrders.DRAW_MON, 1);
        map.put(BattleOrders.REJECT_DRAWED_MON, 2);
        map.put(BattleOrders.UPDATE_BATTLE_MON, 3);
        map.put(BattleOrders.UPDATE_BATTLE_MON_POWER, 4);
        map.put(BattleOrders.NEW_TEAM_MON, 5);
        map.put(BattleOrders.REMOVE_MON, 6);
        map.put(BattleOrders.SET_ACTIVE_ABILITY, 7);
        map.put(BattleOrders.SET_ACTIVE_ELEMENT, 8);
        map.put(BattleOrders.UPDATE_WEATHER, 9);
        return (BidiMap<BattleOrders, Integer>) Collections.unmodifiableMap(map);
    }

    private static BidiMap<MatchOrders, Integer> initMatchOrdersMap(){
        BidiMap<MatchOrders, Integer> map = new DualHashBidiMap<>();
        map.put(MatchOrders.ADD_POINTS, 1);
        map.put(MatchOrders.BATTLE_WINNER, 2);
        map.put(MatchOrders.MATCH_WINNER, 3);
        return (BidiMap<MatchOrders, Integer>) Collections.unmodifiableMap(map);
    }

    private static BidiMap<Mons, Integer> initMonsMap(){
        BidiMap<Mons, Integer> map = new DualHashBidiMap<>();
        map.put(Mons.DELIBIRD, 0);
        map.put(Mons.TAUROS, 1);
        map.put(Mons.TORKOAL, 2);
        map.put(Mons.PELIPPER, 3);
        map.put(Mons.FARIGIRAF, 4);
        map.put(Mons.KINGAMBIT, 5);
        map.put(Mons.MEOWTH, 6);
        map.put(Mons.AZUMARRILL, 7);
        map.put(Mons.FUECOCO, 8);
        return (BidiMap<Mons, Integer>) Collections.unmodifiableMap(map);
    }

    private static BidiMap<Types, Integer> initTypesMap(){
        BidiMap<Types, Integer> map = new DualHashBidiMap<>();
        map.put(Types.FIRE, 1);
        map.put(Types.WATER, 2);
        map.put(Types.OTHER, 3);
        return (BidiMap<Types, Integer>) Collections.unmodifiableMap(map);
    }
}
