package sample.presentation;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import sample.domain.*;
import sample.domain.NPCer.Farmer;
import sample.domain.NPCer.Mechanic;
import sample.domain.NPCer.Professor;
import sample.domain.PlasticElements.*;
import sample.domain.Rooms.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

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
    public static DialogNPC dialog = new DialogNPC();
    public static Timer highScoreTimer = new Timer();
    private boolean north, south, east, west;
    private int spaceCount = 0;
    private int farmerTalked = 0;
    private boolean farmerTalk = false;
    private boolean professorTalk = false;
    private boolean mechanicTalk = false;
    private boolean talking = false;
    private String[] direction = {"North", "South", "West", "East"};
    private SpriteAnimation playerAnimation = new SpriteAnimation(direction[0]);
    private FireAnimation fireAnimation = new FireAnimation();
    private BrokeMachineAnimation brokeMachineAnimation = new BrokeMachineAnimation();
    private NoAccess noAccess = new NoAccess();
    private double[] numbersPlayer;
    private double[] numbersFire;
    private double[] numbersBrokenFire;
    private long animationWalk = 0L;
    private boolean gameNotStarted = true;
    private ObservableList<ImageView> inventoryObservable = FXCollections.observableList(new ArrayList<ImageView>());
    private long animationFireSmoke = 0L;
    private long animationFireSmokeBrokenMachine = 0L;
    private long animationDriving = 0L;
    private int numberOfMovement = 0;
    private boolean talkingRoadbuilder = false;
    private int counterRepair = 0;
    private boolean doneRepairing = true;
    private AudioMusicPlayer backgroundMusic = new AudioMusicPlayer("src/sample/presentation/audio/BackgroundMusic.wav");
    private AudioMusicPlayer roadbuilderCrashSound = new AudioMusicPlayer("src/sample/presentation/audio/RoadbuildCrash.wav");
    private AudioMusicPlayer roadbuilderMovingSound = new AudioMusicPlayer("src/sample/presentation/audio/RoadbuilderMovingSound.wav");
    private AudioMusicPlayer npcTalk = new AudioMusicPlayer("src/sample/presentation/audio/npcTalking.wav");
    private AudioMusicPlayer repairSound = new AudioMusicPlayer("src/sample/presentation/audio/repairSound.wav");
    private AudioMusicPlayer pickUpSound = new AudioMusicPlayer("src/sample/presentation/audio/pickUpSound.wav");
    private boolean gameOver = false;

    @FXML
    private ImageView backgroundRoom;
    @FXML
    public ImageView roadView;
    @FXML
    public ImageView roadBuilderView;
    @FXML
    public ImageView player;
    @FXML
    public ImageView smoke;
    @FXML
    public ImageView smokeBrokenMachine;
    @FXML
    public Rectangle inventory;
    @FXML
    public ImageView professorNpc;
    @FXML
    public ImageView mechanicNpc;
    @FXML
    public ImageView farmerNpc;
    @FXML
    public ImageView dialogBox;
    @FXML
    private ImageView toolsetImg;
    @FXML
    private Text textLine1;
    @FXML
    private Text textLine12;
    @FXML
    private Text textLine13;
    @FXML
    private Text playerText;
    @FXML
    private Text helpText;
    @FXML
    private Rectangle toolRect;
    @FXML
    private TextField nameField;
    @FXML
    private ImageView lockToolSlot;


    public void initialize() {
        helpText.setTranslateY(240);
        helpText.setTranslateX(-296);
        playerText.setFont(Font.font("Dialog", FontWeight.BOLD, 11));
        textLine1.setFont(Font.font("Dialog", FontWeight.BOLD, 11));
        textLine12.setFont(Font.font("Dialog", FontWeight.BOLD, 11));
        textLine13.setFont(Font.font("Dialog", FontWeight.BOLD, 11));
        backgroundRoom.setImage(new Image("file:src/sample/presentation/pictures/Backgrounds/StartScreen.png"));
    }


    public void generatePlasticInRoom(List<Plastic> plasticList) {
        clearPlasticInRoom();
        ImageView[] plast = {plast1, plast2, plast3, plast4, plast5, plast6, plast7, plast8, plast9, plast10, plast11, plast12, plast13, plast14, plast15,
                plast16, plast17, plast18, plast19, plast20};

        for (int i = 0; i < plasticList.size(); i++) {
            if (plasticList.get(i) != null) {
                plast[i].setImage(new Image("file:" + plasticList.get(i).getImage()));
                plast[i].setTranslateX(plasticList.get(i).getPosition()[0]);
                plast[i].setTranslateY(plasticList.get(i).getPosition()[1]);
                plast[i].setFitHeight(30);
                plast[i].setFitWidth(30);
                // Kode til at give plastik ny position hvis de falder inden for no go zonerne.
                if (noAccess.moveBlock(plast[i].getTranslateX(), plast[i].getTranslateY(), 0, -2)) {
                    plasticList.get(i).newPosition();
                    generatePlasticInRoom(plasticList);
                    break;
                } else if (noAccess.moveBlock(plast[i].getTranslateX(), plast[i].getTranslateY(), 0, 2)) {
                    plasticList.get(i).newPosition();
                    generatePlasticInRoom(plasticList);
                    break;
                } else if (noAccess.moveBlock(plast[i].getTranslateX(), plast[i].getTranslateY(), -2, 0)) {
                    plasticList.get(i).newPosition();
                    generatePlasticInRoom(plasticList);
                    break;
                } else if (noAccess.moveBlock(plast[i].getTranslateX(), plast[i].getTranslateY(), 2, 0)) {
                    plasticList.get(i).newPosition();
                    generatePlasticInRoom(plasticList);
                    break;
                }
            }
        }
    }

    public void clearPlasticInRoom() {
        ImageView[] plast = {plast1, plast2, plast3, plast4, plast5, plast6, plast7, plast8, plast9, plast10, plast11, plast12, plast13, plast14, plast15,
                plast16, plast17, plast18, plast19, plast20};

        for (ImageView image : plast) {
            image.setTranslateX(3000);
            image.setTranslateY(3000);
        }
    }


    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long l) {
            if (north && player.getTranslateY() > -220) {
                handleAnimation(0,0,-2,"north");
            } else if (south && player.getTranslateY() < 220) {
                handleAnimation(1,0,2,"south");
            } else if (west && player.getTranslateX() > -340) {
                handleAnimation(2,-2,0,"west");
            } else if (east && player.getTranslateX() < 340) {
                handleAnimation(3,2,0,"east");
            }
        }
    };

    public void handleAnimation(int dir, int x, int y, String option) {
        playerAnimation.setDirection(direction[dir]);
        if (!noAccess.moveBlock(player.getTranslateX(), player.getTranslateY(), x, y)) {
            if (animationWalk % 13 == 0) {
                numbersPlayer = playerAnimation.changePic();
                player.setViewport(new Rectangle2D(numbersPlayer[0], numbersPlayer[1], numbersPlayer[2], numbersPlayer[3]));
            }
            switch (option) {
                case "north":
                player.setTranslateY(player.getTranslateY() - 2.5);
                break;
                case "south":
                player.setTranslateY(player.getTranslateY() + 2.5);
                break;
                case "west":
                player.setTranslateX(player.getTranslateX() - 2.5);
                break;
                case "east":
                player.setTranslateX(player.getTranslateX() + 2.5);
                break;
            }
        }
        animationWalk++;
    }


    // Need this method
    public void updateInventory() {
        ImageView[] inventoryItems = {item1, item2, item3, item4, item5, item6, item7, item8, item9, item10};
        inventoryObservable.removeAll();
        ArrayList<Plastic> playersInv = new ArrayList<>(playerObject.getPlasticInv());
        for (int i = 0; i < inventoryItems.length; i++) {
            if (i < playerObject.getPlasticInv().size()) {
                inventoryItems[i].setImage(new Image("file:" + playersInv.get(i).getImage()));
                // Da plastik imageviews har en standard layout i FXML dokumentet, så skal der bruges LayoutX i stedet for TranslateX
                inventoryItems[i].setLayoutX((147 + i * 45) + playerObject.getPlasticInv().get(i).getAdjustXForInventory());
            } else {
                inventoryItems[i].setImage(null);
            }
        }
    }

    public void collectPlastic(List<Plastic> plasticList) {
        if (playerObject.getPlasticInv().size() < 10) {
            ImageView[] plast = {plast1, plast2, plast3, plast4, plast5, plast6, plast7, plast8, plast9, plast10, plast11, plast12, plast13, plast14, plast15,
                    plast16, plast17, plast18, plast19, plast20};
            for (int i = 0; i < plast.length; i++) {
                if (plast[i].getTranslateX() - 15 <= player.getTranslateX() && plast[i].getTranslateX() + 15 >= player.getTranslateX()) {
                    if (plast[i].getTranslateY() - 15 <= player.getTranslateY() && plast[i].getTranslateY() + 15 >= player.getTranslateY()) {
                        pickUpSound.AudioPlayer();
                        playerObject.plasticCollect(plasticList.get(i), Main.game.getCurrentRoom());
                        plast[i].setTranslateX(3000);
                        updateInventory();
                    }
                }
            }
        } else {
            System.out.println("I can't lift more!!!!");
        }
    }

    /*
    public int adjustInventoryItem(Plastic plastic) {
        if (plastic instanceof CleaningPlastic) {
            return -3;
        } else if (plastic instanceof MilkBottle) {
            return -7;
        } else if (plastic instanceof SodaBottle) {
            return
        }
        return 0;
    }

     */

    public void movePlayer(KeyEvent keyEvent) throws InterruptedException {
        switch (keyEvent.getCode()) {
            case P:
                if (gameOver) {
                    Main main = new Main();
                    Stage primaryStage = new Stage();
                    ControllerAsk.theStage = primaryStage;
                    try {
                        main.start(primaryStage);
                        Parent askRoot = FXMLLoader.load(getClass().getResource("askSample.fxml"));
                        Scene scene2 = new Scene(askRoot);
                        primaryStage.setScene(scene2);
                        primaryStage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case H:
                try {
                    Desktop.getDesktop().open(new File("src/sample/presentation/pictures/video/Introduction.mp4"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case UP:
            case W:
                timer.start();
                north = true;
                System.out.println("y =  " + player.getTranslateY() + " x = " + player.getTranslateX());
                break;
            case DOWN:
            case S:
                timer.start();
                south = true;
                System.out.println("y =  " + player.getTranslateY() + " x = " + player.getTranslateX());
                break;
            case LEFT:
            case A:
                timer.start();
                west = true;
                System.out.println("y =  " + player.getTranslateY() + " x = " + player.getTranslateX());
                break;
            case RIGHT:
            case D:
                timer.start();
                east = true;
                System.out.println("y =  " + player.getTranslateY() + " x = " + player.getTranslateX());
                break;
            case SPACE:
                if (numberOfMovement == 0) {
                    if (gameNotStarted) {
                        String name = nameField.getText();
                        if (name.matches(".*[0-9].*") || name.matches(".*[A-Z]*.")) {
                            playerObject.setNames(name);
                            nameField.setOpacity(0);
                            StartGame();
                            gameNotStarted = false;
                        }
                    } else {
                        collectPlastic(Main.game.placePlastic());
                    }

                    if (Main.game.isRoom(Main.game.RoadBuild) && talkingRoadbuilder && spaceCount != 0) {
                        if (spaceCount == 1) {
                            talking = true;
                            talkNPC(playerText, "Road builder", 3);
                            spaceCount++;
                        } else if (spaceCount == 2) {
                            hideDialogBox();
                            spaceCount = 0;
                        }
                    } else if (Main.game.isRoom(Main.game.RoadBuild) && player.getTranslateX() > roadBuilderView.getTranslateX() - 50 && player.getTranslateX() < roadBuilderView.getTranslateX() + 50 && player.getTranslateY() > roadBuilderView.getTranslateY() - 50 && player.getTranslateY() < roadBuilderView.getTranslateY() + 50) {
                        if (roadBuilder.getInventoryCount() >= 19 && roadBuilder.isNotDamagedBefore()) {
                            roadBuilder.damagedMachine();
                            roadBuilder.setNotDamagedBefore(false);
                            roadbuilderCrashSound.AudioPlayer();
                        } else if (playerObject.getHaveToolset() && roadBuilder.getDamaged() > 0) {
                            repairSound.musicPlayerInfinity();
                            repairTheMachine();
                        }

                        if (roadBuilder.getDamaged() > 0 && !playerObject.getHaveToolset()) {
                            textLine1.setTranslateY(-210);
                            textLine1.setFont(Font.font("Dialog", FontWeight.BOLD, 11));
                            textLine12.setTranslateY(-190);
                            textLine12.setFont(Font.font("Dialog", FontWeight.BOLD, 11));
                            textLine13.setTranslateY(-170);
                            textLine13.setFont(Font.font("Dialog", FontWeight.BOLD, 11));
                            playerText.setTranslateY(-130);
                            playerText.setFont(Font.font("Dialog", FontWeight.BOLD, 11));
                            if (spaceCount == 0 && roadBuilder.getDamaged() > 0) {
                                talking = true;
                                talkNPC(textLine1, "Road builder", 0);
                                talkNPC(textLine12, "Road builder", 1);
                                talkNPC(textLine13, "Road builder", 2);
                                spaceCount++;
                                talkingRoadbuilder = true;
                            }

                        } else if (roadBuilder.getDamaged() == 0) {
                            numberOfMovement = playerObject.getPlasticInv().size() * 4;
                            if (playerObject.getPlasticInv().size() > 0) {
                                Main.game.givePlastic();
                                roadbuilderMovingSound.AudioPlayer();
                            }
                            updateInventory();
                        }
                    } else if (Main.game.isRoom(Main.game.Farm) && player.getTranslateX() > farmerNpc.getTranslateX() - 30 && player.getTranslateX() < farmerNpc.getTranslateX() + 30 && player.getTranslateY() > farmerNpc.getTranslateY() - 30 && player.getTranslateY() < farmerNpc.getTranslateY() + 30) {
                        showDialogBox();
                    } else if (Main.game.isRoom(Main.game.Sdu) && player.getTranslateX() > professorNpc.getTranslateX() - 30 && player.getTranslateX() < professorNpc.getTranslateX() + 30 && player.getTranslateY() > professorNpc.getTranslateY() - 30 && player.getTranslateY() < professorNpc.getTranslateY() + 30) {
                        showDialogBox();
                    } else if (Main.game.isRoom(Main.game.Town) && player.getTranslateX() > mechanicNpc.getTranslateX() - 30 && player.getTranslateX() < mechanicNpc.getTranslateX() + 30 && player.getTranslateY() > mechanicNpc.getTranslateY() - 30 && player.getTranslateY() < mechanicNpc.getTranslateY() + 30) {
                        showDialogBox();
                    }
                }

        }
        if (gameNotStarted) {
            north = false;
            south = false;
            east = false;
            west = false;
        }
        NewRoom();
    }

    public void repairTheMachine() {
        Timeline timeline = new Timeline();
        int FPS = 60;
        KeyFrame frame = new KeyFrame(Duration.millis(1000 / FPS), event -> {
            if (counterRepair % 60 == 0 && roadBuilder.getDamaged() > 0) {
                playerObject.getToolset().repairMachine();
                dialogBox.setTranslateY(-170);
                textLine12.setText(100 - roadBuilder.getDamaged() + "% repaired");
            } else if (doneRepairing && roadBuilder.getDamaged() == 0 && !gameOver) {
                hideDialogBox();
                toolsetImg.setTranslateX(3000);
                lockToolSlot.setOpacity(0.7);
                doneRepairing = false;
                repairSound.AudioStop();
            }
            counterRepair++;
        });
        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    private void endGame() {
        if (roadBuilder.getInventoryCount() >= Main.game.getRoadDone()) {
            gameOver = true;
            //Sets the highscorebackground
            backgroundRoom.setImage(new Image("file:src/sample/presentation/pictures/Backgrounds/EndScreen.png"));
            //Presents the score
            highScoreTimer.setEndTime();
            playerText.setText("Your " + highScoreTimer.timeScore());
            playerText.setTranslateY(130);
            String[] scoreList = highScoreTimer.setHighScore();
            textLine1.setTranslateY(-80);
            textLine12.setTranslateY(0);
            textLine13.setTranslateY(85);
            textLine1.setText(scoreList[0]);
            textLine12.setText(scoreList[1]);
            textLine13.setText(scoreList[2]);
            //resets the road and game
            roadBuilder.setInventoryCount(0);
            gameNotStarted = true;
            //Hide images
            hideSlotLines();
            roadBuilderView.setOpacity(0);
            roadView.setOpacity(0);
            player.setOpacity(0);
            smoke.setOpacity(0);
            inventory.setOpacity(0);
            toolRect.setOpacity(0);
            //Lay out new plastic
            clearPlasticInRoom();
            //Resets NPCs
            farmerTalk = false;
            professorTalk = false;
            mechanicTalk = false;
            talkingRoadbuilder = false;
            doneRepairing = true;
        }
    }

    private void StartGame() {
        backgroundMusic.AudioStop();
        gameOver = false;
        Main.game.createRooms();
        //Create the images
        backgroundRoom.setImage(new Image("file:src/sample/presentation/pictures/Backgrounds/RoadBuild.png"));
        player.setImage(new Image("file:" + playerObject.getImage()));
        player.setViewport(new Rectangle2D(0, 0, 32, 48));
        roadView.setImage(new Image("file:" + road.getImage()));
        roadBuilderView.setImage(new Image("file:" + roadBuilder.getImage()));
        professorNpc.setImage(new Image("file:" + professorObject.getImage()));
        mechanicNpc.setImage(new Image("file:" + mechanicObject.getImage()));
        farmerNpc.setImage(new Image("file:" + farmerObject.getImage()));
        dialogBox.setImage(new Image("file:" + dialog.getImage()));
        smoke.setImage(new Image("file:src/sample/presentation/pictures/buildSmoke.png"));
        smokeBrokenMachine.setImage(new Image("file:src/sample/presentation/pictures/fireSmoke-1.png"));
        //Show images (& hides highscore)
        showSlotLines();
        lockToolSlot.setOpacity(0.7);
        inventory.setOpacity(0.5);
        toolRect.setOpacity(0.5);
        roadBuilderView.setOpacity(1);
        roadView.setOpacity(1);
        player.setOpacity(1);
        smoke.setOpacity(1);
        hideDialogBox();
        //Generates plastic and runs the animations
        generatePlasticInRoom(Main.game.placePlastic());
        movementMachine();
        smokeMachine();
        smokeBrokenMachine();
        //Starts the time for highscorelist
        highScoreTimer.setStartTime();
        backgroundMusic.musicPlayerInfinity();
    }

    private void NewRoom() {
        //North
        if (Main.game.isRoom(Main.game.RoadBuild) && player.getTranslateY() < -202 && player.getTranslateX() > -142.5 && player.getTranslateX() < -82.5) {
            changeNorth();
        } else if (Main.game.isRoom(Main.game.Sdu) && player.getTranslateY() < -158 && player.getTranslateX() > -45.5 && player.getTranslateX() < 14) {
            changeNorth();
            //South
        } else if (Main.game.isRoom(Main.game.RoadBuild) && player.getTranslateY() > 208 && player.getTranslateX() > -80 && player.getTranslateX() < 14) {
            changeSouth();
        } else if (Main.game.isRoom(Main.game.Park) && player.getTranslateY() > 208 && player.getTranslateX() > -142.5 && player.getTranslateX() < -82.5) {
            changeSouth();
        } else if (Main.game.isRoom(Main.game.Park) && player.getTranslateY() > 208 && player.getTranslateX() > 68 && player.getTranslateX() < 126) {
            changeSouth();
            //West
        } else if (player.getTranslateX() < -328 && player.getTranslateY() > -116.5 && player.getTranslateY() < -61.5) {
            changeWest();
        } else if (Main.game.isRoom(Main.game.Farm) && player.getTranslateX() < -328 && player.getTranslateY() > -96 && player.getTranslateY() < -66) {
            changeWest();
        } else if (Main.game.isRoom(Main.game.Town) && player.getTranslateX() < -328 && player.getTranslateY() > -53 && player.getTranslateY() < -15) {
            changeWest();
            //East
        } else if (Main.game.isRoom(Main.game.Beach) && player.getTranslateX() > 328 && player.getTranslateY() > -100 && player.getTranslateY() < -61.5) {
            changeEast();
        } else if (Main.game.isRoom(Main.game.RoadBuild) && player.getTranslateX() > 328 && player.getTranslateY() > -116.5 && player.getTranslateY() < -61.5) {
            changeEast();
        } else if (Main.game.isRoom(Main.game.Park) && player.getTranslateX() > 330 && player.getTranslateY() > -56 && player.getTranslateY() < -15) {
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
                west = false;
                break;
            case RIGHT:
            case D:
                timer.stop();
                animationWalk = 0;
                east = false;
                break;
        }
    }

    public void changeNorth() {
        changeRoom("north");
        setPlayerEntrance(Main.game.RoadBuild,204,false);
        setPlayerEntrance(Main.game.Park,204,false);
    }

    public void changeSouth() {
        changeRoom("south");
        setPlayerEntrance(Main.game.Sdu,-20,true);
        setPlayerEntrance(Main.game.Sdu,-150,false);
        setPlayerEntrance(Main.game.RoadBuild,-200,false);
        setPlayerEntrance(Main.game.RoadBuild,-100,true);
    }

    public void changeWest() {
        changeRoom("west");
        setPlayerEntrance(Main.game.Beach,327,true);
        setPlayerEntrance(Main.game.Park,327,true);
        setPlayerEntrance(Main.game.RoadBuild,327,true);
        setPlayerEntrance(Main.game.RoadBuild,-77,false);
        setPlayerEntrance(Main.game.Park,-33.5,false);
    }

    public void changeEast() {
        changeRoom("east");
        setPlayerEntrance(Main.game.Town, -327, true);
        setPlayerEntrance(Main.game.Farm, -327, true);
        setPlayerEntrance(Main.game.RoadBuild, -327, true);
        setPlayerEntrance(Main.game.Town, -30, false);
        setPlayerEntrance(Main.game.Farm, -76, false);
    }

    public void setPlayerEntrance(Room room, double coordinate, boolean x) {
        if (Main.game.isRoom(room) && !x) {
            player.setTranslateY(coordinate);
        } else if (Main.game.isRoom(room) && x) {
            player.setTranslateX(coordinate);
        }
    }

    public void changeRoom(String direction) {
        Game.changedRoom = direction;
        Main.game.goRoom();
        backgroundRoom.setImage(new Image("file:" + background));
        hideDialogBox();
        showRoomObjects();
        generatePlasticInRoom(Main.game.placePlastic());
    }

    public void smokeMachine() {
        Timeline timeline = new Timeline();
        int FPS = 60;
        KeyFrame frame = new KeyFrame(Duration.millis(1000 / FPS), event -> {
            if (Main.game.isRoom(Main.game.RoadBuild)) {
                if (animationFireSmoke % 20 == 0) {
                    numbersFire = fireAnimation.changePic();
                    smoke.setViewport(new Rectangle2D(numbersFire[0], numbersFire[1], numbersFire[2], numbersFire[3]));
                    double smokeHeight = roadBuilderView.getTranslateY() - numbersFire[0] / 22 - 40;
                    double smokeWidth = roadBuilderView.getTranslateX() + numbersFire[0] / 22 + 43;
                    smoke.setTranslateY(smokeHeight);
                    smoke.setTranslateX(smokeWidth);
                }
                animationFireSmoke++;
            } else {
                smoke.setTranslateX(3000);
            }
        });
        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    public void smokeBrokenMachine() {
        Timeline timeline = new Timeline();
        int FPS = 60;
        KeyFrame frame = new KeyFrame(Duration.millis(1000 / FPS), event -> {
            if (Main.game.isRoom(Main.game.RoadBuild) && roadBuilder.getDamaged() > 0) {
                if (animationFireSmokeBrokenMachine % 20 == 0) {
                    numbersBrokenFire = brokeMachineAnimation.changePic();
                    smokeBrokenMachine.setViewport(new Rectangle2D(numbersBrokenFire[0], numbersBrokenFire[1], numbersBrokenFire[2], numbersBrokenFire[3]));
                    double smokeHeight = roadBuilderView.getTranslateY() - 11;
                    double smokeWidth = roadBuilderView.getTranslateX() - 36;
                    smokeBrokenMachine.setTranslateY(smokeHeight);
                    smokeBrokenMachine.setTranslateX(smokeWidth);
                }
                animationFireSmokeBrokenMachine++;
            } else {
                smokeBrokenMachine.setTranslateX(3000);
            }
        });
        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    public void movementMachine() {
        Timeline timeline = new Timeline();
        int FPS = 60;
        KeyFrame frame = new KeyFrame(Duration.millis(1000 / FPS), event -> {
            if (numberOfMovement != 0 && Main.game.isRoom(Main.game.RoadBuild) && roadBuilderView.getTranslateX() > -290) {
                if (animationDriving % 5 == 0) {
                    roadView.setViewport(new Rectangle2D(-681 + (roadBuilder.getInventoryCount() - numberOfMovement / 4) * 18.9166 + 113.5, 0, 681, 69));
                    roadBuilderView.setViewport(new Rectangle2D(0, 0, 484, 323));
                    roadBuilderView.setTranslateX((300 - ((roadBuilder.getInventoryCount() - numberOfMovement / 4) * 18.9166 + 113.5) + 90));
                    --numberOfMovement;
                }
                animationDriving++;
            } else {
                showRoomObjects();
                roadbuilderMovingSound.AudioStop();
            }

        });
        timeline.setCycleCount(timeline.INDEFINITE);
        timeline.getKeyFrames().add(frame);
        timeline.play();
    }

    public void showRoomObjects() {
        if (roadBuilder.getInventoryCount() >= 30) {
            endGame();
        } else {
            showFarmer();
            showProfessor();
            showMechanic();
            if (Main.game.isRoom(Main.game.RoadBuild)) {
                roadView.setViewport(new Rectangle2D(-681 + (roadBuilder.getInventoryCount() * 18.9166 + 113.5), 0, 681, 69));
                roadBuilderView.setViewport(new Rectangle2D(0, 0, 484, 323));
                roadBuilderView.setTranslateX(300 - ((roadBuilder.getInventoryCount() * 18.9166 + 113.5) - 90));
            } else {
                roadView.setViewport(new Rectangle2D(-681, 0, 681, 69));
                roadBuilderView.setViewport(new Rectangle2D(-484, 0, 484, 323));
            }
        }

    }

    public void showProfessor() {
        professorNpc.setTranslateX(3000);
        if (Main.game.isRoom(Main.game.Sdu)) {
            professorNpc.setTranslateX(30);
        }
    }

    public void showMechanic() {
        mechanicNpc.setTranslateX(3000);
        if (Main.game.isRoom(Main.game.Town)) {
            mechanicNpc.setTranslateX(178);
            mechanicNpc.setTranslateY(50);
        }
    }

    public void showFarmer() {
        farmerNpc.setTranslateX(3000);
        if (Main.game.isRoom(Main.game.Farm)) {
            farmerNpc.setTranslateX(190);
            farmerNpc.setTranslateY(2);
        }
    }

    public void hideDialogBox() {
        spaceCount = 0;
        textLine1.setText("");
        textLine12.setText("");
        textLine13.setText("");
        playerText.setText("");
        dialogBox.setTranslateY(3000);
        talking = false;
        npcTalk.AudioStop();
    }

    public void showDialogBox() {
        textLine1.setTranslateY(-210);
        textLine12.setTranslateY(-190);
        textLine13.setTranslateY(-170);
        playerText.setTranslateY(-130);
        //Farmer
        if (Main.game.isRoom(Main.game.Farm)) {
            if (farmerTalked == 0) {
                if (spaceCount == 0 && !farmerTalk) {
                    npcTalk.musicPlayerInfinity();
                    talking = true;
                    talkNPC(textLine1, "farmer", 0);
                    talkNPC(textLine12, "farmer", 1);
                    talkNPC(textLine13, "farmer", 2);
                    spaceCount++;
                } else if (spaceCount == 1) {
                    talkNPC(playerText, "farmer", 3);
                    spaceCount++;
                } else if (spaceCount == 2) {
                    talkNPC(textLine1, "farmer", 4);
                    textLine12.setText("");
                    textLine13.setText("");
                    playerText.setText("");
                    spaceCount++;
                } else if (spaceCount == 3) {
                    talkNPC(playerText, "farmer", 5);
                    spaceCount++;
                } else if (spaceCount == 4) {
                    talkNPC(textLine1, "farmer", 6);
                    farmerTalk = playerObject.addPlasticInv();
                    if (!farmerTalk) {
                        talkNPC(textLine1, "farmer", 7);
                        playerText.setText("");
                    }
                    updateInventory();
                    spaceCount++;
                } else if (spaceCount == 5) {
                    if (!farmerTalk) {
                        hideDialogBox();
                        farmerTalked++;
                    } else if (farmerTalk) {
                        hideDialogBox();
                        farmerTalk = true;
                    }
                }
            } else if (farmerTalked > 0) {
                if (spaceCount == 0 && !farmerTalk) {
                    npcTalk.musicPlayerInfinity();
                    talking = true;
                    talkNPC(textLine1, "farmer", 8);
                    farmerTalk = playerObject.addPlasticInv();
                    updateInventory();
                    spaceCount++;
                } else if (spaceCount == 1) {
                    if (!farmerTalk) {
                        hideDialogBox();
                        spaceCount = 0;
                    } else if (farmerTalk) {
                        hideDialogBox();
                        farmerTalk = true;
                    }
                }
            }
            //Professor
        } else if (Main.game.isRoom(Main.game.Sdu)) {
            if (spaceCount == 0 && !professorTalk) {
                npcTalk.musicPlayerInfinity();
                talking = true;
                talkNPC(textLine1, "professor", 0);
                talkNPC(textLine12, "professor", 1);
                talkNPC(textLine13, "professor", 2);
                spaceCount++;
            } else if (spaceCount == 1) {
                talkNPC(playerText, "professor", 3);
                spaceCount++;
            } else if (spaceCount == 2) {
                talkNPC(textLine1, "professor", 4);
                textLine12.setText("");
                textLine13.setText("");
                playerText.setText("");
                spaceCount++;
            } else if (spaceCount == 3) {
                talkNPC(playerText, "professor", 5);
                spaceCount++;
            } else if (spaceCount == 4) {
                hideDialogBox();
                professorTalk = true;
            }
        }
        //Mechanic
        if (Main.game.isRoom(Main.game.Town)) {
            if (roadBuilder.getDamaged() > 0) {
                if (spaceCount == 0 && !mechanicTalk) {
                    npcTalk.musicPlayerInfinity();
                    talkNPC(textLine1, "mechanic", 0);
                    talkNPC(textLine12, "mechanic", 1);
                    spaceCount++;
                } else if (spaceCount == 1) {
                    talkNPC(playerText, "mechanic", 2);
                    spaceCount++;
                } else if (spaceCount == 2) {
                    talkNPC(textLine1, "mechanic", 3);
                    textLine12.setText("");
                    playerText.setText("");
                    spaceCount++;
                    playerObject.setToolset(mechanicObject.giveToolset());
                    lockToolSlot.setOpacity(0);
                    toolsetImg.setImage(new Image("file:" + playerObject.getToolset().getImage()));
                    toolsetImg.setTranslateX(650);
                    toolsetImg.setTranslateY(458);
                    toolsetImg.setFitHeight(60);
                    toolsetImg.setFitWidth(60);
                } else if (spaceCount == 3) {
                    hideDialogBox();
                    mechanicTalk = true;
                }
            } else if (roadBuilder.getDamaged() == 0) {
                    if (spaceCount == 0 && !mechanicTalk) {
                        talking = true;
                        npcTalk.musicPlayerInfinity();
                        talkNPC(textLine1, "mechanic", 4);
                        spaceCount++;
                    } else if (spaceCount == 1) {
                        hideDialogBox();
                        talking = false;
                        spaceCount = 0;
                    }
            }
        }

    }

    private void talkNPC(Text npcText, String npcType, int index) {
        dialogBox.setTranslateY(-170);
        npcText.setText(dialog.getNPCText(npcType, index));
    }

    public void hideSlotLines() {
        Line[] lines = {slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8, slot9};

        for (Line line : lines) {
            line.setOpacity(0);
        }
    }

    public void showSlotLines() {
        Line[] lines = {slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8, slot9};

        for (Line line : lines) {
            line.setOpacity(0.5);
        }
    }

    @FXML
    private ImageView plast1 = new ImageView();
    @FXML
    private ImageView plast2 = new ImageView();
    @FXML
    private ImageView plast3 = new ImageView();
    @FXML
    private ImageView plast4 = new ImageView();
    @FXML
    private ImageView plast5 = new ImageView();
    @FXML
    private ImageView plast6 = new ImageView();
    @FXML
    private ImageView plast7 = new ImageView();
    @FXML
    private ImageView plast8 = new ImageView();
    @FXML
    private ImageView plast9 = new ImageView();
    @FXML
    private ImageView plast10 = new ImageView();
    @FXML
    private ImageView plast11 = new ImageView();
    @FXML
    private ImageView plast12 = new ImageView();
    @FXML
    private ImageView plast13 = new ImageView();
    @FXML
    private ImageView plast14 = new ImageView();
    @FXML
    private ImageView plast15 = new ImageView();
    @FXML
    private ImageView plast16 = new ImageView();
    @FXML
    private ImageView plast17 = new ImageView();
    @FXML
    private ImageView plast18 = new ImageView();
    @FXML
    private ImageView plast19 = new ImageView();
    @FXML
    private ImageView plast20 = new ImageView();

    @FXML
    private ImageView item1;

    @FXML
    private ImageView item2;

    @FXML
    private ImageView item3;

    @FXML
    private ImageView item4;

    @FXML
    private ImageView item5;

    @FXML
    private ImageView item6;

    @FXML
    private ImageView item7;

    @FXML
    private ImageView item8;

    @FXML
    private ImageView item9;

    @FXML
    private ImageView item10;

    // De 9 slot linjer under inkaplser de 10 inventory pladser

    @FXML
    private Line slot1;

    @FXML
    private Line slot2;

    @FXML
    private Line slot3;

    @FXML
    private Line slot4;

    @FXML
    private Line slot5;

    @FXML
    private Line slot6;

    @FXML
    private Line slot7;

    @FXML
    private Line slot8;

    @FXML
    private Line slot9;
}