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

public enum Styles {
    INSTANCE;
    private TextField.TextFieldStyle textFieldStyle;
    private Texture blank;
    public BitmapFont baseFont;
    public BitmapFont titleFont;
    public BitmapFont gameLabelFont;
    public Color hiddenColour;
    public Color selectedColour;
    private Color accentColour;

    Styles() {
        System.out.println("Loading styles...");
        loadStyles();
        System.out.println("Loaded styles!");
    }

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
        //load in blank texture for healthbar
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
        System.out.println("Loaded ttf");
        FreeTypeFontGenerator.FreeTypeFontParameter parameter10 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter15 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter40 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter10.size = 15;
        parameter15.size = 25;
        parameter40.size = 70;
        gameLabelFont = generator.generateFont(parameter10);
        baseFont = generator.generateFont(parameter15);
        titleFont = generator_title.generateFont(parameter40);
        generator.dispose();
        generator_title.dispose();
    }

    public TextButton.TextButtonStyle createButtonStyle(BitmapFont font, Color color) {
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

    public Label.LabelStyle createLabelStyle(BitmapFont font, Color color) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = color;
        return labelStyle;
    }

    public TextField createTextField(String text) {
        return new TextField(text, textFieldStyle);
    }

    public Texture getBlank() {
        return blank;
    }

    public Label createTitleLabel() {
        return new Label("TANKNITE", createLabelStyle(INSTANCE.titleFont, accentColour));
    }

    public Label createLabel(String text) {
        return new Label(text, createLabelStyle(INSTANCE.baseFont, Color.WHITE));
    }

    public Label createGameLabel(String text) {
        return new Label(text, createLabelStyle(INSTANCE.gameLabelFont, Color.WHITE));
    }

    public Label createColouredLabel() {
        return new Label("WAITING FOR HOST", createLabelStyle(INSTANCE.baseFont, accentColour));
    }

    public Label createSubtleLabel(String text) {
        return new Label(text, createLabelStyle(INSTANCE.baseFont, Color.CYAN));
    }

    public Label createErrorLabel() {
        return new Label("NAME TAKEN", createLabelStyle(INSTANCE.baseFont, INSTANCE.hiddenColour));
    }

    public TextButton createBackButton(String text) {
        return new TextButton(text, createButtonStyle(INSTANCE.baseFont, Color.GRAY));
    }

    public TextButton createButton(String text) {
        return new TextButton(text, createButtonStyle(INSTANCE.baseFont, Color.WHITE));
    }

    public TextButton createStartButton(String text) {
        return new TextButton(text, createButtonStyle(INSTANCE.baseFont, accentColour));
    }

    public TextButton createPopButton(String text) {
        return new TextButton(text, createButtonStyle(INSTANCE.baseFont, Color.TEAL));
    }

    public TextButton createLessPopButton(String text) {
        return new TextButton(text, createButtonStyle(INSTANCE.baseFont, Color.CYAN));
    }
}
