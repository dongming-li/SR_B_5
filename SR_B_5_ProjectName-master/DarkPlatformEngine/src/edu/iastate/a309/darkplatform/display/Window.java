package edu.iastate.a309.darkplatform.display;

import edu.iastate.a309.darkplatform.global.GlobalMethods;
import edu.iastate.a309.darkplatform.graphics.ArtManager;
import edu.iastate.a309.darkplatform.graphics.Sprite;
import edu.iastate.a309.darkplatform.input.InputHandler;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

public class Window {
    private int width, height;

    private Sprite sprite;
    private int[] pixels;

    private Vector2f position;

    private boolean moving;
    private int xOffset, yOffset;

    private List<WindowString> strings;
    private List<WindowSprite> sprites;

    public Window(int width, int height, Vector2f position){
        this.width = width;
        this.height = height;

        sprite = new Sprite(width, height);
        pixels = new int[width * height];

        this.position = position;

        moving = false;
        xOffset = 0;
        yOffset = 0;
        strings = new ArrayList<WindowString>();
        sprites = new ArrayList<WindowSprite>();
    }

    public void update(InputHandler input) {
        Rectangle mouse = new Rectangle(input.mouse.x, input.mouse.y, 1, 1);
        if (!moving) {
            Rectangle moveArea = new Rectangle((int) position.x, (int) position.y, width, 10);

            if (mouse.intersects(moveArea)) {
                if (input.mouse.leftButton && !moving) {
                    moving = true;
                    xOffset = input.mouse.x - (int) position.x;
                    yOffset = input.mouse.y - (int) position.y;
                }
            }
        } else {
            position.x = mouse.x - xOffset;
            position.y = mouse.y - yOffset;
            if (!input.mouse.leftButton) {
                moving = false;
            }
        }

        generateBasicWindow();
    }

    public void render(Screen screen){
        for(WindowSprite s: sprites){
            renderSprite(s.sprite, new Vector2f(s.x, s.y));
        }
        sprite.setPixels(pixels);
        screen.render(sprite, position);

    }

    public void renderWords(Graphics graphics){
        for(WindowString w: strings){
            graphics.drawString(w.string, w.x + (int) position.x - (int) Frame.camera.offset.x, w.y + (int) position.y - (int) Frame.camera.offset.y);
        }
    }

    public void renderSprite(Sprite sprite, Vector2f position){
        renderSprite(sprite, position, 1, 1);
    }

    public void renderSprite(Sprite sprite, Vector2f position, float xScale, float yScale) {
        int width = sprite.width;
        int height = sprite.height;

        //width and height of the area the sprite will take up after the scales are applied to it
        float newWidth = width * xScale;
        float newHeight = height * yScale;

        /*
            The start and ends of areas on the screen that the sprite will be rendered too

            Gets cut off at the screen bounds because its a waste of resources to try and do the pixel math for off screen pixels.
         */
        int xStart = (int) position.x;
        int xEnd = (int) (xStart + newWidth);
        int yStart = (int) position.y;
        int yEnd = (int) (yStart + newHeight);

        if (xStart < 0) xStart = 0;
        if (xEnd > this.width) xEnd = this.width;
        if (yStart < 0) yStart = 0;
        if (yEnd > this.height) yEnd = this.height;

        //Distance from the original position of the sprite and the renderings area. If the top left corner is on screen, they will be 0.
        int xDifference = (xStart - (int) position.x);
        int yDifference = (yStart - (int) position.y);

        //If the scale of the sprite isnt 1, then we need to keep track of where we are on the sprite.
        float spriteDeltaX = width / newWidth;
        float spriteIndexX = 0;

        float spriteDeltaY = height / newHeight;
        float spriteIndexY = 0;

        //for loops to run through every pixel of the rendering area and put a part of the sprite there.
        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                //if the area on the sprite that we are at is completely translucent, move on to the next pixel.
                if (sprite.pixels[(yDifference + (int) spriteIndexY) * sprite.width + (xDifference + (int) spriteIndexX)] >> 24 == 0) {
                    spriteIndexX += spriteDeltaX;
                    continue;
                }


                //TODO: Should check if there is alpha data in the colors?
                //if the pixel on screen is not the color that clear sets the pixel data too, then apply some alpha blending.
                if (pixels[y * this.width + x] != 0xFF123456) {
                    int spriteColor = sprite.pixels[(yDifference + (int) spriteIndexY) * sprite.width + (xDifference + (int) spriteIndexX)];
                    int backgroundColor = pixels[y * this.width + x];
                    pixels[y * this.width + x] = GlobalMethods.alphaBlend(spriteColor, backgroundColor);
                } else {
                    pixels[y * this.width + x] = sprite.pixels[(yDifference + (int) spriteIndexY) * sprite.width + (xDifference + (int) spriteIndexX)];
                }
                spriteIndexX += spriteDeltaX;
            }
            spriteIndexX = 0;
            spriteIndexY += spriteDeltaY;
        }
    }

    private void generateBasicWindow() {
        for (int i = 0; i < pixels.length; i++) {
            int y = i / width;
            int x = i % width;

            if(x <= 3 || y <= 3 || x >= width - 4 || y >= height - 4){
                pixels[i] = 0xffd35400;
            } else {
                pixels[i] = 0xffffffff;
            }
        }
    }

    public void addString(String string, int x, int y){
        strings.add(new WindowString(string, x, y));
    }

    public void addSprite(Sprite sprite, int x, int y){
        sprites.add(new WindowSprite(sprite, x, y));
    }

    public static void generateEditWindowStrings(Window window){
        window.addString("Platform", 10, 30);
        window.addString("RollingSawblade", 105, 30);
        window.addString("Spawner", 10, 80);
        window.addString("BlackHole", 135, 80);
    }

    public static void generateEditWindowSprites(Window window){
        window.addSprite(new Sprite(ArtManager.terrainSpriteSheet, 4*32, 0, 32, 32), 32, 40);
        window.addSprite(new Sprite(ArtManager.terrainSpriteSheet, 2*32, 1*32, 32,32), 165, 30);
        window.addSprite(new Sprite(ArtManager.terrainSpriteSheet, 5*32, 1*32, 32,32), 32, 85);
        window.addSprite(new Sprite(ArtManager.terrainSpriteSheet, 1*32, 0*32, 32,32), 165, 85);
    }
}
