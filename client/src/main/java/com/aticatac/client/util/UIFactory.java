package com.aticatac.client.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;

/**
 * The type Ui factory.
 */
public class UIFactory {

    public Label.LabelStyle errorStyle;
    public Label.LabelStyle hideErrorStyle;

    private Label.LabelStyle labelStyle;
    private Label.LabelStyle titleStyle;
    private TextButton.TextButtonStyle buttonStyle;
    private TextButton.TextButtonStyle backButtonStyle;
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
        hideErrorStyle = new Label.LabelStyle();
        hideErrorStyle.font = buttonFont;
        hideErrorStyle.fontColor = Color.BLACK;

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

        //create style for back buttons
        backButtonStyle = new TextButton.TextButtonStyle();
        backButtonStyle.font = buttonFont;
        backButtonStyle.fontColor = Color.CORAL;

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
     * Create error label label.
     *
     * @param text the text
     * @return the label
     */
    public Label createErrorLabel(String text) {
        return new Label(text, hideErrorStyle);
    }

    public Label createLabel(String text) {
        return new Label(text, labelStyle);
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

    public TextButton createBackButton(String text) {
        return new TextButton(text, backButtonStyle);
    }

    /**
     * Create listener input listener.
     *
     * @param dstScreen the dst screen
     * @param uiFactory the ui factory
     * @return the input listener
     */
    public InputListener createListener(final ScreenEnum dstScreen, final ScreenEnum senderScreen, final UIFactory uiFactory) {
        return new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x,
                                     float y, int pointer, int button) {
                ScreenManager.getInstance().showScreen(dstScreen, senderScreen, uiFactory);
                return false;
            }
        };
    }

}
