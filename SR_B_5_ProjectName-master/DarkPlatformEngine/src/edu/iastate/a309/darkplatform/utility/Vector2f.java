package edu.iastate.a309.darkplatform.utility;

import edu.iastate.a309.darkplatform.display.Frame;

/**
 * Helper class that is a representation of 2 float values. Generally used for positions.
 */
public class Vector2f {

    /**
     * The 2 float values that this class represents.
     */
    public float x, y;

    /**
     * Default constructor, sets both values to 0.
     */
    public Vector2f() {
        x = 0;
        y = 0;
    }

    /**
     * Constructor that provides the two floats to be represented.
     *
     * @param x the x value to be represented
     * @param y the y value to be represented
     */
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructor that provides the values to be represented through another Vector2f. Creates a copy of vector.
     *
     * @param vector the Vector2f to make a copy of.
     */
    public Vector2f(Vector2f vector) {
        x = vector.x;
        y = vector.y;
    }

    /**
     * Add two Vector2f's together and store it into this one.
     *
     * @param vector vector to be added to this
     */
    public void add(Vector2f vector) {
        x += vector.x;
        y += vector.y;
    }

    /**
     * Add a scalar value to both x and y.
     *
     * @param scalar value to add to this
     */
    public void add(float scalar) {
        x += scalar;
        y += scalar;
    }

    /**
     * Subtract a Vector2f from this one, stores back into this.
     *
     * @param vector vector to subtract from this
     */
    public void subtract(Vector2f vector) {
        x -= vector.x;
        y -= vector.y;
    }

    /**
     * Subtract a scalar value from both x and y.
     *
     * @param scalar value to subtract
     */
    public void subtract(float scalar) {
        x -= scalar;
        y -= scalar;
    }

    /**
     * Scale this up by a Vector2f value. Multiplies both x's and y's.
     *
     * @param vector vector to multiply with this
     */
    public void scale(Vector2f vector) {
        x *= vector.x;
        y *= vector.y;
    }

    /**
     * Scale this up by a scalar value. Multiplies x and y by scalar.
     *
     * @param scalar value to multiply with this
     */
    public void scale(float scalar) {
        x *= scalar;
        y *= scalar;
    }

    public void clone(Vector2f cloner) {
        x = cloner.x;
        y = cloner.y;
    }

    public boolean onScreen() {
        if (x > (0 + Frame.camera.offset.x) && x < (Frame.width + Frame.camera.offset.x) && y > (0 + Frame.camera.offset.y) && y < (Frame.height + Frame.camera.offset.y)) {
            return true;
        } else {
            return false;
        }
    }

    public void reset() {
        x = 0;
        y = 0;
    }
}
