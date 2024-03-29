package worldofzuul.NPCer;

import worldofzuul.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Mechanic extends NPC {
    private boolean gaveToolset = false;
    private String file = new File("worldofzuul/NPCer/NPC-descriptions/VillagerText.txt").toString();

    public Mechanic(String name) {
        super(name);
    }

    @Override
    public void description(String command) {
        if (!super.getTalking()) {
            try {
                setTalking(true);
                String line = Files.readAllLines(Paths.get(this.file)).get(0);
                System.out.println(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (command.equals("information") && super.getTalking()) {
            try {
                String line = Files.readAllLines(Paths.get(this.file)).get(1);
                System.out.println(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (command.equals("take") && super.getTalking()) {
            if (!gaveToolset) {
                giveToolset();
                try {
                    String line = Files.readAllLines(Paths.get(this.file)).get(2);
                    System.out.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    String line = Files.readAllLines(Paths.get(this.file)).get(3);
                    System.out.println(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (command.equals("bye") && super.getTalking()) {
            try {
                String line = Files.readAllLines(Paths.get(this.file)).get(4);
                System.out.println(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Unknown conversation");
        }
    }

    @Override
    public String toString() {
        return super.getName();
    }

    public Toolset giveToolset() {
        this.gaveToolset = true;
        Player.setHaveToolset(true);
        return new Toolset();
    }
}
