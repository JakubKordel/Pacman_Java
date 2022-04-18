package model.staticObjects;

import controller.PacmanEngine;
import org.newdawn.slick.geom.Vector2f;

public class Apple extends Food {

    public Apple(Vector2f pos, boolean up, boolean down, boolean left, boolean right){
        super(pos, up, down, left, right);
    }

    @Override
    public void eat() {
        PacmanEngine.startEscape();
        PacmanEngine.points += 50;
        eaten = true;
    }
}
