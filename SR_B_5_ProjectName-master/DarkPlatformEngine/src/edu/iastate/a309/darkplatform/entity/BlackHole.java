package edu.iastate.a309.darkplatform.entity;

import edu.iastate.a309.darkplatform.entitycomponent.AnimationComponent;
import edu.iastate.a309.darkplatform.entitycomponent.CollisionComponent;
import edu.iastate.a309.darkplatform.graphics.Animation;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BlackHole extends Entity {


    public BlackHole(Vector2f position) {
        super(position);
        super.type = 3;
        List<Animation> anis = new ArrayList<Animation>();
        anis.add(Animation.createAnimation("1,0>2,0>3,0", "Default", 60));
        addComponent(new AnimationComponent(anis));
        addComponent(new CollisionComponent(new Rectangle((int) position.x, (int) position.y, 32, 32)));

    }
}
