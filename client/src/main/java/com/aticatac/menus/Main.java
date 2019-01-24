package com.aticatac.menus;

import javafx.stage.Stage;

public class Main {

    public static void main(String[] args) {
        UIController UI = new UIController();
        try {
            UI.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
