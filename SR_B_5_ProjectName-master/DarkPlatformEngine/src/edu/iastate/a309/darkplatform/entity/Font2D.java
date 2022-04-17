package edu.iastate.a309.darkplatform.entity;

import edu.iastate.a309.darkplatform.display.Frame;
import edu.iastate.a309.darkplatform.display.Screen;
import edu.iastate.a309.darkplatform.global.GlobalMethods;
import edu.iastate.a309.darkplatform.graphics.ArtManager;
import edu.iastate.a309.darkplatform.graphics.Sprite;
import edu.iastate.a309.darkplatform.input.InputHandler;
import edu.iastate.a309.darkplatform.server.entity.Map;
import edu.iastate.a309.darkplatform.utility.Vector2f;
import org.springframework.web.client.RestTemplate;


import javax.swing.*;
import java.awt.*;

/**
 * @author Alexander Lee
 *
 */
public class Font2D extends Entity {

//    public static String chars = "" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ    " + "abcdefghijklmnopqrstuvwxyz    " + "0123456789.,:;'\"!?()+=-_/*$%^&";

    private JTextArea j;

    private Font f;

    private Color c;



    public Font2D(Vector2f position, JTextArea j, Font f, Color c) {
        super(position);
        this.j = j;
        this.f = f;
        this.c = c;
        j.setFont(f);
        j.setForeground(c);
    }

    /**
     * Render the button onto a screen
     * @param screen The screen the button will be rendered on
     */
    public void render(Screen screen){
        screen.render(sprite, position);
    }

}
