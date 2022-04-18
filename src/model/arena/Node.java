package model.arena;

import model.movingObjects.MovingObject;
import org.newdawn.slick.geom.Vector2f;

public class Node {
    final public boolean up;
    final public boolean down;
    final public boolean left;
    final public boolean right;
    final public Vector2f pos;

    public Node(Vector2f pos, boolean up, boolean down, boolean left, boolean right){
        this.up = up;
        this.down = down;
        this.left = left;
        this.right = right;
        this.pos = pos;
    }

    public void trigger(MovingObject movingObject){

    };

    public Vector2f getPos() {
        return pos;
    }
}
