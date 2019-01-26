package com.aticatac.menus;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


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
            LoadScene(FXMLLoader.load(getClass().getResource("/GameScene.fxml")));
        } catch (IOException e) {
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

