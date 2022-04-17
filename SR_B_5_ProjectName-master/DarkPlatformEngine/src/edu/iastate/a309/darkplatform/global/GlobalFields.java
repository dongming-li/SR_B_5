package edu.iastate.a309.darkplatform.global;

import edu.iastate.a309.darkplatform.graphics.ArtManager;
import edu.iastate.a309.darkplatform.graphics.Sprite;
import edu.iastate.a309.darkplatform.utility.Vector2f;

public class GlobalFields {

    public static edu.iastate.a309.darkplatform.map.Map map;
    //    public static final String ip = "http://proj-309-sr-b-5.cs.iastate.edu:8080/";
    public static final String ip = "http://localhost:8080/";
    public static final Vector2f MAX_VELOCITY = new Vector2f(10f, 10f);

    public static boolean EDIT_MODE = false;

    public static Sprite upArrow = new Sprite(ArtManager.terrainSpriteSheet, 9*32, 0*32, 16, 16);
    public static Sprite rightArrow = new Sprite(ArtManager.terrainSpriteSheet, 9*32 + 16, 0*32, 16, 16);
    public static Sprite downArrow = new Sprite(ArtManager.terrainSpriteSheet, 9*32 + 16, 0*32 + 16, 16, 16);
    public static Sprite leftArrow = new Sprite(ArtManager.terrainSpriteSheet, 9*32, 0*32 + 16
            , 16, 16);

}
