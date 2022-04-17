package edu.iastate.a309.darkplatform.entity;

import edu.iastate.a309.darkplatform.display.Screen;
import edu.iastate.a309.darkplatform.entitycomponent.AnimationComponent;
import edu.iastate.a309.darkplatform.entitycomponent.CollisionComponent;
import edu.iastate.a309.darkplatform.global.GlobalFields;
import edu.iastate.a309.darkplatform.graphics.Animation;
import edu.iastate.a309.darkplatform.input.InputHandler;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RollingSawblade extends Entity {

    private Vector2f movement;

    private static Vector2f boundingBoxOffset = new Vector2f(4, 6);

    private Vector2f velocity;

    private Vector2f horizontalVelocity;

    public boolean forward;


    public RollingSawblade(Vector2f position, Boolean forward) {
        this(position);
        this.forward = forward;
    }

    public RollingSawblade(Vector2f position) {
        super(position);
        super.type = 2;
        List<Animation> anis = new ArrayList<Animation>();
        anis.add(Animation.createAnimation("2,1>4,1>3,1", "Right", 40));
        anis.add(Animation.createAnimation("2,1>3,1>4,1", "Left", 40));

        addComponent(new AnimationComponent(anis));
        addComponent(new CollisionComponent(new Rectangle((int) position.x + 4, (int) position.y + 6, 23, 23)));
        velocity = new Vector2f();
        movement = new Vector2f();
        horizontalVelocity = new Vector2f(2, 0);
        forward = true;
    }

    public RollingSawblade(Vector2f position, boolean forward) {
        super(position);
        super.type = 2;
        List<Animation> anis = new ArrayList<Animation>();
        anis.add(Animation.createAnimation("2,1>4,1>3,1", "Right", 40));
        anis.add(Animation.createAnimation("2,1>3,1>4,1", "Left", 40));

        addComponent(new AnimationComponent(anis));
        addComponent(new CollisionComponent(new Rectangle((int) position.x + 4, (int) position.y + 6, 23, 23)));
        velocity = new Vector2f();
        movement = new Vector2f();
        horizontalVelocity = new Vector2f(2, 0);
        this.forward = forward;
    }

    public void render(Screen screen){
        super.render(screen);
        if(GlobalFields.EDIT_MODE){
            if(forward){
                screen.render(GlobalFields.rightArrow, new Vector2f(position.x + 32, position.y + 12));
            } else {
                screen.render(GlobalFields.leftArrow, new Vector2f(position.x - 16, position.y + 12));
            }
        }
    }

    public void update(InputHandler input){
        if(!GlobalFields.EDIT_MODE){
            movement.clone(position);
            CollisionComponent collision = ((CollisionComponent)getComponent("CollisionComponent"));
            AnimationComponent animation = ((AnimationComponent)getComponent("AnimationComponent"));

            movement.add(velocity);

            collision.moved = true;

            if(forward){
                movement.add(horizontalVelocity);
            } else {
                movement.subtract(horizontalVelocity);
            }

            movement.add(boundingBoxOffset);
            collision.updateBoundingBox(movement);
            movement.subtract(boundingBoxOffset);

            super.update(input);

            if(collision.canMoveDown && collision.canMoveUp){
                position = new Vector2f(position.x, movement.y);
            } else {
                velocity.y = 0;
            }
            if(collision.canMoveLeft && collision.canMoveRight){
                position = new Vector2f(movement.x, position.y);
            } else {
                velocity.x = 0;
            }
            if(!collision.canMove){
                if(!collision.foundThisUpdate){
                    if(!collision.canMoveLeft){
                        forward = true;
                        animation.setAnimation("Right");
                    } else if(!collision.canMoveRight){
                        forward = false;
                        animation.setAnimation("Left");
                    }
                }
                position.x = (float)(int) position.x;
                position.y = (float)(int) position.y;
            } else {
                if(!collision.somethingBelow){
                    velocity.add(GlobalFields.map.gravity.getAcceleration(position));
                    if(velocity.x > GlobalFields.MAX_VELOCITY.x) velocity.x = GlobalFields.MAX_VELOCITY.x;
                    if(velocity.y > GlobalFields.MAX_VELOCITY.y) velocity.y = GlobalFields.MAX_VELOCITY.y;
                }
            }
        }
    }

    public Entity clone(){
        RollingSawblade retVal = new RollingSawblade(new Vector2f(this.position.x, this.position.y));
        retVal.forward = this.forward;
        retVal.cloned = true;
        return retVal;
    }

    public boolean isForward() {
        return forward;
    }
}
