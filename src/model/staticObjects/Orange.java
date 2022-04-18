package model.staticObjects;

import controller.PacmanEngine;
import org.newdawn.slick.geom.Vector2f;

public class Orange extends Food {

    public Orange(Vector2f pos, boolean up, boolean down, boolean left, boolean right){
        super(pos, up, down, left, right);
    }

    @Override
    public void eat() {
        eaten = true;
        PacmanEngine.points += 10;
    }

}
