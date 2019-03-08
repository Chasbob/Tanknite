package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.client.util.Styles;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * The type Username screen.
 */
public class UsernameScreen extends AbstractScreen {
  /**
   * Instantiates a new Username screen.
   */
  UsernameScreen() {
    super();
  }

  @Override
  public void buildStage() {
    //create root table
    Table rootTable = new Table();
    rootTable.setFillParent(true);
    addActor(rootTable);
    //create table for label, text field, submit button
    Table usernameTable = new Table();
    rootTable.addActor(usernameTable);
    usernameTable.setFillParent(true);
    usernameTable.center();
    usernameTable.defaults().pad(10).width(200).height(30).center();
    //create guidance label
    Label guidanceLabel = UIFactory.createLabel("Enter username");
    usernameTable.add(guidanceLabel);
    usernameTable.row();
    //create error label
    Label nameTakenLabel = UIFactory.createErrorLabel("Name Taken");
    usernameTable.add(nameTakenLabel);
    usernameTable.row();
    //create text field
    TextField textField = UIFactory.createTextField("");
    usernameTable.add(textField);
    //create button for submit
    TextButton submitButton = UIFactory.createButton("Submit");
    usernameTable.add(submitButton);
    //create custom listener for submit button to get text field text
    submitButton.addListener(UIFactory.newListenerEvent(() -> {
      if (Screens.INSTANCE.getPreviousScreen() == MainMenuScreen.class) {
        boolean accepted = Data.INSTANCE.connect(textField.getText(), true);
        if (accepted) {
          nameTakenLabel.setStyle(Styles.INSTANCE.getHideLabelStyle());
          Screens.INSTANCE.showScreen(GameScreen.class);
        } else {
          nameTakenLabel.setStyle(Styles.INSTANCE.getErrorStyle());
        }
        return false;
      } else if (Screens.INSTANCE.getPreviousScreen() == ServerScreen.class || Screens.INSTANCE.getPreviousScreen() == MultiplayerScreen.class) {
        Data.INSTANCE.setCurrentInformation(Data.INSTANCE.getLocalhost());
        boolean accepted = Data.INSTANCE.connect(textField.getText(), false);
        if (accepted) {
          nameTakenLabel.setStyle(Styles.INSTANCE.getHideLabelStyle());
          Screens.INSTANCE.showScreen(LobbyScreen.class);
        } else {
          nameTakenLabel.setStyle(Styles.INSTANCE.getErrorStyle());
        }
        return false;
      } else {
        return false;
      }
    }));
    //create table to store back button
    Table backTable = new Table();
    backTable.setFillParent(true);
    rootTable.addActor(backTable);
    backTable.bottom();
    //create back button
    TextButton backButton = UIFactory.createBackButton("quit");
    backTable.add(backButton).bottom().padBottom(10);
//        backButton.addListener(UIFactory.newListenerEvent(() -> {
//            Screens.INSTANCE.setSingleplayer(false);
//            return false;
//        }));
    backButton.addListener(UIFactory.newChangeScreenEvent(MainMenuScreen.class));
  }
}
