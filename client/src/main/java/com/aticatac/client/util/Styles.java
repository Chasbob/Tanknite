package com.aticatac.client.util;

import com.aticatac.client.game.GDXGame;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public enum Styles {
    INSTANCE;
    private Label.LabelStyle errorStyle;
    private Label.LabelStyle hideLabelStyle;
    private Label.LabelStyle labelStyle;
    private Label.LabelStyle titleStyle;
    private TextButton.TextButtonStyle buttonStyle;
    private TextButton.TextButtonStyle selectedButtonStyle;
    private TextButton.TextButtonStyle startButtonStyle;
    private TextButton.TextButtonStyle backButtonStyle;
    private TextField.TextFieldStyle textFieldStyle;
    private Texture blank;

    Styles() {
        System.out.println("Loading styles...");
        loadStyles();
        System.out.println("Loaded styles!");
    }

    private void loadStyles() {
        //load in font for menu
        //String path = getClass().getResource("/styles/barcadebrawl.ttf").toString();
        //System.out.println("Path: " + path);
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("styles/barcadebrawl.ttf"));
        System.out.println("Loaded ttf");
        FreeTypeFontGenerator.FreeTypeFontParameter parameter15 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter40 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter15.size = 15;
        parameter40.size = 40;
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
    }

    public static Styles getInstance() {
        return INSTANCE;
    }

    public Label.LabelStyle getErrorStyle() {
        return errorStyle;
    }

    public Label.LabelStyle getHideLabelStyle() {
        return hideLabelStyle;
    }

    public Label.LabelStyle getLabelStyle() {
        return labelStyle;
    }

    public Label.LabelStyle getTitleStyle() {
        return titleStyle;
    }

    public TextButton.TextButtonStyle getButtonStyle() {
        return buttonStyle;
    }

    public TextButton.TextButtonStyle getSelectedButtonStyle() {
        return selectedButtonStyle;
    }

    public TextButton.TextButtonStyle getStartButtonStyle() {
        return startButtonStyle;
    }

    public TextButton.TextButtonStyle getBackButtonStyle() {
        return backButtonStyle;
    }

    public TextField.TextFieldStyle getTextFieldStyle() {
        return textFieldStyle;
    }

    public Texture getBlank() {
        return blank;
    }
}
