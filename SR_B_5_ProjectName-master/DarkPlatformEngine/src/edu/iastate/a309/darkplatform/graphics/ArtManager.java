package edu.iastate.a309.darkplatform.graphics;

public class ArtManager {

    public static SpriteSheet terrainSpriteSheet;
    public static SpriteSheet mapBackground;

    public static void init() {
        terrainSpriteSheet = new SpriteSheet("/TempSpriteSheet.png");
        mapBackground = new SpriteSheet("/MapBG.png");
    }
}
