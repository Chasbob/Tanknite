package com.aticatac.menus;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;


public class UIController extends Application {

    private static final int width = 1000;
    private static final int height = 700;

    private static Stage stage;


    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;

        stage.setTitle("AticAtac");
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/MainMenuScene.fxml")),width,height));
        stage.show();

    }

    //scene functions
    public void loadMainMenu(ActionEvent actionEvent)
    {
        try {
            LoadScene(FXMLLoader.load(getClass().getResource("/MainMenuScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGameScene(ActionEvent actionEvent)
    {
        try {

            Group root = new Group();

            Scene Scene = new Scene( root,width,height );
            stage.setScene(Scene);

            Canvas canvas = new Canvas(width, height);
            root.getChildren().add( canvas );

            GraphicsContext gc = canvas.getGraphicsContext2D();

            Image tank = new Image( "/73749645-pixel-military-tank-top.png" );

            Tank player = new Tank();
            player.SetImage(tank);

            ArrayList<String> input = new ArrayList<>();

            Scene.setOnKeyPressed(
                    e -> {
                        String code = e.getCode().toString();

                        // only add once... prevent duplicates
                        if ( !input.contains(code) )
                            input.add( code );
                    });

            Scene.setOnKeyReleased(
                    e -> {
                        String code = e.getCode().toString();
                        input.remove( code );
                    });



            final long startNanoTime = System.nanoTime();

            new AnimationTimer()
            {
                public void handle(long currentNanoTime)
                {
                    gc.clearRect(0, 0, width,height);
                    gc.setFill(Color.web("#000000"));
                    gc.fillRect(0,0,width,height);

                    double t = (currentNanoTime - startNanoTime) / 1000000000.0;

                    if (input.contains("LEFT")) {
                        player.Transform(-2,0);
                        player.SetRotation(-90);
                    } else if (input.contains("RIGHT")) {
                        player.Transform(2,0);
                        player.SetRotation(90);
                    }
                    else if (input.contains("UP")) {
                        player.Transform(0,-2);
                        player.SetRotation(0);
                    }
                    else if (input.contains("DOWN")) {
                        player.Transform(0,2);
                        player.SetRotation(180);
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

    public void loadMultiPlayerScene (ActionEvent actionEvent)
    {
        try {
            LoadScene(FXMLLoader.load(getClass().getResource("/MultiplayerScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSettingsScene(ActionEvent actionEvent)
    {
        try {
            LoadScene(FXMLLoader.load(getClass().getResource("/SettingsScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadScoreScene(ActionEvent actionEvent)
    {
        try {
            LoadScene(FXMLLoader.load(getClass().getResource("/ScoreScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //singleplayer functions
    public void singlePlayer(ActionEvent actionEvent){
        //TODO set up single player game may involve invoking server
        loadGameScene(actionEvent);
    }

    //multiplayer functions
    public void hostGame(ActionEvent actionEvent){
        //TODO set up server for hosting
        System.out.println("hosting game");
        loadGameScene(actionEvent);
    }

    public void joinGame(ActionEvent actionEvent){
        //TODO join a server
        System.out.println("joining game");
        loadGameScene(actionEvent);
    }

    //toggle functions
    public void toggleSound(ActionEvent actionEvent){
        //TODO toggle sound setting of game....setting needs to be saved after clicking or atleast for that session.
        System.out.println("sound toggled.");
    }

    public void toggleClearScores(ActionEvent actionEvent){
        //TODO toggle clear scores setting of game...setting needs to be reset after clicking
        System.out.println("cleared scores.");
    }

    //helper functions
    private void LoadScene (Parent scene) { stage.setScene(new Scene (scene,width,height)); }
}

