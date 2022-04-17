package edu.iastate.a309.darkplatform.graphics;

public class Sprite {
    public int[] pixels;
    public int xPos, yPos, width, height;
    private SpriteSheet spriteSheet;

    public Sprite(SpriteSheet spriteSheet, int xPos, int yPos, int width, int height) {
        this.spriteSheet = spriteSheet;
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        pixels = this.spriteSheet.getSprite(xPos, yPos, width, height);
    }

    public Sprite(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }

    public void changeColor(int color1, int color2) {
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == color1) pixels[i] = color2;
        }
    }

    public void setPixel(int pixel, int color) {
        if (pixel >= pixels.length || pixel < 0) {
            return;
        }
        pixels[pixel] = color;
    }

    public boolean setPixels(int[] pixels) {
        if (pixels.length != this.pixels.length) {
            return false;
        } else {
            this.pixels = pixels;
            return true;
        }
    }
}
