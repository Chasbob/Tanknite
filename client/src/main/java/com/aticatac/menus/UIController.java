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
    private static final int height = 1000;

    private static Stage stage;


    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;

        stage.setTitle("AticAtac");
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/MainMenuScene.fxml")),width,height));
        stage.show();

    }

    public void LoadMainMenu(ActionEvent actionEvent)
    {
        try {
            LoadScene(FXMLLoader.load(getClass().getResource("/MainMenuScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSinglePlayerScene (ActionEvent actionEvent)
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
            LoadScene(FXMLLoader.load(getClass().getResource("/GameScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadSettingsScene (ActionEvent actionEvent)
    {
        try {
            LoadScene(FXMLLoader.load(getClass().getResource("/SettingsScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LoadHighScoreScene (ActionEvent actionEvent)
    {
        try {
            LoadScene(FXMLLoader.load(getClass().getResource("/HighScoreScene.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //helper functions
    private void LoadScene (Parent scene) { stage.setScene(new Scene (scene,width,height)); }
}

