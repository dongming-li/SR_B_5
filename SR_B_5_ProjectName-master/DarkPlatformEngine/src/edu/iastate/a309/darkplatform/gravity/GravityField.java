package edu.iastate.a309.darkplatform.gravity;

import edu.iastate.a309.darkplatform.utility.Vector2f;

public class GravityField {
    private int width = 60;
    private int height = 33;

    //Rather than the Vector2f representing a (x,y) coord, its now a (angle, acceleration) coord
    //The new representation's units are degrees and pixels/s^2, where 0 degrees is down, 90 is right, 180 is up, 270 is left.
    private Vector2f[] gravity;
    private static final Vector2f STANDARD_GRAVITY = new Vector2f(0, .1f);

    public GravityField() {
        gravity = new Vector2f[width * height];
        for (int i = 0; i < width * height; i++) {
            gravity[i] = new Vector2f(STANDARD_GRAVITY);
        }
    }

    public void addBlackHole(Vector2f position) {
        int i = 0;
        int k = 0;
        for (Vector2f v : gravity) {
            Vector2f v2 = new Vector2f(i, k);
            v2.scale(32);
            v2.add(16);
            float distance = (float) Math.sqrt(Math.pow(position.x - v2.x, 2) + Math.pow(position.y - v2.y, 2));

            if (distance < 300) {
                float angle = (float) Math.toDegrees(Math.acos(Math.abs(v2.x - position.x) / distance));
                if (position.x > v2.x) {
                    if (position.y > v2.y) {
                        //Q1
                        v.x = (90 - angle) + 270;
                    } else {
                        //Q2
                        v.x = angle + 180;
                    }
                } else {
                    if (position.y > v2.y) {
                        //Q4
                        v.x = angle;
                    } else {
                        //Q3
                        v.x = 90 + (90 - angle);
                    }
                }

                v.y = 3f * ((16 - (distance / 32 + 1)) / 16f);
            }
            i++;
            if (i >= width) {
                k++;
                i = 0;
            }
        }
    }

    public Vector2f getAcceleration(Vector2f position) {
        Vector2f velocity = new Vector2f();
        int x = (int) (position.x / 32);
        int y = (int) (position.y / 32);
        if (width * y + x < gravity.length && x >= 0 && y >= 0) {

            Vector2f gravity = this.gravity[width * y + x];

            float xVel = gravity.y * (float) Math.cos(Math.toRadians(gravity.x + 90));
            float yVel = gravity.y * (float) Math.sin(Math.toRadians(gravity.x + 90));
            velocity = new Vector2f(xVel, yVel);
        }
        return velocity;
    }
}
