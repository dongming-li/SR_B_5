package edu.iastate.a309.darkplatform.input;

import edu.iastate.a309.darkplatform.display.Frame;

public class InputHandler {

    public Keyboard keyboard;
    public Mouse mouse;

    public InputHandler(Frame frame) {
        keyboard = new Keyboard(frame, this);
        mouse = new Mouse(frame);
    }
}
