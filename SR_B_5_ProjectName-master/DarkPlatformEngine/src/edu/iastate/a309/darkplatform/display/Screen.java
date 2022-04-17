package edu.iastate.a309.darkplatform.display;

import edu.iastate.a309.darkplatform.global.GlobalMethods;
import edu.iastate.a309.darkplatform.graphics.Sprite;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * @author Joshua Kuennen
 * A class in which all the sprites will be drawn too.
 */
public class Screen {

    /**
     * The width and height of the drawable part of the screen (entire screen for fullscreen and the inside part of the frame otherwise)
     */
    private int width, height;

    /**
     * Integer representation of the pixel colors on screen.
     * <p>
     * Size = width * height since its a single array.
     * Runs from left to right and top to bottom.
     * <p>
     * Example:
     * [[0][1][2]]
     * [[3][4][5]]
     * [[6][7][8]]
     * for on screen pixels of screen size 3x3 is equivalent to
     * [0][1][2][3][4][5][6][7][8] in the array.
     */
    private int[] pixelsOnScreen;

    /**
     * The image that we will draw to the frame. The pixel data of the image is stored in pixelsToDraw.
     */
    public BufferedImage imageToDraw;

    /**
     * Constructor of the Screen. Sets up the width and height and links pixelsToDraw and imageToDraw.
     *
     * @param width  desired width of the screen
     * @param height desired height of the screen
     */
    public Screen(int width, int height) {
        this.width = width;
        this.height = height;

        imageToDraw = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        pixelsOnScreen = ((DataBufferInt) imageToDraw.getRaster().getDataBuffer()).getData();
    }

    /**
     * Clears the screen to a dark blue color. Used mainly for alpha blending and resetting each frames pixel data.
     */
    public void clear() {
        for (int i = 0; i < width * height; i++) {
            pixelsOnScreen[i] = 0xFF123456;
        }
    }

    /**
     * Simpler render method. Calls the main one with scales of 1.
     *
     * @param sprite   sprite to render
     * @param position position of the sprite in game
     */
    public void render(Sprite sprite, Vector2f position) {
        render(sprite, position, 1, 1);
    }

    /**
     * @param sprite   sprite to render
     * @param position position of the sprite in game
     * @param xScale   x scale of the sprite
     * @param yScale   y scale of the sprite
     */
    public void render(Sprite sprite, Vector2f position, float xScale, float yScale) {
        position.subtract(Frame.camera.offset);

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

        //If the scale of the sprite isn't 1, then we need to keep track of where we are on the sprite.
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
                if (pixelsOnScreen[y * this.width + x] != 0xFF123456) {
                    int spriteColor = sprite.pixels[(yDifference + (int) spriteIndexY) * sprite.width + (xDifference + (int) spriteIndexX)];
                    int backgroundColor = pixelsOnScreen[y * this.width + x];
                    pixelsOnScreen[y * this.width + x] = GlobalMethods.alphaBlend(spriteColor, backgroundColor);
                } else {
                    pixelsOnScreen[y * this.width + x] = sprite.pixels[(yDifference + (int) spriteIndexY) * sprite.width + (xDifference + (int) spriteIndexX)];
                }
                spriteIndexX += spriteDeltaX;
            }
            spriteIndexX = 0;
            spriteIndexY += spriteDeltaY;
        }
        position.add(Frame.camera.offset);
    }

    /**
     * Alpha blends two colors together.
     *
     * @param oolor1 the source color
     * @param color2 the color being added
     * @return the value of the two colors blended
     */
    public int alphaBlend(int oolor1, int color2) {
        int alpha = (oolor1 & 0xff000000) >>> 24;
        if(alpha == 0)
            return color2;

        int red1 = (oolor1 & 0x00ff0000) >> 16;
        int red2 = (color2 & 0x00ff0000) >> 16;

        int green1 = (oolor1 & 0x0000ff00) >> 8;
        int green2 = (color2 & 0x0000ff00) >> 8;

        int blue1 = (oolor1 & 0x000000ff);
        int blue2 = (color2 & 0x000000ff);

        float src_alpha = ((float) alpha) / 255.0f;

        int red   = (int) ((red1 * src_alpha) + (red2 * (1.0f - src_alpha)));
        int green = (int) ((green1 * src_alpha) + (green2 * (1.0f - src_alpha)));
        int blue  = (int) ((blue1 * src_alpha) + (blue2 * (1.0f - src_alpha)));

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}

