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
    private Label.LabelStyle errorStyle;
    private Label.LabelStyle hideLabelStyle;
    private Label.LabelStyle baseLabelStyle;
    private Label.LabelStyle colouredLabelStyle;
    private Label.LabelStyle gameLabelStyle;
    private Label.LabelStyle titleStyle;
    private Label.LabelStyle subtleStyle;
    private TextButton.TextButtonStyle baseButtonStyle;
    private TextButton.TextButtonStyle selectedButtonStyle;
    private TextButton.TextButtonStyle startButtonStyle;
    private TextButton.TextButtonStyle backButtonStyle;
    private TextButton.TextButtonStyle lessSubtleButtonStyle;
    private TextButton.TextButtonStyle subtleButtonStyle;
    private TextField.TextFieldStyle textFieldStyle;
    private Texture blank;

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
        titleStyle = createLabelStyle(titleFont, Color.CORAL);
        //create base label style
        baseLabelStyle = createLabelStyle(buttonFont, Color.WHITE);
        //create coloured label style
        colouredLabelStyle = createLabelStyle(buttonFont, Color.CORAL);
        //create error label style
        errorStyle = createLabelStyle(buttonFont, Color.RED);
        //create style to hide error label - set to black
        hideLabelStyle = createLabelStyle(buttonFont, Color.BLACK);
        //create style for game screen labels
        gameLabelStyle = createLabelStyle(gameLabelFont, Color.WHITE);
        //create style for subtle labels
        subtleStyle = createLabelStyle(buttonFont, Color.DARK_GRAY);
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
        cursorColor.setColor(Color.CORAL);
        cursorColor.fill();
        textFieldStyle.cursor = new Image(new Texture(cursorColor)).getDrawable();
        Pixmap textFieldColour = new Pixmap(150, 15, Pixmap.Format.RGB888);
        textFieldColour.setColor(Color.DARK_GRAY);
        textFieldColour.fill();
        textFieldStyle.background = new Image(new Texture(textFieldColour)).getDrawable();
        //create a style for basic buttons
        baseButtonStyle = createButtonStyle(buttonFont, Color.WHITE);
        //create a style for selected buttons
        selectedButtonStyle = createButtonStyle(buttonFont, Color.GRAY);
        //create a style for start buttons
        startButtonStyle = createButtonStyle(buttonFont, Color.CORAL);
        //create style for back buttons
        backButtonStyle = createButtonStyle(buttonFont, Color.SLATE);
        //create style for less subtle buttons
        lessSubtleButtonStyle = createButtonStyle(buttonFont, Color.GRAY);
        //create style for subtle buttons
        subtleButtonStyle = createButtonStyle(buttonFont, Color.DARK_GRAY);
        //load in blank texture for healthbar
        blank = new Texture(Gdx.files.internal("img/white.png"));
    }

    private Label.LabelStyle createLabelStyle(BitmapFont font, Color color) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = color;
        return labelStyle;
    }

    private TextButton.TextButtonStyle createButtonStyle(BitmapFont font, Color color) {
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = color;
        return buttonStyle;
    }

    public void addTableColour(Table table, Color color) {
        Pixmap tableColour = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        tableColour.setBlending(Pixmap.Blending.None);
        tableColour.setColor(color);
        tableColour.fill();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(tableColour))));
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
    public Label.LabelStyle getBaseLabelStyle() {
        return baseLabelStyle;
    }

    public Label.LabelStyle getColouredLabelStyle() {
        return colouredLabelStyle;
    }

    /**
     * Gets game label style.
     *
     * @return the game label style
     */
    public Label.LabelStyle getGameLabelStyle() {
        return gameLabelStyle;
    }

    public Label.LabelStyle getSubtleStyle() {
        return subtleStyle;
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
    public TextButton.TextButtonStyle getBaseButtonStyle() {
        return baseButtonStyle;
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

    public TextButton.TextButtonStyle getLessSubtleButtonStyle() {
        return lessSubtleButtonStyle;
    }

    public TextButton.TextButtonStyle getSubtleButtonStyle() {
        return subtleButtonStyle;
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
}
