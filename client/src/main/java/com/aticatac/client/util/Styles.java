package com.aticatac.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * The enum Styles.
 */
public enum Styles {
  /**
   * Instance styles.
   */
  INSTANCE;
  private Label.LabelStyle errorStyle;
  private Label.LabelStyle hideLabelStyle;
  private Label.LabelStyle labelStyle;
  private Label.LabelStyle gameLabelStyle;
  private Label.LabelStyle titleStyle;
  private TextButton.TextButtonStyle buttonStyle;
  private TextButton.TextButtonStyle selectedButtonStyle;
  private TextButton.TextButtonStyle startButtonStyle;
  private TextButton.TextButtonStyle backButtonStyle;
  private TextField.TextFieldStyle textFieldStyle;
  private Texture blank;
  private ImageButton refreshIcon;

  Styles() {
    System.out.println("Loading styles...");
    loadStyles();
    System.out.println("Loaded styles!");
  }

  /**
   * Gets instance.
   *
   * @return the instance
   */
  public static Styles getInstance() {
    return INSTANCE;
  }

  private void loadStyles() {
    //load in font for menu
    //String path = getClass().getResource("/styles/barcadebrawl.ttf").toString();
    //System.out.println("Path: " + path);
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("styles/barcadebrawl.ttf"));
    System.out.println("Loaded ttf");
    FreeTypeFontGenerator.FreeTypeFontParameter parameter10 = new FreeTypeFontGenerator.FreeTypeFontParameter();
    FreeTypeFontGenerator.FreeTypeFontParameter parameter15 = new FreeTypeFontGenerator.FreeTypeFontParameter();
    FreeTypeFontGenerator.FreeTypeFontParameter parameter40 = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter10.size = 10;
    parameter15.size = 15;
    parameter40.size = 40;
    BitmapFont gameLabelFont = generator.generateFont(parameter10);
    BitmapFont buttonFont = generator.generateFont(parameter15);
    BitmapFont titleFont = generator.generateFont(parameter40);
    generator.dispose();
    //create title label style
    titleStyle = new Label.LabelStyle();
    titleStyle.font = titleFont;
    titleStyle.fontColor = Color.FOREST;
    //create label style
    labelStyle = new Label.LabelStyle();
    labelStyle.font = buttonFont;
    labelStyle.fontColor = Color.WHITE;
    //create error label style
    errorStyle = new Label.LabelStyle();
    errorStyle.font = buttonFont;
    errorStyle.fontColor = Color.RED;
    //create style to hide error label - set to black
    hideLabelStyle = new Label.LabelStyle();
    hideLabelStyle.font = buttonFont;
    hideLabelStyle.fontColor = Color.BLACK;
    //create style for game screen labels
    gameLabelStyle = new Label.LabelStyle();
    gameLabelStyle.font = gameLabelFont;
    gameLabelStyle.fontColor = Color.WHITE;
    //create text field style with cursor
    textFieldStyle = new TextField.TextFieldStyle();
    textFieldStyle.font = buttonFont;
    textFieldStyle.fontColor = Color.WHITE;
    Label.LabelStyle cursorStyle = new Label.LabelStyle();
    cursorStyle.font = buttonFont;
    Label cursorImage = new Label("|", cursorStyle);
    Pixmap cursorColor = new Pixmap((int) cursorImage.getWidth(),
    (int) cursorImage.getHeight(),
    Pixmap.Format.RGB888);
    cursorColor.setColor(Color.FOREST);
    cursorColor.fill();
    textFieldStyle.cursor = new Image(new Texture(cursorColor)).getDrawable();
    Pixmap textFieldColour = new Pixmap(150, 15, Pixmap.Format.RGB888);
    textFieldColour.setColor(Color.DARK_GRAY);
    textFieldColour.fill();
    textFieldStyle.background = new Image(new Texture(textFieldColour)).getDrawable();
    //create a style for buttons
    buttonStyle = new TextButton.TextButtonStyle();
    buttonStyle.font = buttonFont;
    buttonStyle.fontColor = Color.WHITE;
    //create a style for selected buttons
    selectedButtonStyle = new TextButton.TextButtonStyle();
    selectedButtonStyle.font = buttonFont;
    selectedButtonStyle.fontColor = Color.GRAY;
    //create a style for start buttons
    startButtonStyle = new TextButton.TextButtonStyle();
    startButtonStyle.font = buttonFont;
    startButtonStyle.fontColor = Color.FOREST;
    //create style for back buttons
    backButtonStyle = new TextButton.TextButtonStyle();
    backButtonStyle.font = buttonFont;
    backButtonStyle.fontColor = Color.YELLOW;
    //load in blank texture for healthbar
    blank = new Texture(Gdx.files.internal("img/white.png"));
    //load in refresh icon
    //Texture refreshTexture = new Texture(Gdx.files.internal("img/refresh.png"));
    // Drawable drawable = new TextureRegionDrawable(new TextureRegion(refreshTexture));
    //refreshIcon = new ImageButton(drawable);
  }

  /**
   * Gets error style.
   *
   * @return the error style
   */
  public Label.LabelStyle getErrorStyle() {
    return errorStyle;
  }

  /**
   * Gets hide label style.
   *
   * @return the hide label style
   */
  public Label.LabelStyle getHideLabelStyle() {
    return hideLabelStyle;
  }

  /**
   * Gets label style.
   *
   * @return the label style
   */
  public Label.LabelStyle getLabelStyle() {
    return labelStyle;
  }

  /**
   * Gets game label style.
   *
   * @return the game label style
   */
  public Label.LabelStyle getGameLabelStyle() {
    return gameLabelStyle;
  }

  /**
   * Gets title style.
   *
   * @return the title style
   */
  public Label.LabelStyle getTitleStyle() {
    return titleStyle;
  }

  /**
   * Gets button style.
   *
   * @return the button style
   */
  public TextButton.TextButtonStyle getButtonStyle() {
    return buttonStyle;
  }

  /**
   * Gets selected button style.
   *
   * @return the selected button style
   */
  public TextButton.TextButtonStyle getSelectedButtonStyle() {
    return selectedButtonStyle;
  }

  /**
   * Gets start button style.
   *
   * @return the start button style
   */
  public TextButton.TextButtonStyle getStartButtonStyle() {
    return startButtonStyle;
  }

  /**
   * Gets back button style.
   *
   * @return the back button style
   */
  public TextButton.TextButtonStyle getBackButtonStyle() {
    return backButtonStyle;
  }

  /**
   * Gets text field style.
   *
   * @return the text field style
   */
  public TextField.TextFieldStyle getTextFieldStyle() {
    return textFieldStyle;
  }

  /**
   * Gets blank.
   *
   * @return the blank
   */
  public Texture getBlank() {
    return blank;
  }

  /**
   * Gets refresh button.
   *
   * @return the refresh button
   */
  public ImageButton getRefreshButton() {
    return refreshIcon;
  }
}
