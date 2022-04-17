package edu.iastate.a309.darkplatform.entitycomponent;

import edu.iastate.a309.darkplatform.display.Screen;
import edu.iastate.a309.darkplatform.entity.*;
import edu.iastate.a309.darkplatform.global.GlobalFields;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class CollisionComponent implements Component {

    public enum COLLISION_TYPE {
        standard,
        death,
        goal
    }

    public COLLISION_TYPE typeOfCollision;

    private Rectangle boundingBox;
    private Rectangle bottomChecker;

    public boolean somethingBelow;

    public boolean moved;
    public boolean canMoveUp;
    public boolean canMoveDown;
    public boolean canMoveLeft;
    public boolean canMoveRight;
    public boolean canMove;

    public boolean foundThisUpdate;

    private List<Entity> entBelow;

    public CollisionComponent(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
        moved = false;
        canMoveUp = true;
        canMoveDown = true;
        canMoveLeft = true;
        canMoveRight = true;
        canMove = true;
        entBelow = new ArrayList<Entity>();
        somethingBelow = false;
        bottomChecker = new Rectangle(-1, -1, 1, 1);

        foundThisUpdate = false;
    }

    public void update(Object o) {
        typeOfCollision = null;
        canMove = true;
        canMoveUp = true;
        canMoveDown = true;
        canMoveLeft = true;
        canMoveRight = true;
        foundThisUpdate = false;
        entBelow.clear();
        if (somethingBelow) {
            bottomChecker.setLocation(boundingBox.x, bottomChecker.y);
        }
        if (moved) {
            List<Entity> ents = GlobalFields.map.getEnts();
            boolean stillBelow = false;
            for (Entity e : ents) {
                if (e.equals(o)) continue;
                if (e.hasComponent("CollisionComponent")) {
                    CollisionComponent collision = (CollisionComponent) e.getComponent("CollisionComponent");
                    if (collision.boundingBox.intersects(boundingBox)) {
                        if (collision.boundingBox.y > boundingBox.y && boundingBox.height > collision.boundingBox.y - boundingBox.y) {
                            canMoveDown = false;
                            entBelow.add(e);
                            somethingBelow = true;
                            foundThisUpdate = true;
                            bottomChecker = new Rectangle(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
                        } else if (collision.boundingBox.y < boundingBox.y && boundingBox.y > collision.boundingBox.y - boundingBox.y) {
                            canMoveUp = false;
                        }
                        if (collision.boundingBox.x < boundingBox.x && boundingBox.x > collision.boundingBox.x - boundingBox.x) {
                            if (!entBelow.contains(e)) {
                                canMoveLeft = false;
                            } else {
                                if (boundingBox.y + boundingBox.height - 1 > collision.boundingBox.y) {
                                    canMoveLeft = false;
                                }
                            }
                        } else if (collision.boundingBox.x > boundingBox.x && boundingBox.width > collision.boundingBox.x - boundingBox.x) {
                            if (!entBelow.contains(e)) {
                                canMoveRight = false;
                                canMoveDown = true;
                                canMoveUp = true;
                            } else {
                                if (boundingBox.y + boundingBox.height > collision.boundingBox.y) {
                                    canMoveRight = false;
                                }
                            }
                        }
                        canMove = false;
                        if (e instanceof Platform) {
                            typeOfCollision = COLLISION_TYPE.standard;
                        } else if (e instanceof BlackHole) {
                            typeOfCollision = COLLISION_TYPE.death;
                        } else if (e instanceof RollingSawblade) {
                            typeOfCollision = COLLISION_TYPE.death;
                        } else if (e instanceof Spike) {
                            Spike spike = (Spike) e;
                            if (spike.hitWall) {
                                typeOfCollision = COLLISION_TYPE.standard;
                            } else {
                                typeOfCollision = COLLISION_TYPE.death;
                            }
                        }
                    }

                    if (somethingBelow && bottomChecker.intersects(collision.boundingBox)) {
                        stillBelow = true;
                    }
                }
            }
            moved = false;
            somethingBelow = stillBelow;
        }
    }

    public void render(Screen screen, Vector2f position) {

    }

    public void updateBoundingBox(Vector2f position) {
        boundingBox.setLocation((int) position.x, (int) position.y);
    }

    public String getName() {
        return "CollisionComponent";
    }

    public COMPONENT_TYPE getComponentType() {
        return COMPONENT_TYPE.collision_component;
    }
}
