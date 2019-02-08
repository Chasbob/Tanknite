package com.gdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gdx.screens.MainMenuScreen;


public class GDXGame extends com.badlogic.gdx.Game {

    @Override
    public void create() {

        //load in font for menu
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("styles/ARCADECLASSIC.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter15 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter50 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter15.size = 15;
        parameter50.size = 50;
        BitmapFont buttonFont = generator.generateFont(parameter15);
        BitmapFont titleFont = generator.generateFont(parameter50);
        generator.dispose();

        //add main menu label to root, creating styling for it as well
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        Color titleColour = new Color(0, 255, 0, 1);
        titleStyle.fontColor = titleColour;

        //create a style for buttons
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = buttonFont;

        //load menu screen
        //TODO load in splash screen instead
        setScreen(new MainMenuScreen(this, titleStyle, buttonStyle));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }
}
