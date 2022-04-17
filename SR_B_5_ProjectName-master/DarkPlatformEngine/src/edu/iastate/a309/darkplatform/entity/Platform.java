package edu.iastate.a309.darkplatform.entity;

import edu.iastate.a309.darkplatform.entitycomponent.CollisionComponent;
import edu.iastate.a309.darkplatform.graphics.ArtManager;
import edu.iastate.a309.darkplatform.graphics.Sprite;
import edu.iastate.a309.darkplatform.serialization.DPField;
import edu.iastate.a309.darkplatform.serialization.DPObject;
import edu.iastate.a309.darkplatform.serialization.SerializationWriter;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.awt.*;

public class Platform extends Entity {

    public int direction;

    public Platform(Vector2f position, int direction) {
        super(position);
        super.type = 1;
        this.direction = direction;
        if (direction == 0) {
            sprite = new Sprite(ArtManager.terrainSpriteSheet, 4 * 32, 0 * 32, 32, 32);
            addComponent(new CollisionComponent(new Rectangle((int) position.x, (int) position.y, 32, 6)));
        } else if (direction == 1) {
            sprite = new Sprite(ArtManager.terrainSpriteSheet, 5 * 32, 0 * 32, 32, 32);
            addComponent(new CollisionComponent(new Rectangle((int) position.x + 26, (int) position.y, 6, 32)));
        } else if (direction == 2) {
            sprite = new Sprite(ArtManager.terrainSpriteSheet, 6 * 32, 0 * 32, 32, 32);
            addComponent(new CollisionComponent(new Rectangle((int) position.x, (int) position.y + 26, 32, 6)));
        } else {
            sprite = new Sprite(ArtManager.terrainSpriteSheet, 7 * 32, 0 * 32, 32, 32);
            addComponent(new CollisionComponent(new Rectangle((int) position.x, (int) position.y, 6, 32)));
        }
    }

    public Platform(Vector2f position) {
        super(position);
        super.type = 1;
        direction = 0;
        sprite = new Sprite(ArtManager.terrainSpriteSheet, 4 * 32, 0 * 32, 32, 32);
        addComponent(new CollisionComponent(new Rectangle((int) position.x, (int) position.y, 32, 6)));
    }

    @Override
    public DPObject serialize() {
        DPObject object = super.serialize();
        DPField dir = DPField.createInt("Direction", direction);
        object.addField(dir);
        object.setName("Platform");
        return object;
    }

    public static Platform deserialize(DPObject object) {
        float xPos = 0.0f, yPos = 0.0f;
        int id = -1;
        int direction = 0;
        for (DPField field : object.fields) {
            if (field.getName().matches("XPos")) {
                xPos = SerializationWriter.readFloat(field.data, 0);
            } else if (field.getName().matches("YPos")) {
                yPos = SerializationWriter.readFloat(field.data, 0);
            } else if (field.getName().matches("ID")) {
                id = SerializationWriter.readInt(field.data, 0);
            } else if (field.getName().matches("Direction")) {
                direction = SerializationWriter.readInt(field.data, 0);
            }
        }
        Platform platform = new Platform(new Vector2f(xPos, yPos), direction);
        platform.id = id;
        return platform;
    }

    public Entity clone(){
        Platform retVal = new Platform(new Vector2f(this.position.x, this.position.y));
        retVal.direction = this.direction;
        return retVal;
    }
}
