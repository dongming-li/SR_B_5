package edu.iastate.a309.darkplatform.input;

import edu.iastate.a309.darkplatform.display.Frame;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Joshua Kuennen
 * A class that checks for and keeps track of key presses.
 */
public class Keyboard implements KeyListener {

    /**
     * A list of keys are that currently pressed
     */
    public List<Integer> currentPressedKeys;

    /**
     * Used later so when you don't check if a key is pressed when the list is being updated.
     */
    private boolean checking;

    /**
     * The InputHandler that this keyboard belongs too.
     */
    private InputHandler father;

    /**
     * A few default keys you can check for being pressed.
     */
    public int keyW = KeyEvent.VK_W;
    public int keyA = KeyEvent.VK_A;
    public int keyS = KeyEvent.VK_S;
    public int keyD = KeyEvent.VK_D;
    public int keyM = KeyEvent.VK_M;
    public int keyShift = KeyEvent.VK_SHIFT;
    public int keyEsc = KeyEvent.VK_ESCAPE;
    public int keySpace = KeyEvent.VK_SPACE;

    /**
     * Constructor for a keyboard.
     *
     * @param frame the component to add this to as a keyboard listener
     * @param dad   the InputHandler that this keyboard belongs too
     */
    public Keyboard(Frame frame, InputHandler dad) {
        frame.addKeyListener(this);
        checking = false;
        currentPressedKeys = new ArrayList<Integer>();
        father = dad;
    }

    /**
     * Gets called every time there is a key pressed.
     * Adds the keycode of the key pressed to currentKeysPressed if its not already in the list.
     *
     * @param event KeyEvent that triggered the method call
     */
    public void keyPressed(KeyEvent event) {
//        while(checking){
//
//        }
        if (!isKeyPressed(event.getKeyCode())) {
            currentPressedKeys.add(event.getKeyCode());
        }
        System.out.println(event.getKeyCode());
    }

    /**
     * Gets called every time there is a key released.
     * Removes the keycode of the key released from currentKeysPressed if its there.
     *
     * @param event KeyEvent that triggered the method call
     */
    public void keyReleased(KeyEvent event) {
//        while(checking){
//
//        }
        if (isKeyPressed(event.getKeyCode())) {
            currentPressedKeys.remove(new Integer(event.getKeyCode()));
        }
    }

    /**
     * Dummy method that is only hear because it needs to be, we have no real use for it.
     *
     * @param event KeyEvent that triggered the method call
     */
    public void keyTyped(KeyEvent event) {

    }

    /**
     * Checks to see if a key with a given keycode is pressed.
     *
     * @param keyCode Key code of the key to check if pressed
     * @return true of the key with the key code keyCode is pressed, else false
     */
    public synchronized boolean isKeyPressed(int keyCode) {
        checking = true;
        boolean pressed = false;
        for (int code : currentPressedKeys) {
            if (code == keyCode) pressed = true;
        }
        checking = false;
        return pressed;
    }
}
