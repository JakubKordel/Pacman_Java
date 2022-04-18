package model.movingObjects;
import model.arena.Map;
import org.newdawn.slick.geom.Vector2f;

public abstract class MovingObject {

    public static Vector2f getSpeed(Direction dir, float speedVal){
        switch (dir) {
            case UP:
                return new Vector2f(0, -1*speedVal);
            case DOWN:
                return new Vector2f(0, speedVal);
            case LEFT:
                return new Vector2f(-1*speedVal, 0);
            case RIGHT:
                return new Vector2f(speedVal, 0);
            default:
                return new Vector2f(0,0);
        }
    }

    final Map map;
    protected Direction currentDirection;
    /* Position related to map scheme */
    Vector2f pos;
    Vector2f spawnPos; //on paths
    float speed; //  0 < speed < 1
    boolean reverseWanted;

    MovingObject(Map map, Vector2f spawnPos, float speed){
        this.map = map;
        this.pos = new Vector2f(spawnPos);
        this.speed = speed;
        this.spawnPos = new Vector2f(spawnPos);
        currentDirection = Direction.NONE;
        reverseWanted = false;
    }

    public void move(){
        if (currentDirection == Direction.NONE){
            currentDirection = fetchNextDirection();
        } else if(reverseWanted){
            reverseWanted = false;
            currentDirection = Direction.opposite(currentDirection);
        }

        Vector2f prevPos = new Vector2f(pos);
        pos.add(getSpeed(currentDirection, speed));
        if((int)prevPos.x != (int)pos.x || (int)prevPos.y != (int)pos.y){
            float speedRemainder;
            switch (currentDirection) {
                case UP:
                    speedRemainder = ((float) (int)prevPos.y) - pos.y;
                    pos.y = (float) (int)prevPos.y;
                    break;
                case DOWN:
                    speedRemainder = pos.y - ((float) (int) pos.y);
                    pos.y = (float) (int)pos.y;
                    break;
                case LEFT:
                    speedRemainder = ((float) (int)prevPos.x) - pos.x;
                    pos.x = (float) (int)prevPos.x;
                    break;
                case RIGHT:
                    speedRemainder = pos.x - ((float) (int) pos.x);
                    pos.x = (float) (int)pos.x;
                    break;
                default:
                    speedRemainder = 0;
                    break;
            }
            map.node[(int)pos.x][(int)pos.y].trigger(this);
            currentDirection = fetchNextDirection();
            if (speedRemainder == 0){ //unlikely case, but possible
                speedRemainder = 0.00001f;
            }
            if (!isMoveAvailable(currentDirection)){
                currentDirection = Direction.NONE;
            }
            pos.add(getSpeed(currentDirection, speedRemainder));
        }
    }

    public void reverse(){
        reverseWanted = true;
    }

    public boolean isMoveAvailable(Direction d){
        int x = (int)pos.x;
        int y = (int)pos.y;
        switch(d){
            case UP:
                if ((float)x == pos.x && map.node[x][y].up)
                    return true;
                return false;
            case DOWN:
                if ((float)x == pos.x && map.node[x][y].down)
                    return true;
                return false;
            case LEFT:
                if ((float)y == pos.y && map.node[x][y].left)
                    return true;
                return false;
            case RIGHT:
                if ((float)y == pos.y && map.node[x][y].right)
                    return true;
                return false;
            default:
                return true;
        }
    }

    public Vector2f getPos(){
        return pos;
    }

    public void setPos(Vector2f pos){
        this.pos = new Vector2f(pos);
    }

    public Direction getCurrentDirection(){
        return currentDirection;
    }

    public void respawn(){
        pos = new Vector2f(spawnPos);
        currentDirection = Direction.NONE;
    }

    public abstract Direction fetchNextDirection() ;

    public float getSpeed(){
        return speed;
    }

}

