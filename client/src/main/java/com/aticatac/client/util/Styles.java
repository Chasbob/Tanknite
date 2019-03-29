package com.aticatac.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * The enum Styles.
 */
public enum Styles {
  /**
   * Instance styles.
   */
  INSTANCE;
  private TextField.TextFieldStyle textFieldStyle;
  private Texture blank;
  /**
   * The Base font.
   */
  public BitmapFont baseFont;
  /**
   * The Title font.
   */
  public BitmapFont titleFont;
  /**
   * The Small font.
   */
  public BitmapFont smallFont;
  /**
   * The Italic font.
   */
  public BitmapFont italicFont;
  /**
   * The Big menu font.
   */
  public BitmapFont bigMenuFont;
  /**
   * The Hidden colour.
   */
  public Color hiddenColour;
  /**
   * The Selected colour.
   */
  public Color selectedColour;
  private Color accentColour;
  private Color transparentColour;

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
    //load in font
    loadFonts();
    //create text field style with cursor
    createTextFieldStyle();
    //assign colours
    hiddenColour = Color.valueOf("363636");
    selectedColour = Color.CYAN;
    accentColour = Color.CORAL;
    transparentColour = new Color(new Color(0f, 0f, 0f, 0.25f));
    //load in blank texture for health bar
    blank = new Texture(Gdx.files.internal("img/white.png"));
  }

  private void createTextFieldStyle() {
    textFieldStyle = new TextField.TextFieldStyle();
    textFieldStyle.font = baseFont;
    textFieldStyle.fontColor = Color.WHITE;
    Label.LabelStyle cursorStyle = new Label.LabelStyle();
    cursorStyle.font = baseFont;
    Label cursorImage = new Label("|", cursorStyle);
    Pixmap cursorColor = new Pixmap((int) cursorImage.getWidth(),
      (int) cursorImage.getHeight(),
      Pixmap.Format.RGB888);
    cursorColor.setColor(Color.CORAL);
    cursorColor.fill();
    textFieldStyle.cursor = new Image(new Texture(cursorColor)).getDrawable();
    Pixmap textFieldColour = new Pixmap(150, 15, Pixmap.Format.RGB888);
    textFieldColour.setColor(Color.DARK_GRAY);
    textFieldColour.fill();
    textFieldStyle.background = new Image(new Texture(textFieldColour)).getDrawable();
  }

  private void loadFonts() {
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("styles/menu_font.ttf"));
    FreeTypeFontGenerator generator_title = new FreeTypeFontGenerator(Gdx.files.internal("styles/title_font.ttf"));
    FreeTypeFontGenerator generator_title_2 = new FreeTypeFontGenerator(Gdx.files.internal("styles/menu_font.ttf"));
    FreeTypeFontGenerator generator_italic_bold = new FreeTypeFontGenerator(Gdx.files.internal("styles/menu_font_italic_bold.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter10 = new FreeTypeFontGenerator.FreeTypeFontParameter();
    FreeTypeFontGenerator.FreeTypeFontParameter parameter15 = new FreeTypeFontGenerator.FreeTypeFontParameter();
    FreeTypeFontGenerator.FreeTypeFontParameter parameter40 = new FreeTypeFontGenerator.FreeTypeFontParameter();
    FreeTypeFontGenerator.FreeTypeFontParameter paramerer40_2 = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter10.size = 10;
    parameter15.size = 15;
    parameter40.size = 70;
    paramerer40_2.size = 70;
    smallFont = generator.generateFont(parameter10);
    baseFont = generator.generateFont(parameter15);
    titleFont = generator_title.generateFont(parameter40);
    italicFont = generator_italic_bold.generateFont(parameter10);
    bigMenuFont = generator_title_2.generateFont(paramerer40_2);
    generator.dispose();
    generator_title.dispose();
    generator_italic_bold.dispose();
    generator_title_2.dispose();
  }

  /**
   * Create button style text button . text button style.
   *
   * @param font  the font
   * @param color the color
   * @return the text button . text button style
   */
  public TextButton.TextButtonStyle createButtonStyle(BitmapFont font, Color color) {
    TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
    buttonStyle.font = font;
    buttonStyle.fontColor = color;
    return buttonStyle;
  }

  /**
   * Add table colour.
   *
   * @param table the table
   * @param color the color
   */
  public void addTableColour(Table table, Color color) {
    Pixmap tableColour = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    tableColour.setBlending(Pixmap.Blending.None);
    tableColour.setColor(color);
    tableColour.fill();
    table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(tableColour))));
  }

  /**
   * Create label style label . label style.
   *
   * @param font  the font
   * @param color the color
   * @return the label . label style
   */
  public Label.LabelStyle createLabelStyle(BitmapFont font, Color color) {
    Label.LabelStyle labelStyle = new Label.LabelStyle();
    labelStyle.font = font;
    labelStyle.fontColor = color;
    return labelStyle;
  }

  /**
   * Create menu table menu table.
   *
   * @param selected the selected
   * @param tab      the tab
   * @return the menu table
   */
  public MenuTable createMenuTable(boolean selected, boolean tab) {
    MenuTable table = new MenuTable(tab);
    if (selected) {
      table.setShowGroup(true);
    } else {
      table.setShowGroup(false);
    }
    table.defaults().padTop(5).padBottom(5).padLeft(10).padRight(10);
    return table;
  }

  /**
   * Create pop up table table.
   *
   * @return the table
   */
  public Table createPopUpTable() {
    Table popUpTable = new Table();
    addTableColour(popUpTable, transparentColour);
    popUpTable.defaults().padTop(5).padBottom(5).padLeft(10).padRight(10);
    return popUpTable;
  }


  /**
   * Create text field text field.
   *
   * @param text the text
   * @return the text field
   */
  public TextField createTextField(String text) {
    return new TextField(text, textFieldStyle);
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
   * Create title label label.
   *
   * @return the label
   */
  public Label createTitleLabel() {
    return new Label("TANKNITE", createLabelStyle(titleFont, accentColour));
  }

  /**
   * Create label label.
   *
   * @param text the text
   * @return the label
   */
  public Label createLabel(String text) {
    return new Label(text, createLabelStyle(baseFont, Color.WHITE));
  }

  /**
   * Create custom label label.
   *
   * @param text  the text
   * @param color the color
   * @return the label
   */
  public Label createCustomLabel(String text, Color color) {
    return new Label(text, createLabelStyle(baseFont, color));
  }

  /**
   * Create custom label with font label.
   *
   * @param font  the font
   * @param text  the text
   * @param color the color
   * @return the label
   */
  public Label createCustomLabelWithFont(BitmapFont font, String text, Color color) {
    return new Label(text, createLabelStyle(font, color));
  }

  /**
   * Create small label label.
   *
   * @param text the text
   * @return the label
   */
  public Label createSmallLabel(String text) {
    return new Label(text, createLabelStyle(smallFont, Color.WHITE));
  }

  /**
   * Create italic label label.
   *
   * @param text the text
   * @return the label
   */
  public Label createItalicLabel(String text) {
    return new Label(text, createLabelStyle(italicFont, Color.WHITE));
  }

  /**
   * Create back button text button.
   *
   * @param text the text
   * @return the text button
   */
  public TextButton createBackButton(String text) {
    return new TextButton(text, createButtonStyle(baseFont, Color.WHITE));
  }

  /**
   * Create button text button.
   *
   * @param text the text
   * @return the text button
   */
  public TextButton createButton(String text) {
    return new TextButton(text, createButtonStyle(baseFont, Color.WHITE));
  }

  /**
   * Create italic button text button.
   *
   * @param text the text
   * @return the text button
   */
  public TextButton createItalicButton(String text) {
    return new TextButton(text, createButtonStyle(italicFont, Color.WHITE));
  }

  /**
   * Create vertical group vertical group.
   *
   * @return the vertical group
   */
  public VerticalGroup createVerticalGroup() {
    VerticalGroup verticalGroup = new VerticalGroup();
    verticalGroup.columnLeft();
    verticalGroup.space(10);
    return verticalGroup;
  }

  /**
   * Gets transparent colour.
   *
   * @return the transparent colour
   */
  public Color getTransparentColour() {
    return transparentColour;
  }
}
