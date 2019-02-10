package com.aticatac.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

/**
 * The type Ui factory.
 */
public class UIFactory {

    private Label.LabelStyle titleStyle;
    private TextButton.TextButtonStyle buttonStyle;
    private TextField.TextFieldStyle textFieldStyle;

    /**
     * Instantiates a new Ui factory.
     */
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

        //create text field style
        textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.font = buttonFont;
        textFieldStyle.fontColor = Color.WHITE;

        //create a style for buttons
        buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = buttonFont;

    }

    /**
     * Create title label label.
     *
     * @param text the text
     * @return the label
     */
    public Label createTitleLabel(String text) {
        return new Label(text, titleStyle);
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
     * Create button text button.
     *
     * @param text the text
     * @return the text button
     */
    public TextButton createButton(String text) {
        return new TextButton(text, buttonStyle);
    }

    /**
     * Create listener input listener.
     *
     * @param dstScreen the dst screen
     * @param params    the params
     * @return the input listener
     */
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
