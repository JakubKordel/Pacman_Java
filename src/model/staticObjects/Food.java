package model.staticObjects;

import controller.PacmanEngine;
import model.arena.Node;
import model.movingObjects.MovingObject;
import model.movingObjects.Player;
import org.newdawn.slick.geom.Vector2f;

public abstract class Food extends Node {
    protected boolean eaten = false;
    public boolean isEaten(){
        return eaten;
    }

    public abstract void eat();

    public void reset(){
        eaten = false;
    }

    Food(Vector2f pos, boolean up, boolean down, boolean left, boolean right){
        super(pos, up, down, left, right);
    }

    @Override
    public void trigger(MovingObject movingObject){
        if (movingObject instanceof Player && !isEaten()){
            eat();
            --PacmanEngine.foodLeft;
        }
    }
}
