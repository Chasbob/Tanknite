package com.aticatac.client.util;

import com.aticatac.common.model.ServerInformation;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * The type Server button.
 */
public class ServerButton extends TextButton {
  private ServerInformation serverInformation;

  /**
   * Instantiates a new Server button.
   *
   * @param text              the text
   * @param skin              the skin
   * @param serverInformation the server information
   */
  public ServerButton(String text, TextButtonStyle skin, ServerInformation serverInformation) {
    super(text, skin);
    this.serverInformation = serverInformation;
  }

  /**
   * Instantiates a new Server button.
   *
   * @param text the text
   * @param skin the skin
   */
  public ServerButton(String text, Skin skin) {
    super(text, skin);
  }

  /**
   * Instantiates a new Server button.
   *
   * @param text      the text
   * @param skin      the skin
   * @param styleName the style name
   */
  public ServerButton(String text, Skin skin, String styleName) {
    super(text, skin, styleName);
  }

  /**
   * Instantiates a new Server button.
   *
   * @param text  the text
   * @param style the style
   */
  public ServerButton(String text, TextButtonStyle style) {
    super(text, style);
  }

  /**
   * Gets server information.
   *
   * @return the server information
   */
  public ServerInformation getServerInformation() {
    return serverInformation;
  }
}
