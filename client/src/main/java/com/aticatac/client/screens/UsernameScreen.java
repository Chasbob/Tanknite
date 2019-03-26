package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.client.util.ListenerFactory;
import com.aticatac.client.util.Styles;
import com.aticatac.common.model.ServerInformation;
import com.aticatac.common.model.Updates.Response;
import com.aticatac.server.networking.Server;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import java.net.InetAddress;

/**
 * The type Username screen.
 */
public class UsernameScreen extends AbstractScreen {
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
    super.buildStage();
    //create table for label, text field, submit button
    Table dataTable = new Table();
    super.addToRoot(dataTable);
    dataTable.center();
    dataTable.defaults().pad(10).width(200).height(30).center();
    //create error label
    errorLabel = Styles.INSTANCE.createErrorLabel();
    dataTable.add(errorLabel);
    dataTable.row();
    if (Data.INSTANCE.isManualConfigForServer() || Data.INSTANCE.isHosting()) {
      String text;
      if (Data.INSTANCE.isManualConfigForServer()) {
        text = "IP";
      } else {
        text = "NAME";
      }
      //create server label
      Label serverLabel = Styles.INSTANCE.createLabel("SERVER " + text);
      dataTable.add(serverLabel);
      dataTable.row();
      //create server textfield
      serverTextField = Styles.INSTANCE.createTextField("");
      dataTable.add(serverTextField);
      dataTable.row();
    }
    //create button for submit
    TextButton submitButton = Styles.INSTANCE.createButton("SUBMIT");
    dataTable.add(submitButton);
    //create custom listener for submit button to get text field text
    submitButton.addListener(ListenerFactory.newListenerEvent(() -> {
      if (!Data.INSTANCE.isHosting() && Screens.INSTANCE.getPreviousScreen() == MainMenuScreen.class) {
        //join single player server
        Response response = Data.INSTANCE.connect(Data.INSTANCE.getUsername(), true);
        switch (response) {
          case ACCEPTED:
            refresh();
            Screens.INSTANCE.showScreen(GameScreen.class);
            break;
          case INVALID_NAME:
            errorLabel.setText("INVALID USERNAME");
            errorLabel.setStyle(getErrorStyle());
            break;
        }
        return false;
      } else if (Screens.INSTANCE.getPreviousScreen() == ServerScreen.class || Data.INSTANCE.isHosting()) {
        Response response;
        if (Data.INSTANCE.isManualConfigForServer()) {
          //join server with ip
          response = Data.INSTANCE.connect(Data.INSTANCE.getUsername(), false, serverTextField.getText());
        } else if (Data.INSTANCE.isHosting()) {
          //create custom server
          Server server = new Server(false, serverTextField.getText());
          server.start();
          Data.INSTANCE.setSingleplayer(false);
          //TODO make getter for port and ip
          Data.INSTANCE.setCurrentInformation(new ServerInformation(Data.INSTANCE.getUsername(), InetAddress.getByName("127.0.0.1"), 5500, Server.ServerData.INSTANCE.getMaxPlayers(), Server.ServerData.INSTANCE.playerCount()));
          response = Data.INSTANCE.connect(Data.INSTANCE.getUsername(), false);
        } else {
          //join server previously collected
          response = Data.INSTANCE.connect(Data.INSTANCE.getUsername(), false);
        }
        switch (response) {
          case ACCEPTED:
            ListenerFactory.newChangeScreenAndReloadEvent(LobbyScreen.class);
            refresh();
            break;
          case TAKEN:
            errorLabel.setText("NAME TAKEN");
            errorLabel.setStyle(getErrorStyle());
            break;
          case NO_SERVER:
            errorLabel.setText("SERVER DOES NOT EXIST");
            errorLabel.setStyle(getErrorStyle());
            break;
          case INVALID_NAME:
            errorLabel.setText("INVALID USERNAME");
            errorLabel.setStyle(getErrorStyle());
            break;
          case FULL:
            errorLabel.setText("GAME FULL");
            errorLabel.setStyle(getErrorStyle());
            break;
        }
        return false;
      } else {
        return false;
      }
    }));
  }

  private Label.LabelStyle getErrorStyle() {
    return Styles.INSTANCE.createLabelStyle(Styles.INSTANCE.baseFont, Color.RED);
  }

  @Override
  public void refresh() {
    Data.INSTANCE.setHosting(false);
    Data.INSTANCE.setManualConfigForServer(false);
    errorLabel.setStyle(Styles.INSTANCE.createLabelStyle(Styles.INSTANCE.baseFont, Styles.INSTANCE.hiddenColour));
  }
}
