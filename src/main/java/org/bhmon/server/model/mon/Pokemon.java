package org.bhmon.server.model.mon;

import java.util.Objects;

public class Pokemon implements Comparable{
    public enum types{
        FIRE, WATER, OTHER
    }

    // Ids de los pokemon
    public static final int DELIBIRD_NUM = 0;
    public static final int TAUROS_NUM = 1;
    public static final int TORKOAL_NUM = 2;
    public static final int PELIPPER_NUM = 3;
    public static final int FARIGIRAF_NUM = 4;
    public static final int KINGAMBIT_NUM = 5;
    public static final int MEOWTH_NUM = 6;
    public static final int AZUMARRILL_NUM = 7;
    public static final int FUECOCO_NUM = 8;

    private int id, number, quantity, power, powerLoweredTimes=0;
    private String name;
    private types type;
    private boolean activeAbility;

    public Pokemon(int number, int quantity, String name, types type){
        setNumber(number);
        setQuantity(quantity);
        setPower(this.number);
        setName(name);
        setType(type);
    }

    public Pokemon(int id, Pokemon p){
        setId(id);
        setNumber(p.getNumber());
        setQuantity(p.getQuantity());
        setPower(this.number);
        setName(p.getName());
        setType(p.getType());
    }

//    @Override
//    public String toString() {
//        return "Pokemon{" +
//                "id=" + id +
//                ", quantity=" + quantity +
//                ", power=" + power +
//                ", name='" + name + '\'' +
//                ", type=" + type +
//                ", activeAbility=" + activeAbility +
//                '}';
//    }

    @Override
    public String toString() {
        return "Pokemon{" +
                "uniqueId=" + id +
                ", name='" + name + '\'' +
                '}';
    }


//    @Override
//    public String toString() {
//        return "Pokemon{" +
//                "uniqueId=" + uniqueId +
//                '}';
//    }

    @Override
    public int compareTo(Object o) {
        Pokemon p = (Pokemon) o;
        return this.getNumber() - p.getNumber();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon p = (Pokemon) o;
        return id == p.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, quantity, power, name, type, activeAbility);
    }

    public int updatePower(int n){
        if(this.number == KINGAMBIT_NUM && n == -1){
            n = 1;
        }
        power += n;
        return n;
    }

    public void setId(int id) {
        this.id = id;
    }

    private void setNumber(int number) {
        this.number = number;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void setType(types type) {
        this.type = type;
    }

    public void setActiveAbility(boolean activeAbility) {
        this.activeAbility = activeAbility;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPower() {
        return power;
    }

    public int getPowerLowerdTimes() {
        return powerLoweredTimes;
    }

    public String getName() {
        return name;
    }

    public types getType() {
        return type;
    }

    public boolean isActiveAbility() {
        return activeAbility;
    }
}
