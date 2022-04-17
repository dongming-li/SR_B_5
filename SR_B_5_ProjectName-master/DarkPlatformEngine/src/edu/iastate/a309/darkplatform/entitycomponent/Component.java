package edu.iastate.a309.darkplatform.entitycomponent;

import edu.iastate.a309.darkplatform.display.Screen;
import edu.iastate.a309.darkplatform.utility.Vector2f;

public interface Component {

    enum COMPONENT_TYPE {
        animation_component,
        collision_component
    }

    void update(Object o);

    void render(Screen screen, Vector2f position);

    String getName();

    COMPONENT_TYPE getComponentType();
}
