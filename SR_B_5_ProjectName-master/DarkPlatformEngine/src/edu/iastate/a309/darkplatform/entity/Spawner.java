package edu.iastate.a309.darkplatform.entity;

import edu.iastate.a309.darkplatform.display.Screen;
import edu.iastate.a309.darkplatform.entitycomponent.AnimationComponent;
import edu.iastate.a309.darkplatform.global.GlobalFields;
import edu.iastate.a309.darkplatform.graphics.Animation;
import edu.iastate.a309.darkplatform.input.InputHandler;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Spawner extends Entity{

    private Entity entToSpawn;

    private int spawnDelay;

    public Spawner(Vector2f position, Entity entity, int spawnDelay){
        super(position);
        super.type = 6;
        entToSpawn = entity;

        List<Animation> anis = new ArrayList<Animation>();
        anis.add(Animation.createAnimation("5,1>6,1>7,1>8,1", "Default", spawnDelay));
        addComponent(new AnimationComponent(anis));
        this.spawnDelay = spawnDelay;
    }

    public void update(InputHandler input){
        if(!GlobalFields.EDIT_MODE){
            super.update(input);
            AnimationComponent animation = ((AnimationComponent)getComponent("AnimationComponent"));


            if(animation.animationCompleted){
                Entity entToAdd = entToSpawn.clone();
                GlobalFields.map.addEnt(entToAdd);
                animation.resetCompletion();
            }
        }
    }

    public void render(Screen screen){
        super.render(screen);
        if(GlobalFields.EDIT_MODE){
            entToSpawn.render(screen);
        }
    }

    public Entity getEntToSpawn() {
        return entToSpawn;
    }

    public int getSpawnDelay() {
        return spawnDelay;
    }
}
