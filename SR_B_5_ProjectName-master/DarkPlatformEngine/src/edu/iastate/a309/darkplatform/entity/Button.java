package edu.iastate.a309.darkplatform.entity;

import edu.iastate.a309.darkplatform.menu.Menu;
import edu.iastate.a309.darkplatform.display.Screen;
import edu.iastate.a309.darkplatform.graphics.ArtManager;
import edu.iastate.a309.darkplatform.graphics.Sprite;
import edu.iastate.a309.darkplatform.input.InputHandler;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.awt.*;

/**
 * @author Alexander Lee
 * Class of an interactable object that performs miscellaneous tasks
 */
public class Button extends Entity {

    /**
     * Menu that opens when button is pressed
     */
    private Menu menu;

    /**
     * Screen that opens/closes when button is pressed
     */
    private Screen screen;

    /**
     * Button type with the following params (subject to change):
     * Value -1: close type (closes a screen)
     * Value 0: color type (changes button's color)
     * Value 1: menu type (directs to a whole new menu)
     * Value 2: screen type (opens a new screen that overlays any existing screens, i.e. a pop-up)
     */
    private int type;

    /**
     * Initialize button with STANDARD dimensions to save time; dimensions subject to change
     * @param position Position to render the button
     * @param type Button type
     * @param menu Leave as null if this button will not be associated with a menu
     * @param screen Leave as null if this button will not be associated with a screen
     */
    public Button(Vector2f position, int type, Menu menu, Screen screen) {

        super(position);
        this.type = type;
        this.menu = menu;
        this.screen = screen;
        sprite = new Sprite(ArtManager.terrainSpriteSheet, 4*32, 1*32, 128, 64);
        boundingBox = new Rectangle((int) position.x, (int) position.y, 128, 64);

    }

    /**
     * Initialize button with CUSTOM dimensions
     * @param position Position to render the button
     * @param type Button type
     * @param menu Leave as null if this button will not be associated with a menu
     * @param screen Leave as null if this button will not be associated with a screen
     * @param xPos
     * @param yPos
     * @param width1
     * @param width2
     * @param height1
     * @param height2
     */
    public Button(Vector2f position, int type, Menu menu, Screen screen,
                  int xPos, int yPos, int width1, int width2, int height1, int height2) {

        super(position);
        this.type = type;
        this.menu = menu;
        this.screen = screen;
        sprite = new Sprite(ArtManager.terrainSpriteSheet, xPos, yPos, width1, height1);
        boundingBox = new Rectangle((int) position.x, (int) position.y, width2, height2);

    }

    /**
     * Render the button onto a screen
     * @param screen The screen the button will be rendered on
     */
    public void render(Screen screen){
        screen.render(sprite, position);
    }

    /**
     * Button reacts to mouse clicking on it
     * @param input Mouse click input
     */
    public void update(InputHandler input) {
        int mouseX = input.mouse.x;
        int mouseY = input.mouse.y;
        if (input.mouse.leftButton) {

            if (type == -1 && new Rectangle(mouseX, mouseY, 1, 1).intersects(new Rectangle((int) position.x, (int) position.y, 32, 32))) {
                //TODO, button closes screen
            }

            if (type == 0 && new Rectangle(mouseX, mouseY, 1, 1).intersects(new Rectangle((int) position.x, (int) position.y, 32, 32))) {
                sprite.changeColor(0xFF000000, 0xFF661111);     //assuming ARGB orientation, should produce a dim red
            }

            if (type == 1 && new Rectangle(mouseX, mouseY, 1, 1).intersects(new Rectangle((int) position.x, (int) position.y, 32, 32))) {
                //TODO, button opens menu
            }

            if (type == 2 && new Rectangle(mouseX, mouseY, 1, 1).intersects(new Rectangle((int) position.x, (int) position.y, 32, 32))) {
                //TODO, button opens pop-up screen in front of already used screen
            }
        }
    }
}
