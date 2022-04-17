package edu.iastate.a309.darkplatform.input;

import edu.iastate.a309.darkplatform.display.Frame;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;

public class Mouse implements MouseMotionListener, MouseListener {
    public boolean leftButton = false;
    public boolean rightButton = false;
    public int scrollPosition = 0;
    public int x = 0, y = 0;

    public Mouse(Frame frame) {
        frame.addMouseListener(this);
        frame.addMouseMotionListener(this);

    }

    public void mouseWheelMoved(MouseWheelEvent event) {
        scrollPosition -= event.getWheelRotation();
    }

    public void mouseClicked(MouseEvent event) {

    }

    public void mouseEntered(MouseEvent event) {

    }

    public void mouseExited(MouseEvent event) {

    }

    public void mousePressed(MouseEvent event) {
        if (event.getButton() == 1) {
            leftButton = true;
        }
        if (event.getButton() == 3) {
            rightButton = true;
        }
    }

    public void mouseReleased(MouseEvent event) {
        if (event.getButton() == 1) {
            leftButton = false;
        }
        if (event.getButton() == 3) {
            rightButton = false;
        }
    }

    public void mouseDragged(MouseEvent event) {
        mouseMoved(event);
    }

    public void mouseMoved(MouseEvent event) {
        x = event.getX();
        y = event.getY();

    }
}
