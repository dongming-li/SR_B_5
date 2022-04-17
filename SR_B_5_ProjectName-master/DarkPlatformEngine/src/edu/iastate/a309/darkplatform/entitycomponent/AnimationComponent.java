package edu.iastate.a309.darkplatform.entitycomponent;

import edu.iastate.a309.darkplatform.display.Screen;
import edu.iastate.a309.darkplatform.graphics.Animation;
import edu.iastate.a309.darkplatform.utility.Vector2f;

import java.util.List;

public class AnimationComponent implements Component {

    private List<Animation> animations;
    private Animation currentAnimation;

    public boolean animationCompleted;

    public AnimationComponent(List<Animation> animations) {
        this.animations = animations;
        currentAnimation = animations.get(0);
    }

    public void update(Object o) {
        currentAnimation.update();
        if (currentAnimation.finishedOneCycle) animationCompleted = true;
    }

    public void render(Screen screen, Vector2f position) {
        currentAnimation.render(screen, position);
    }

    public String getName() {
        return "AnimationComponent";
    }

    public String getAnimationName() {
        return currentAnimation.getName();
    }

    public COMPONENT_TYPE getComponentType() {
        return COMPONENT_TYPE.animation_component;
    }

    public boolean equals(Object o) {
        if (o instanceof AnimationComponent) return true;
        return false;
    }

    public void setAnimation(String name) {
        //TODO: Error if name not found
        for (Animation a : animations) {
            if (a.getName().matches(name)) {
                currentAnimation = a;
                currentAnimation.resetTimer();
                currentAnimation.reset();
                break;
            }
        }
        animationCompleted = false;
    }

    public void resetCompletion(){
        animationCompleted = false;
        currentAnimation.finishedOneCycle = false;
    }

    public int getNumFrames(){
        return currentAnimation.sprites.length;
    }
}
