package com.aticatac.client.util;

import com.aticatac.common.model.ServerInformation;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ServerButton extends TextButton {
  private ServerInformation serverInformation;

  public ServerButton(String text, TextButtonStyle skin, ServerInformation serverInformation) {
    super(text, skin);
    this.serverInformation = serverInformation;
  }

  public ServerButton(String text, Skin skin) {
    super(text, skin);
  }

  public ServerButton(String text, Skin skin, String styleName) {
    super(text, skin, styleName);
  }

  public ServerButton(String text, TextButtonStyle style) {
    super(text, style);
  }

  public ServerInformation getServerInformation() {
    return serverInformation;
  }
}
