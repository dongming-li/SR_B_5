package edu.iastate.a309.darkplatform.camera;

import edu.iastate.a309.darkplatform.entity.Entity;
import edu.iastate.a309.darkplatform.input.InputHandler;
import edu.iastate.a309.darkplatform.utility.Vector2f;

public class Camera extends Entity {

    public Vector2f offset;

    private Vector2f actualOffset;

    public Camera(Vector2f position) {
        super(position);
        super.type = 4;
        offset = this.position;
        actualOffset = new Vector2f();
    }

    public void move(Vector2f delta) {
        actualOffset.add(delta);
        offset.x = (float) ((int) actualOffset.x);
        offset.y = (float) ((int) actualOffset.y);
    }

    public Vector2f getActualOffset() {
        return new Vector2f(actualOffset.x, actualOffset.y);
    }

    public void update(InputHandler input) {
        if (input.keyboard.isKeyPressed(input.keyboard.keyShift)) {
            if (input.keyboard.isKeyPressed(input.keyboard.keyW)) {
                move(new Vector2f(0, -5));
            }
            if (input.keyboard.isKeyPressed(input.keyboard.keyA)) {
                move(new Vector2f(-5, 0));
            }
            if (input.keyboard.isKeyPressed(input.keyboard.keyS)) {
                move(new Vector2f(0, 5));
            }
            if (input.keyboard.isKeyPressed(input.keyboard.keyD)) {
                move(new Vector2f(5, 0));
            }
        }
    }
}
