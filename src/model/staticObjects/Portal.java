package model.staticObjects;

import model.arena.Node;
import model.movingObjects.Direction;
import model.movingObjects.MovingObject;
import org.newdawn.slick.geom.Vector2f;

public class Portal extends Node {

    protected Portal pair;

    Direction direction; //Direction in which moving object can go after leaving portal

    public Portal(Vector2f pos, boolean up, boolean down, boolean left, boolean right){

        super(pos, up, down, left, right);
    }

    public void setPair(Portal pair){
        this.pair = pair;
    };

    @Override
    public void trigger(MovingObject movingObject){
        movingObject.setPos(new Vector2f(pair.pos));
    }
}
