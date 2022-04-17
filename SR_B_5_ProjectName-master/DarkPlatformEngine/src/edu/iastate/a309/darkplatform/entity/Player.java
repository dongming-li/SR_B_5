package edu.iastate.a309.darkplatform.entity;

import edu.iastate.a309.darkplatform.entitycomponent.AnimationComponent;
import edu.iastate.a309.darkplatform.entitycomponent.CollisionComponent;
import edu.iastate.a309.darkplatform.global.GlobalFields;
import edu.iastate.a309.darkplatform.graphics.Animation;
import edu.iastate.a309.darkplatform.graphics.ArtManager;
import edu.iastate.a309.darkplatform.graphics.Sprite;
import edu.iastate.a309.darkplatform.input.InputHandler;
import edu.iastate.a309.darkplatform.serialization.DPObject;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {

    private Vector2f movement;
    private static Vector2f boundingBoxOffset = new Vector2f(7, 0);
    public Boolean respawning;

    private Vector2f velocity;

    public Player(Vector2f position, Boolean respawning) {
        this(position);
        this.respawning = respawning;
    }

    public Player(Vector2f position) {
        super(position);
        super.type = 99;
        sprite = new Sprite(ArtManager.terrainSpriteSheet, 1 * 32, 0 * 32, 32, 32);
        addComponent(new CollisionComponent(new Rectangle((int) position.x + 7, (int) position.y, 18, 32)));
        movement = new Vector2f();

        List<Animation> anis = new ArrayList<Animation>();
        anis.add(Animation.createAnimation("0,2>4,2", "Idle", 500));
        anis.add(Animation.createAnimation("0,2>1,2>2,2>3,2", "Death", 250));
        anis.add(Animation.createAnimation("3,2>2,2>1,2>0,2", "Life", 250));
        addComponent(new AnimationComponent(anis));

        respawning = false;

        velocity = new Vector2f();
    }

    @Override
    public void update(InputHandler input) {
        movement.clone(position);
        CollisionComponent collision = ((CollisionComponent) getComponent("CollisionComponent"));
        AnimationComponent animation = ((AnimationComponent) getComponent("AnimationComponent"));

        if (respawning) {
            super.update(input);
            if (animation.getAnimationName().matches("Death")) {
                if (animation.animationCompleted) {
                    position = new Vector2f(GlobalFields.map.spawnPoint);
                    position.add(boundingBoxOffset);
                    collision.updateBoundingBox(position);
                    position.subtract(boundingBoxOffset);
                    animation.setAnimation("Life");
                }
            }
            if (animation.getAnimationName().matches("Life")) {
                if (animation.animationCompleted) {
                    animation.setAnimation("Idle");
                    velocity.reset();
                    respawning = false;
                }
            }
        } else {
            movement.add(velocity);
            movement.add(boundingBoxOffset);
            collision.updateBoundingBox(movement);
            movement.subtract(boundingBoxOffset);
            if (velocity.x != 0 || velocity.y != 0) {
                collision.moved = true;
            }
            if (!input.keyboard.isKeyPressed(input.keyboard.keyShift)) {
                if (input.keyboard.isKeyPressed(input.keyboard.keyW)) {
                    movement.add(new Vector2f(0, -5));
                    collision.moved = true;
                    movement.add(boundingBoxOffset);
                    collision.updateBoundingBox(movement);
                    movement.subtract(boundingBoxOffset);
                }
                if (input.keyboard.isKeyPressed(input.keyboard.keyA)) {
                    movement.add(new Vector2f(-5, 0));
                    collision.moved = true;
                    movement.add(boundingBoxOffset);
                    collision.updateBoundingBox(movement);
                    movement.subtract(boundingBoxOffset);
                }
                if (input.keyboard.isKeyPressed(input.keyboard.keyS)) {
                    movement.add(new Vector2f(0, 5));
                    collision.moved = true;
                    movement.add(boundingBoxOffset);
                    collision.updateBoundingBox(movement);
                    movement.subtract(boundingBoxOffset);
                }
                if (input.keyboard.isKeyPressed(input.keyboard.keyD)) {
                    movement.add(new Vector2f(5, 0));
                    collision.moved = true;
                    movement.add(boundingBoxOffset);
                    collision.updateBoundingBox(movement);
                    movement.subtract(boundingBoxOffset);
                }
                if (input.keyboard.isKeyPressed(input.keyboard.keySpace)) {
                    movement.subtract(velocity);
                    velocity = new Vector2f(0, -5);
                    movement.add(velocity);
                    movement.add(boundingBoxOffset);
                    collision.updateBoundingBox(movement);
                    movement.subtract(boundingBoxOffset);
                }
            }
            super.update(input);
            if (collision.canMoveDown && collision.canMoveUp) {
                position = new Vector2f(position.x, movement.y);
            } else {
                velocity.y = 0;
            }
            if (collision.canMoveLeft && collision.canMoveRight) {
                position = new Vector2f(movement.x, position.y);
            } else {
                velocity.x = 0;
            }
            if (!collision.canMove) {
                if (collision.typeOfCollision == CollisionComponent.COLLISION_TYPE.death) {
                    respawning = true;
                    animation.setAnimation("Death");
                }
                position.x = (float) (int) position.x;
                position.y = (float) (int) position.y;
            }
        }

        velocity.add(GlobalFields.map.gravity.getAcceleration(position));
        if (velocity.x > GlobalFields.MAX_VELOCITY.x) velocity.x = GlobalFields.MAX_VELOCITY.x;
        if (velocity.y > GlobalFields.MAX_VELOCITY.y) velocity.y = GlobalFields.MAX_VELOCITY.y;
    }

    @Override
    public DPObject serialize() {
        DPObject object = super.serialize();
        object.setName("Player");
        return object;
    }
}
