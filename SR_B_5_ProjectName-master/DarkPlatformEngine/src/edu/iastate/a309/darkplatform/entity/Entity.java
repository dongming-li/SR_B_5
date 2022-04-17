package edu.iastate.a309.darkplatform.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import edu.iastate.a309.darkplatform.display.Frame;
import edu.iastate.a309.darkplatform.display.Screen;
import edu.iastate.a309.darkplatform.entitycomponent.Component;
import edu.iastate.a309.darkplatform.global.GlobalFields;
import edu.iastate.a309.darkplatform.graphics.ArtManager;
import edu.iastate.a309.darkplatform.graphics.Sprite;
import edu.iastate.a309.darkplatform.input.InputHandler;
import edu.iastate.a309.darkplatform.serialization.DPField;
import edu.iastate.a309.darkplatform.serialization.DPObject;
import edu.iastate.a309.darkplatform.serialization.SerializationWriter;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Entity {

    protected int type;

    protected Vector2f position;

    @JsonIgnore
    protected List<Component> components;

    @JsonIgnore
    protected Sprite sprite;

    @JsonIgnore
    protected Rectangle boundingBox;


    protected int id;

    @JsonIgnore
    protected static int idIndex = 0;

    @JsonIgnore
    protected int updates;

    protected boolean cloned;

    public Entity(Vector2f position){
        this.position = position;
        updates = 0;
        sprite = new Sprite(ArtManager.terrainSpriteSheet, 32 * 0, 32 * 0, 32, 32);
        components = new ArrayList<Component>();
        id = idIndex++;
        boundingBox = new Rectangle((int) position.x + 8, (int)position.y,16,32);
        cloned = false;
    }

    public void render(Screen screen) {
        for (Component c : components) {
            c.render(screen, position);
        }
        if (!hasComponent("AnimationComponent")) {
            screen.render(sprite, position);
        }
    }

    public void update(InputHandler input) {
        for (Component c : components) {
            c.update(this);

       }

       if(this.position.y > Frame.height && !(this instanceof Player)){
           GlobalFields.map.removeEnt(this);
       }
//       if(new Rectangle((int)(input.mouse.x + Frame.camera.offset.x), (int)(input.mouse.y + Frame.camera.offset.y), 1,1).intersects(boundingBox) || moving){
//            if(input.mouse.leftButton && !moving) {
//                moving = true;
//            }
//            if(moving && input.mouse.leftButton){
//                position = new Vector2f(input.mouse.x + Frame.camera.offset.x, input.mouse.y + Frame.camera.offset.y);
//                boundingBox = new Rectangle((int) position.x + 8, (int)position.y,16,32);
//            } else if (moving) {
//                moving = false;
//            }
//
//       }
        updates++;
    }

    public Vector2f getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public void addComponent(Component comp) {
        components.add(comp);
    }

    public boolean hasComponent(String name) {
        boolean hasComp = false;
        for (Component c : components) {
            if (c.getName().matches(name)) hasComp = true;
        }
        return hasComp;
    }

    public Component getComponent(String name) {
        if (hasComponent(name)) {
            for (Component c : components) {
                if (c.getName().matches(name)) return c;
            }
        }
        return null;
    }

    protected void setPosition(Vector2f newPos) {
        position = newPos;
    }

    public DPObject serialize() {
        DPField xPos = DPField.createFloat("XPos", position.x);
        DPField yPos = DPField.createFloat("YPos", position.y);

        DPField id = DPField.createInt("ID", this.id);
        DPObject object = new DPObject("Entity");
        object.addField(xPos);
        object.addField(yPos);
        object.addField(id);

        return object;
    }

    public static Entity deserialize(DPObject object) {
        float xPos = 0.0f, yPos = 0.0f;
        int id = -1;
        for (DPField field : object.fields) {
            if (field.getName().matches("XPos")) {
                xPos = SerializationWriter.readFloat(field.data, 0);
            } else if (field.getName().matches("YPos")) {
                yPos = SerializationWriter.readFloat(field.data, 0);
            } else if (field.getName().matches("ID")) {
                id = SerializationWriter.readInt(field.data, 0);
            }
        }

        Entity ent = new Entity(new Vector2f(xPos, yPos));
        ent.id = id;
        return ent;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }    
    public boolean isCloned(){
        return cloned;
    }

    public Entity clone(){
        Entity ent = new Entity(new Vector2f(this.position.x, this.position.y));
        ent.cloned = true;
        return ent;
    }

}
