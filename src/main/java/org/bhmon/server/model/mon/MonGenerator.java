package org.bhmon.server.model.mon;

import org.bhmon.server.model.mon.Pokemon.types;
import static org.bhmon.server.model.mon.Pokemon.*;

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
        list.add(new Pokemon(DELIBIRD_NUM, DELIBIRD_QUANTITY, "Delibird", types.OTHER));
        list.add(new Pokemon(TAUROS_NUM, TAUROS_QUANTITY, "Tauros", types.OTHER));
        list.add(new Pokemon(TORKOAL_NUM, TORKOAL_QUANTITY, "Torkoal", types.FIRE));
        list.add(new Pokemon(PELIPPER_NUM, PELIPPER_QUANTITY, "Pelipper", types.WATER));
        list.add(new Pokemon(FARIGIRAF_NUM, FARIGIRAF_QUANTITY, "Farigiraf", types.OTHER));
        list.add(new Pokemon(KINGAMBIT_NUM, KINGAMBIT_QUANTITY, "Kingambit", types.OTHER));
        list.add(new Pokemon(MEOWTH_NUM, MEOWTH_QUANTITY, "Meowth", types.OTHER));
        list.add(new Pokemon(AZUMARRILL_NUM, AZUMARRILL_QUANTITY, "Azumarrill", types.WATER));
        list.add(new Pokemon(FUECOCO_NUM, FUECOCO_QUANTITY, "Fuecoco", types.FIRE));
        return list;
    }
}
