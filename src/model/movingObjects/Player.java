package model.movingObjects;

import model.arena.Map;
import org.newdawn.slick.geom.Vector2f;

public class Player extends MovingObject {

    public Player(Map map, Vector2f spawnPos, float speed) {
        super(map, spawnPos, speed);
        wantedDirection = Direction.NONE;
    }

    private Direction wantedDirection;

    @Override
    public Direction fetchNextDirection() {
        if (isMoveAvailable(wantedDirection))
            return wantedDirection;
        else if (isMoveAvailable(currentDirection))
            return currentDirection;
        else
            return Direction.NONE;
    }

    public void setWantedDirection(Direction direction){
        wantedDirection = direction;
    }

}
