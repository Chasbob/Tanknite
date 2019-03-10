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

  private TextField usernameTextField;
  private TextField serverTextField;
  private Label errorLabel;
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
    Table dataTable = new Table();
    rootTable.addActor(dataTable);
    dataTable.setFillParent(true);
    dataTable.center();
    dataTable.defaults().pad(10).width(200).height(30).center();
    //create error label
    errorLabel = UIFactory.createErrorLabel("Name Taken");
    dataTable.add(errorLabel);
    dataTable.row();
    if(Screens.INSTANCE.getScreen(ServerScreen.class).isManualConfig() || Screens.INSTANCE.getScreen(MultiplayerScreen.class).isHosting()){
      String text;
      if(Screens.INSTANCE.getScreen(ServerScreen.class).isManualConfig()){
        text = "IP";
      }else{
        text = "Name";
      }
      //create server label
      Label serverLabel = UIFactory.createLabel("Server " + text);
      dataTable.add(serverLabel);
      dataTable.row();
      //create server textfield
      serverTextField = UIFactory.createTextField("");
      dataTable.add(serverTextField);
      dataTable.row();
    }
    //create username label
    Label usernameLabel = UIFactory.createLabel("Username");
    dataTable.add(usernameLabel);
    dataTable.row();
    //create text field for username
    usernameTextField = UIFactory.createTextField("");
    dataTable.add(usernameTextField);
    //create button for submit
    TextButton submitButton = UIFactory.createButton("Submit");
    dataTable.add(submitButton);
    //create custom listener for submit button to get text field text
    submitButton.addListener(UIFactory.newListenerEvent(() -> {
      if (Screens.INSTANCE.getPreviousScreen() == MainMenuScreen.class) {
        boolean accepted = Data.INSTANCE.connect(usernameTextField.getText(), true);
        if (accepted) {
          refresh();
          Screens.INSTANCE.showScreen(GameScreen.class);
        } else {
          errorLabel.setStyle(Styles.INSTANCE.getErrorStyle());
        }
        return false;
      } else if (Screens.INSTANCE.getPreviousScreen() == ServerScreen.class || Screens.INSTANCE.getPreviousScreen() == MultiplayerScreen.class) {
        boolean accepted = Data.INSTANCE.connect(usernameTextField.getText(), false);
        if (accepted) {
          refresh();
          Screens.INSTANCE.showScreen(LobbyScreen.class);
        } else {
          errorLabel.setStyle(Styles.INSTANCE.getErrorStyle());
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
    TextButton backButton = UIFactory.createBackButton("quit");;
    backTable.add(backButton).bottom().padBottom(10);
    backButton.addListener(UIFactory.newChangeScreenEvent(MainMenuScreen.class));
  }

  @Override
  public void refresh() {
    errorLabel.setStyle(Styles.INSTANCE.getHideLabelStyle());
    usernameTextField.setText("");
  }
}
