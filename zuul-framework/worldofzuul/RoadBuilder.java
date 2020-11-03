package worldofzuul;

import worldofzuul.PlasticElements.Plastic;

import java.util.ArrayList;

public class RoadBuilder {
    static ArrayList<Plastic> inventory = new ArrayList<>();

    public static ArrayList<Plastic> inventory(ArrayList<Plastic> plastic) {
        inventory.addAll(plastic);
        int inv = ((inventory.size()*100)/Game.getRoadDone());
        System.out.println("The road is " + inv + "% Complete");
        System.out.println("You need to collect " + (Game.getRoadDone()-inventory.size()));
        return inventory;
    }

    public static ArrayList<Plastic> getInventory() {
        return inventory;
    }



}
