package sample.presentation;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import sample.domain.Game;
import sample.domain.NPCer.Farmer;
import sample.domain.NPCer.Mechanic;
import sample.domain.NPCer.NPC;
import sample.domain.NPCer.Professor;
import sample.domain.Player;
import sample.domain.Road;
import sample.domain.RoadBuilder;
import sample.domain.Rooms.*;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    public static List<String> roomExit = new ArrayList<>();
    public static String background;
    public static Road road = new Road();
    public static Player playerObject = new Player();
    public static RoadBuilder roadBuilder = new RoadBuilder();
    public static Professor professorObject = new Professor("Professor");
    public static Mechanic mechanicObject = new Mechanic("Mechanic");
    public static Farmer farmerObject = new Farmer("Farmer");
    private boolean north, south, east, west;
    private String[] direction = {"North", "South", "West", "East"};
    private SpriteAnimation playerAnimation = new SpriteAnimation(direction[0]);
    private int[] numbersPlayer;
    private long animationWalk = 0;

    @FXML
    private ImageView backgroundRoom;

    @FXML
    public ImageView roadView = new ImageView("file:" + road.getImage());

    @FXML
    public ImageView roadBuilderView = new ImageView("file:" + roadBuilder.getImage());

    @FXML
    public ImageView player = new ImageView("file:" + playerObject.getImage());

    @FXML
    public ImageView professorNpc = new ImageView("file:" + professorObject.getImage());

    @FXML
    public ImageView mechanicNpc = new ImageView("file:"+ mechanicObject.getImage());

    @FXML
    public ImageView farmerNpc = new ImageView("file:" + farmerObject.getImage());

    public void initialize() {
        player.setImage(new Image("file:" + playerObject.getImage()));
        player.setViewport(new Rectangle2D(0, 0, 32, 48));
        roadView.setImage(new Image("file:" + road.getImage()));
        roadBuilderView.setImage(new Image("file:" + roadBuilder.getImage()));
        professorNpc.setImage(new Image("file:" + professorObject.getImage()));
        professorNpc.setTranslateX(3000);
        mechanicNpc.setImage(new Image("file:" + mechanicObject.getImage()));
        mechanicNpc.setTranslateX(3000);
        farmerNpc.setImage(new Image("file:" + farmerObject.getImage()));
        farmerNpc.setTranslateX(3000);
        showRoadBuilderRoad();
    }

    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (north && player.getTranslateY() > -220) {
                playerAnimation.setDirection(direction[0]);
                if (animationWalk % 13 == 0) {
                    numbersPlayer = playerAnimation.changePic();
                    player.setViewport(new Rectangle2D(numbersPlayer[0], numbersPlayer[1], numbersPlayer[2], numbersPlayer[3]));
                }
                player.setTranslateY(player.getTranslateY() - 2.5);
                animationWalk++;
            }
            if (south && player.getTranslateY() < 220) {
                playerAnimation.setDirection(direction[1]);
                if (animationWalk % 13 == 0) {
                    numbersPlayer = playerAnimation.changePic();
                    player.setViewport(new Rectangle2D(numbersPlayer[0], numbersPlayer[1], numbersPlayer[2], numbersPlayer[3]));
                }
                player.setTranslateY(player.getTranslateY() + 2.5);
                animationWalk++;
            }
            if (east && player.getTranslateX() > -340) {
                playerAnimation.setDirection(direction[2]);
                if (animationWalk % 13 == 0) {
                    numbersPlayer = playerAnimation.changePic();
                    player.setViewport(new Rectangle2D(numbersPlayer[0], numbersPlayer[1], numbersPlayer[2], numbersPlayer[3]));
                }
                player.setTranslateX(player.getTranslateX() - 2.5);
                animationWalk++;
            }
            if (west && player.getTranslateX() < 340) {
                playerAnimation.setDirection(direction[3]);
                if (animationWalk % 13 == 0) {
                    numbersPlayer = playerAnimation.changePic();
                    player.setViewport(new Rectangle2D(numbersPlayer[0], numbersPlayer[1], numbersPlayer[2], numbersPlayer[3]));
                }
                player.setTranslateX(player.getTranslateX() + 2.5);
                animationWalk++;
            }

        }
    };

    public void movePlayer(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
            case W:
                timer.start();
                north = true;
                west = false;
                east = false;
                System.out.println(player.getTranslateY());
                break;
            case DOWN:
            case S:
                timer.start();
                south = true;
                west = false;
                east = false;
                System.out.println(player.getTranslateY());
                break;
            case LEFT:
            case A:
                timer.start();
                east = true;
                north = false;
                south = false;
                System.out.println(player.getTranslateX());
                break;
            case RIGHT:
            case D:
                timer.start();
                west = true;
                north = false;
                south = false;
                System.out.println(player.getTranslateX());
                break;
        }
        NewRoom();
    }

    private void NewRoom() {
        if (player.getTranslateY() < -208 && player.getTranslateX() > -150 && player.getTranslateX() < -10) {
            changeNorth();
        } else if (player.getTranslateY() > 208 && player.getTranslateX() > -140 && player.getTranslateX() < 20) {
            changeSouth();
        } else if (player.getTranslateX() < -328 && player.getTranslateY() > -60 && player.getTranslateY() < 0) {
            changeWest();
        } else if (player.getTranslateX() > 328 && player.getTranslateY() > -60 && player.getTranslateY() < 0) {
            changeEast();
        }
    }

    public void stopPlayer(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
            case W:
                timer.stop();
                animationWalk = 0;
                north = false;
                break;
            case DOWN:
            case S:
                timer.stop();
                animationWalk = 0;
                south = false;
                break;
            case LEFT:
            case A:
                timer.stop();
                animationWalk = 0;
                east = false;
                break;
            case RIGHT:
            case D:
                timer.stop();
                animationWalk = 0;
                west = false;
                break;
        }
    }

    public void changeNorth() {
        if (!(Main.game.getCurrentRoom() instanceof Beach || Main.game.getCurrentRoom() instanceof Farm || Main.game.getCurrentRoom() instanceof Town || Main.game.getCurrentRoom() instanceof Park)) {
            player.setTranslateY(204);
        }
        Game.changedRoom = "north";
        Main.game.goRoom();
        backgroundRoom.setImage(new Image("file:" + background));
        showRoadBuilderRoad();
        showProfessor();
        showMechanic();
        showFarmer();
    }

    public void changeSouth() {
        if (!(Main.game.getCurrentRoom() instanceof Beach || Main.game.getCurrentRoom() instanceof Farm || Main.game.getCurrentRoom() instanceof Town || Main.game.getCurrentRoom() instanceof Sdu)) {
            player.setTranslateY(-204);
        }
        Game.changedRoom = "south";
        Main.game.goRoom();
        backgroundRoom.setImage(new Image("file:" + background));
        showRoadBuilderRoad();
        showProfessor();
        showMechanic();
        showFarmer();
    }

    public void changeWest() {
        if (!(Main.game.getCurrentRoom() instanceof Beach || Main.game.getCurrentRoom() instanceof Sdu || Main.game.getCurrentRoom() instanceof Park)) {
            player.setTranslateX(330);
        }
        Game.changedRoom = "west";
        Main.game.goRoom();
        backgroundRoom.setImage(new Image("file:" + background));
        showRoadBuilderRoad();
        showProfessor();
        showMechanic();
        showFarmer();
    }

    public void changeEast() {
        if (!(Main.game.getCurrentRoom() instanceof Sdu || Main.game.getCurrentRoom() instanceof Town || Main.game.getCurrentRoom() instanceof Farm)) {
            player.setTranslateX(-330);
        }
        Game.changedRoom = "east";
        Main.game.goRoom();
        backgroundRoom.setImage(new Image("file:" + background));
        showRoadBuilderRoad();
        showProfessor();
        showMechanic();
        showFarmer();
    }

    public void showRoadBuilderRoad() {
        if (Main.game.getCurrentRoom() instanceof RoadBuild) {
            roadView.setViewport(new Rectangle2D(-681 + (RoadBuilder.getInventoryCount()*22.7), 0, 681, 69));
        } else {
            roadView.setViewport(new Rectangle2D(-681, 0, 681, 69));
        }
        showRoadBuilder();
        showProfessor();
        showMechanic();
        showFarmer();
    }

    public void showRoadBuilder() {
        if (Main.game.getCurrentRoom() instanceof RoadBuild) {
            roadBuilderView.setViewport(new Rectangle2D(0, 0, 484, 323));
            roadBuilderView.setTranslateX(300 - (RoadBuilder.getInventoryCount() * 20));
        } else {
            roadBuilderView.setViewport(new Rectangle2D(-484, 0, 484, 323));
        }
    }

    public void showProfessor(){
        professorNpc.setTranslateX(3000);
        if (Main.game.getCurrentRoom() instanceof Sdu){
            professorNpc.setTranslateX(30);
        }
    }

    public void showMechanic() {
        mechanicNpc.setTranslateX(3000);
        if (Main.game.getCurrentRoom() instanceof Town){
            mechanicNpc.setTranslateX(169);
            mechanicNpc.setTranslateY(20);
        }
    }

    public void showFarmer(){
        farmerNpc.setTranslateX(3000);
        if (Main.game.getCurrentRoom() instanceof Farm){
            farmerNpc.setTranslateX(190);
            farmerNpc.setTranslateY(2);
        }
    }
}