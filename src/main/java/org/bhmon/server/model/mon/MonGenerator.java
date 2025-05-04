package org.bhmon.server.model.mon;

import static org.bhmon.codes.Codes.*;

import java.util.*;

public class MonGenerator {
    // Número de veces que se repite cada pokemon
    private static final int DELIBIRD_QUANTITY = 1;
    private static final int TAUROS_QUANTITY = 5;
    private static final int TORKOAL_QUANTITY = 3;
    private static final int PELIPPER_QUANTITY = 3;
    private static final int FARIGIRAF_QUANTITY = 2;
    private static final int KINGAMBIT_QUANTITY = 1;
    private static final int MEOWTH_QUANTITY = 1;
    private static final int AZUMARRILL_QUANTITY = 1;
    private static final int FUECOCO_QUANTITY = 1;


    public static Stack<Pokemon> getMons(){
        List<Pokemon> monsList = createMonsList();
        Stack<Pokemon> stack = new Stack<>();
        addMons(monsList, stack);

        Collections.shuffle(stack);
        return stack;
    }

    private static void addMons(List<Pokemon> list, Stack<Pokemon> stack){
        int id=0;
        for(Pokemon p : list){
            for(int i=0; i<p.getQuantity(); i++){
                Pokemon mon = new Pokemon(id, p);
                id++;
                stack.add(mon);
            }
        }
    }

    private static List<Pokemon> createMonsList(){
        List<Pokemon> list = new ArrayList<>();
        list.add(new Pokemon(monsMap.get(Mons.DELIBIRD), DELIBIRD_QUANTITY, "Delibird", Types.OTHER));
        list.add(new Pokemon(monsMap.get(Mons.TAUROS), TAUROS_QUANTITY, "Tauros", Types.OTHER));
        list.add(new Pokemon(monsMap.get(Mons.TORKOAL), TORKOAL_QUANTITY, "Torkoal", Types.FIRE));
        list.add(new Pokemon(monsMap.get(Mons.PELIPPER), PELIPPER_QUANTITY, "Pelipper", Types.WATER));
        list.add(new Pokemon(monsMap.get(Mons.FARIGIRAF), FARIGIRAF_QUANTITY, "Farigiraf", Types.OTHER));
        list.add(new Pokemon(monsMap.get(Mons.KINGAMBIT), KINGAMBIT_QUANTITY, "Kingambit", Types.OTHER));
        list.add(new Pokemon(monsMap.get(Mons.MEOWTH), MEOWTH_QUANTITY, "Meowth", Types.OTHER));
        list.add(new Pokemon(monsMap.get(Mons.AZUMARRILL), AZUMARRILL_QUANTITY, "Azumarrill", Types.WATER));
        list.add(new Pokemon(monsMap.get(Mons.FUECOCO), FUECOCO_QUANTITY, "Fuecoco", Types.FIRE));
        return list;
    }
}
