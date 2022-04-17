package edu.iastate.a309.darkplatform.graphics;

import edu.iastate.a309.darkplatform.display.Screen;
import edu.iastate.a309.darkplatform.utility.Vector2f;

public class Animation {

    public Sprite[] sprites;
    private int frame;
    private int frameLength;
    private long frameTimer;
    private String name;

    public boolean finishedOneCycle;

    public Animation(Sprite[] sprites, String name) {
        this.sprites = sprites;
        this.name = name;

        frameLength = 500;
        frameTimer = System.currentTimeMillis();
        frame = 0;

        finishedOneCycle = false;
    }

    public String getName() {
        return name;
    }

    public void update() {
        if (System.currentTimeMillis() - frameTimer >= frameLength) {
            frame++;
            if (frame >= sprites.length) {
                frame = 0;
                finishedOneCycle = true;

            }
            frameTimer += frameLength;
            if(System.currentTimeMillis() - frameTimer >= frameLength * 3){
                frameTimer = System.currentTimeMillis();
            }
        }
    }

    public void render(Screen screen, Vector2f position) {
        screen.render(sprites[frame], position);
    }

    public void reset() {
        frame = 0;
        finishedOneCycle = false;
    }

    public void resetTimer() {
        frameTimer = System.currentTimeMillis();
    }

    public static Animation createAnimation(String ani, String name, int frameLength) {
        String[] frames = ani.split(">");
        Sprite[] sprites = new Sprite[frames.length];
        int i = 0;
        for (String frame : frames) {
            String[] coords = frame.split(",");
            int xCoord = Integer.parseInt(coords[0]);
            int yCoord = Integer.parseInt(coords[1]);
            Sprite sprite = new Sprite(ArtManager.terrainSpriteSheet, 32 * xCoord, 32 * yCoord, 32, 32);
            sprites[i++] = sprite;
        }
        Animation anim = new Animation(sprites, name);
        anim.frameLength = frameLength;
        return anim;
    }

    public boolean equals(Object o) {
        if (o instanceof Animation) {
            if (this.name.matches(((Animation) o).name)) {
                return true;
            }
        }
        return false;
    }

    public Sprite getCurrentFrame() {
        return sprites[frame];
    }
}