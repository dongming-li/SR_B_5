package edu.iastate.a309.darkplatform.menu;

import edu.iastate.a309.darkplatform.display.Screen;
import edu.iastate.a309.darkplatform.graphics.Sprite;
import edu.iastate.a309.darkplatform.entity.Button;
import edu.iastate.a309.darkplatform.utility.Vector2f;
import edu.iastate.a309.darkplatform.input.InputHandler;

import java.awt.*;


/**
 * @author Alexander Lee
 * Class that handles all GUI rendered outside of gameplay, a.k.a. menus
 */
public class Menu {
    /**
     * Array of sprites to render into the menu
     */
    private Sprite[] sprites;

    /**
     * Array of sprite render positions
     */
    private Vector2f[] spriteVectors;

    /**
     * Array of buttons to render into the menu
     */
    private Button[] buttons;

    /**
     * Array of button render positions
     */
    private Vector2f[] buttonVectors;

    /**
     * Length of sprite array
     */
    private int spriteLength;

    /**
     * Length of button array
     */
    private int buttonLength;

    /**
     * Initialize menu: accept arrays of sprites/buttons and their respective render positions
     * @param sprite Pre-made array of sprites to be imported into the Menu object
     * @param spritePos Pre-made array of sprite positions to be imported into the Menu object
     */
    public Menu(Sprite[] sprite, Vector2f[] spritePos, Button[] button, Vector2f[] buttonPos) {
        spriteLength = sprite.length;
        sprites = new Sprite[spriteLength];
        spriteVectors = new Vector2f[spriteLength];
        for (int i = 0; i < spriteLength; i++) {
            sprites[i] = sprite[i];
            spriteVectors[i] = spritePos[i];
        }

        buttonLength = button.length;
        buttons = new Button[buttonLength];
        buttonVectors = new Vector2f[buttonLength];
        for (int i = 0; i < buttonLength; i++) {
            buttons[i] = button[i];
            buttonVectors[i] = buttonPos[i];
        }
    }

    /**
     * Interact with objects; use only when their default update() methods do not work as desired
     * @param input Input device (just mouse for now)
     */
    public void update(InputHandler input){

    }

    /**
     * Method to render all objects into the screen in their appropriate positions
     * @param screen Screen to render things into
     */
    public void render(Screen screen) {
        for (int i = 0; i < spriteLength; i++) {
            screen.render(sprites[i], spriteVectors[i]);
        }

        for (int i = 0; i < buttonLength; i++) {
            buttons[i].render(screen);
        }
    }

    /**
     * Method to render all objects into the screen in their appropriate positions,
     * WILL WIPE OUT ALL EXISTING ASSETS ON-SCREEN BEFORE DOING SO!!!
     * @param screen Screen to render things into
     */
    public void clearRender(Screen screen) {
        screen.clear(); //assuming this is even the right method; ***ASK JOSH***
        for (int i = 0; i < spriteLength; i++) {
            screen.render(sprites[i], spriteVectors[i]);
        }
    }
}
