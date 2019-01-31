package com.aticatac.client.menus;

import com.aticatac.client.networking.singleplayer.Client;
import com.aticatac.common.model.Command;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;

public class UIController extends Application {
    private static final int width = 1000;
    private static final int height = 700;
    private static Stage stage;
    private static Client client;
    private final Logger logger = Logger.getLogger(getClass());

    @Override
    public void start(Stage primaryStage) throws Exception {
        logger.info("Starting");
        stage = primaryStage;
        client = new Client("test", "localhost", 9800);
        stage.setTitle("AticAtac");
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/MainMenuScene.fxml")), width, height));
        stage.show();
    }

    //scene functions
    public void loadMainMenu(ActionEvent actionEvent) {
        try {
            LoadScene(FXMLLoader.load(getClass().getResource("/MainMenuScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //helper functions
    private void LoadScene(Parent scene) {
        stage.setScene(new Scene(scene, width, height));
    }

    public void loadMultiPlayerScene(ActionEvent actionEvent) {
        try {
            LoadScene(FXMLLoader.load(getClass().getResource("/MultiplayerScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSettingsScene(ActionEvent actionEvent) {
        try {
            LoadScene(FXMLLoader.load(getClass().getResource("/SettingsScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadScoreScene(ActionEvent actionEvent) {
        try {
            LoadScene(FXMLLoader.load(getClass().getResource("/ScoreScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //singleplayer functions
    public void singlePlayer(ActionEvent actionEvent) {
        //TODO set up single player game may involve invoking server
        client.start();
        loadGameScene(actionEvent);
    }

    public void loadGameScene(ActionEvent actionEvent) {
        try {
            Group root = new Group();
            Scene Scene = new Scene(root, width, height);
            stage.setScene(Scene);
            Canvas canvas = new Canvas(width, height);
            root.getChildren().add(canvas);
            GraphicsContext gc = canvas.getGraphicsContext2D();
            Image tank = new Image("/73749645-pixel-military-tank-top.png");
            Image bullet = new Image("/bullet.png");
            Tank player = new Tank();
            player.SetImage(tank);
            ArrayList<String> input = new ArrayList<>();
            Scene.setOnKeyPressed(
                    e -> {
                        String code = e.getCode().toString();
                        // only add once... prevent duplicates
                        if (!input.contains(code))
                            input.add(code);
                    });
            Scene.setOnKeyReleased(
                    e -> {
                        String code = e.getCode().toString();
                        input.remove(code);
                    });
            ArrayList<Missile> mL = new ArrayList<>();
            final long startNanoTime = System.nanoTime();
            new AnimationTimer() {
                boolean onSpacePressed = false;

                public void handle(long currentNanoTime) {
                    update();
                    gc.clearRect(0, 0, width, height);
                    gc.setFill(Color.web("#000000"));
                    gc.fillRect(0, 0, width, height);
                    double t = (currentNanoTime - startNanoTime) / 1000000000.0;
                    if (input.contains("LEFT")) {
                        client.setCommand(Command.LEFT);
                        logger.info(Command.LEFT);
                    } else if (input.contains("RIGHT")) {
                        client.setCommand(Command.RIGHT);
                        logger.info(Command.RIGHT);
                    } else if (input.contains("UP")) {
                        client.setCommand(Command.UP);
                        logger.info(Command.UP);
                    } else if (input.contains("DOWN")) {
                        client.setCommand(Command.DOWN);
                        logger.info(Command.DOWN);
                    } else if (input.contains("SPACE") && !onSpacePressed) {
                        client.setCommand(Command.SHOOT);
                        client.interrupt();
                        logger.info(Command.SHOOT);
                    }
                    if (!input.contains("SPACE")) {
                        onSpacePressed = false;
                    }
                    for (Missile m : mL) {
                        m.Forward(10);
                        m.render(gc);
                    }
                    player.render(gc);
                }
            }.start();
            //root.getChildren().add(FXMLLoader.load(getClass().getResource("/GameScene.fxml")));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void update() {
        logger.trace("Updated");
    }

    //multiplayer functions
    public void hostGame(ActionEvent actionEvent) {
        //TODO set up server for hosting
        System.out.println("hosting game");
        loadGameScene(actionEvent);
    }

    public void joinGame(ActionEvent actionEvent) {
        //TODO join a server
        System.out.println("joining game");
        loadGameScene(actionEvent);
    }

    //toggle functions
    public void toggleSound(ActionEvent actionEvent) {
        //TODO toggle sound setting of game....setting needs to be saved after clicking or atleast for that session.
        System.out.println("sound toggled.");
    }

    public void toggleClearScores(ActionEvent actionEvent) {
        //TODO toggle clear scores setting of game...setting needs to be reset after clicking
        System.out.println("cleared scores.");
    }
}

