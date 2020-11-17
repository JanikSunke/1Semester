package sample.domain;

import sample.domain.NPCer.*;
import sample.domain.PlasticElements.Plastic;
import sample.domain.Rooms.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import sample.presentation.Controller;

public class Game {
    private Room currentRoom;
    final private File welcomeMessage = new File("src/sample/data/textfiles/gameDescriptions/gameDescription.txt");
    final private File help = new File("src/sample/data/textfiles/gameDescriptions/help.txt");
    private static final int roadDone = 30;
    private Room RoadBuild, Town, Beach, Farm, Park, Sdu;
    private Farmer farmer = new Farmer("Farmer");
    private Mechanic mechanic = new Mechanic("Mechanic");
    private Professor professor = new Professor("Professor");
    private Toolset toolset = new Toolset();

    public Game() {
        createRooms();
    }


    private void createRooms() {
        RoadBuild = new RoadBuild();
        Town = new Town();
        Beach = new Beach();
        Farm = new Farm();
        Park = new Park();
        Sdu = new Sdu();

        RoadBuild.setExit("east", Farm);
        RoadBuild.setExit("south", Sdu);
        RoadBuild.setExit("west", Beach);
        RoadBuild.setExit("north", Park);

        Sdu.setExit("north", RoadBuild);

        Farm.setExit("west", RoadBuild);

        Beach.setExit("east", RoadBuild);

        Park.setExit("east", Town);
        Park.setExit("south", RoadBuild);

        Town.setExit("west", Park);

        currentRoom = RoadBuild;
    }

    public void play() {
        printWelcome();

        boolean finished = false;
        /*
        while (!finished) {
            RoadBuilder.damagedMachine();
        }
        */
    }

    private void printWelcome() {
        Scanner reader;
        try {
            reader = new Scanner(welcomeMessage);
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            }
            System.out.println();
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find the file");
            e.printStackTrace();
        }
        Player.setName();
        Timer.setStartTime();
    }



    private void printHelp() {

        Scanner reader;
        try {
            reader = new Scanner(help);
            System.out.println(reader.nextLine());
            System.out.println(reader.nextLine());
            System.out.println(reader.nextLine());
            System.out.println(reader.nextLine());
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find the file");
            e.printStackTrace();
        }
    }

    // private void goRoom() {

    public void goRoom() {
        // Room nextRoom = currentRoom.getExit();
        Room nextRoom = Park;

        if (nextRoom == null) {
            System.out.println("That is not possible!");
        } else {
            currentRoom = nextRoom;
            currentRoom.getPlasticTypes();
            Controller.background = currentRoom.getPictureRoom();
        }
    }

    private boolean givePlastic() {
        List<Plastic> plasticInv = Player.getPlasticInv();
        List<Plastic> road = RoadBuilder.inventory(plasticInv);
        Player.resetPlasticInv();
        if (road.size() >= roadDone) {
            return true;
        }
        return false;
    }

    public static int getRoadDone() {
        return roadDone;
    }

}
