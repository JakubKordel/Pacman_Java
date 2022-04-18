package model.movingObjects;

import model.arena.Map;
import org.newdawn.slick.geom.Vector2f;

public class Clyde extends Ghost {

    static Strategy hunt;

    public Clyde(Map map, Vector2f spawnPos, float speed, Vector2f cagePos, Player player){
        super(map, spawnPos, speed, cagePos, player);

        hunt = new Strategy();

        hunt.priority[0][0][0][0] = Direction.UP;
        hunt.priority[0][0][1][0] = Direction.LEFT;
        hunt.priority[0][0][1][1] = Direction.RIGHT;
        hunt.priority[0][0][2][0] = Direction.DOWN;

        hunt.priority[0][1][0][0] = Direction.UP;
        hunt.priority[0][1][1][0] = Direction.LEFT;
        hunt.priority[0][1][2][0] = Direction.RIGHT;
        hunt.priority[0][1][3][0] = Direction.DOWN;

        hunt.priority[0][2][0][0] = Direction.UP;
        hunt.priority[0][2][1][0] = Direction.LEFT;
        hunt.priority[0][2][1][1] = Direction.RIGHT;
        hunt.priority[0][2][2][0] = Direction.DOWN;

        hunt.priority[0][3][0][0] = Direction.UP;
        hunt.priority[0][3][1][0] = Direction.RIGHT;
        hunt.priority[0][3][2][0] = Direction.LEFT;
        hunt.priority[0][3][3][0] = Direction.DOWN;

        hunt.priority[0][4][0][0] = Direction.UP;
        hunt.priority[0][4][1][0] = Direction.LEFT;
        hunt.priority[0][4][1][1] = Direction.RIGHT;
        hunt.priority[0][4][2][0] = Direction.DOWN;

        hunt.priority[1][0][0][0] = Direction.RIGHT;
        hunt.priority[1][0][1][0] = Direction.UP;
        hunt.priority[1][0][2][0] = Direction.LEFT;
        hunt.priority[1][0][3][0] = Direction.DOWN;

        hunt.priority[1][1][0][0] = Direction.RIGHT;
        hunt.priority[1][1][1][0] = Direction.UP;
        hunt.priority[1][1][2][0] = Direction.LEFT;
        hunt.priority[1][1][3][0] = Direction.DOWN;

        hunt.priority[1][2][0][0] = Direction.UP;
        hunt.priority[1][2][1][0] = Direction.RIGHT;
        hunt.priority[1][2][2][0] = Direction.DOWN;
        hunt.priority[1][2][3][0] = Direction.LEFT;

        hunt.priority[1][3][0][0] = Direction.UP;
        hunt.priority[1][3][1][0] = Direction.RIGHT;
        hunt.priority[1][3][2][0] = Direction.DOWN;
        hunt.priority[1][3][3][0] = Direction.LEFT;

        hunt.priority[1][4][0][0] = Direction.UP;
        hunt.priority[1][4][0][1] = Direction.RIGHT;
        hunt.priority[1][4][1][0] = Direction.LEFT;
        hunt.priority[1][4][1][1] = Direction.DOWN;

    }


    @Override
    Direction defaultMove() {
        boolean up = isMoveAvailable(Direction.UP);
        boolean down = isMoveAvailable(Direction.DOWN);
        boolean left = isMoveAvailable(Direction.LEFT);
        boolean right =isMoveAvailable(Direction.RIGHT);
        switch(currentDirection){
            case LEFT:
                right = false;
                break;
            case RIGHT:
                left = false;
                break;
            case UP:
                down = false;
                break;
            case DOWN:
                up = false;
                break;
            default:
                break;
        }
        return Direction.rand(up, down, left, right );
    }

    @Override
    Direction huntMove() {
        return symmetricalPriorityStrategy(hunt, false);
    }

    @Override
    Direction escapeMove() {
        return symmetricalPriorityStrategy(hunt, true);
    }
}
