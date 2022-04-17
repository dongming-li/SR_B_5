package edu.iastate.a309.darkplatform.entity;

import edu.iastate.a309.darkplatform.display.Screen;
import edu.iastate.a309.darkplatform.entitycomponent.CollisionComponent;
import edu.iastate.a309.darkplatform.global.GlobalFields;
import edu.iastate.a309.darkplatform.graphics.ArtManager;
import edu.iastate.a309.darkplatform.graphics.Sprite;
import edu.iastate.a309.darkplatform.input.InputHandler;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.awt.*;

public class Spike extends Entity {

    public int direction;
    private Vector2f velocity;

    public Boolean hitWall;

    private Vector2f movement;

    private static int flashSpeed = 5;
    private static int totalFlashes = 6;

    private int updateOnHitWall;
    private int flashes;
    private boolean flashing;

    public Spike(Vector2f position, int direction, Boolean hitWall) {
        this(position, direction);
        this.hitWall = hitWall;
    }

    public Spike(Vector2f position, int direction) {
        super(position);
        super.type = 5;
        this.direction = direction;
        if (direction == 0) {
            sprite = new Sprite(ArtManager.terrainSpriteSheet, 8 * 32, 0 * 32 + 5, 16, 5);
            addComponent(new CollisionComponent(new Rectangle((int) position.x, (int) position.y, 16, 5)));
            velocity = new Vector2f(2, 0);
        } else if (direction == 1) {
            sprite = new Sprite(ArtManager.terrainSpriteSheet, 8 * 32 + 5, 0 * 32 + 10, 5, 16);
            addComponent(new CollisionComponent(new Rectangle((int) position.x, (int) position.y, 5, 16)));
            velocity = new Vector2f(0, 2);
        } else if (direction == 2) {
            sprite = new Sprite(ArtManager.terrainSpriteSheet, 8 * 32, 0 * 32, 16, 5);
            addComponent(new CollisionComponent(new Rectangle((int) position.x, (int) position.y, 16, 5)));
            velocity = new Vector2f(-2, 0);
        } else {
            sprite = new Sprite(ArtManager.terrainSpriteSheet, 8 * 32, 0 * 32 + 10, 5, 16);
            addComponent(new CollisionComponent(new Rectangle((int) position.x, (int) position.y, 5, 16)));
            velocity = new Vector2f(0, -2);
        }

        hitWall = false;
        movement = new Vector2f();
        updateOnHitWall = 0;
        flashes = 0;
        flashing = false;
    }

    public Spike(Vector2f position) {
        super(position);
        super.type = 5;
        direction = 0;
        sprite = new Sprite(ArtManager.terrainSpriteSheet, 8 * 32, 0 * 32 + 5, 16, 5);
        addComponent(new CollisionComponent(new Rectangle((int) position.x, (int) position.y, 16, 5)));
        velocity = new Vector2f(2, 0);
        hitWall = false;
        movement = new Vector2f();
        updateOnHitWall = 0;
        flashes = 0;
        flashing = false;
    }

    public void update(InputHandler input){
        if(!GlobalFields.EDIT_MODE){

            if(!hitWall){
                movement.clone(position);
                CollisionComponent collision = ((CollisionComponent)getComponent("CollisionComponent"));

                movement.add(velocity);

                collision.moved = true;

                collision.updateBoundingBox(movement);

                super.update(input);

                if(collision.canMove){
                    position.clone(movement);
                } else {
                    hitWall = true;
                    updateOnHitWall = updates;
                }
            } else {
                if(updates - updateOnHitWall > 30){
                    if(flashes == totalFlashes){
                        GlobalFields.map.removeEnt(this);
                    }
                    if((updates - updateOnHitWall) % flashSpeed == 0){
                        flashing = !flashing;
                        if(!flashing){
                            flashes++;
                        }
                    }
                }

                updates++;
            }
        }
    }

    public void render(Screen screen) {
        if (!flashing) {
            screen.render(sprite, position);
        }
        if(GlobalFields.EDIT_MODE){
            if(direction == 0){
                screen.render(GlobalFields.rightArrow, new Vector2f(position.x + 32, position.y - 3));
            } else if(direction == 1){
                screen.render(GlobalFields.downArrow, new Vector2f(position.x - 3, position.y + 32));
            } else if(direction == 2){
                screen.render(GlobalFields.leftArrow, new Vector2f(position.x - 16, position.y - 3));
            } else {
                screen.render(GlobalFields.upArrow, new Vector2f(position.x - 3, position.y - 16));
            }
        }
    }

    public Entity clone(){
        Spike retVal = new Spike(new Vector2f(this.position.x, this.position.y), this.direction);
        retVal.cloned = true;
        return retVal;
    }
}
