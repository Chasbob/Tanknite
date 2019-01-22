package com.aticatac.menus;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(Main.class.getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("AticAtac");
        primaryStage.setScene(new Scene(root, 1000, 1000));
        primaryStage.show();
    }
}
