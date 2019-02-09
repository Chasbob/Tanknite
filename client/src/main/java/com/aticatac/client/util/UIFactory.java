package com.aticatac.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class UIFactory {

    private Label.LabelStyle titleStyle;
    private TextButton.TextButtonStyle buttonStyle;

    public UIFactory() {
        this.loadStyles();
    }

    private void loadStyles() {

        //load in font for menu
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("styles/ARCADECLASSIC.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter15 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter parameter50 = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter15.size = 15;
        parameter50.size = 50;
        BitmapFont buttonFont = generator.generateFont(parameter15);
        BitmapFont titleFont = generator.generateFont(parameter50);
        generator.dispose();

        //create title label style
        titleStyle = new Label.LabelStyle();
        titleStyle.font = titleFont;
        Color titleColour = new Color(0, 255, 0, 1);
        titleStyle.fontColor = titleColour;

        //create a style for buttons
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = buttonFont;

    }

    public Label createLabel(String text) {
        return new Label(text, titleStyle);
    }

    public TextButton createButton(String text) {
        return new TextButton(text, buttonStyle);
    }

    public InputListener createListener(final ScreenEnum dstScreen, final Object... params) {
        return
                new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x,
                                             float y, int pointer, int button) {
                        ScreenManager.getInstance().showScreen(dstScreen, params);
                        return false;
                    }
                };
    }

}
