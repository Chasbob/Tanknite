package com.aticatac.client.screens;

import com.aticatac.client.util.Data;
import com.aticatac.client.util.Styles;
import com.aticatac.common.model.ServerInformation;
import com.aticatac.common.model.Updates.Response;
import com.aticatac.server.networking.Server;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import java.net.InetAddress;

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
    super.buildStage();
    //create table for label, text field, submit button
    Table dataTable = new Table();
    super.addToRoot(dataTable);
    dataTable.center();
    dataTable.defaults().pad(10).width(200).height(30).center();
    //create error label
    errorLabel = UIFactory.createErrorLabel("Name Taken");
    dataTable.add(errorLabel);
    dataTable.row();
    if (Data.INSTANCE.isManualConfigForServer() || Data.INSTANCE.isHosting()) {
      String text;
      if (Data.INSTANCE.isManualConfigForServer()) {
        text = "IP";
      } else {
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
      if (!Data.INSTANCE.isHosting() && Screens.INSTANCE.getPreviousScreen() == MainMenuScreen.class) {
        //join single player server
        Response response = Data.INSTANCE.connect(usernameTextField.getText(), true);
        switch (response) {
          case ACCEPTED:
            refresh();
            Screens.INSTANCE.showScreen(GameScreen.class);
            break;
          case INVALID_NAME:
            errorLabel.setText("Invalid Username");
            errorLabel.setStyle(Styles.INSTANCE.getErrorStyle());
            break;
        }
        return false;
      } else if (Screens.INSTANCE.getPreviousScreen() == ServerScreen.class || Data.INSTANCE.isHosting()) {
        Response response;
        if (Data.INSTANCE.isManualConfigForServer()) {
          //join server with ip
          response = Data.INSTANCE.connect(usernameTextField.getText(), false, serverTextField.getText());
        } else if (Data.INSTANCE.isHosting()) {
          //create custom server
          Server server = new Server(false, serverTextField.getText());
          server.start();
          Data.INSTANCE.setSingleplayer(false);
          //TODO make getter for port and ip
          Data.INSTANCE.setCurrentInformation(new ServerInformation(serverTextField.getText(), InetAddress.getByName("127.0.0.1"), 5500, Server.ServerData.INSTANCE.getMaxPlayers(), Server.ServerData.INSTANCE.playerCount()));
          response = Data.INSTANCE.connect(usernameTextField.getText(), false);
        } else {
          //join server previously collected
          response = Data.INSTANCE.connect(usernameTextField.getText(), false);
        }
        switch (response) {
          case ACCEPTED:
            refresh();
            Screens.INSTANCE.reloadScreen(LobbyScreen.class);
            Screens.INSTANCE.showScreen(LobbyScreen.class);
            break;
          case TAKEN:
            errorLabel.setText("Name Taken");
            errorLabel.setStyle(Styles.INSTANCE.getErrorStyle());
            break;
          case NO_SERVER:
            errorLabel.setText("Server does not exist");
            errorLabel.setStyle(Styles.INSTANCE.getErrorStyle());
            break;
          case INVALID_NAME:
            errorLabel.setText("Invalid Username");
            errorLabel.setStyle(Styles.INSTANCE.getErrorStyle());
            break;
          case FULL:
            errorLabel.setText("Game Full");
            errorLabel.setStyle(Styles.INSTANCE.getErrorStyle());
            break;
        }
        return false;
      } else {
        return false;
      }
    }));
  }

  @Override
  public void refresh() {
    errorLabel.setStyle(Styles.INSTANCE.getHideLabelStyle());
    usernameTextField.setText("");
  }
}
