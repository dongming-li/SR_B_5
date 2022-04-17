package edu.iastate.a309.darkplatform.display;

import edu.iastate.a309.darkplatform.graphics.Sprite;

public class WindowSprite {
    public int x, y;
    public Sprite sprite;

    public WindowSprite(Sprite sprite, int x, int y){
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }
}
