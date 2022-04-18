package model.movingObjects;

import model.arena.Map;
import org.newdawn.slick.geom.Vector2f;

import java.util.ArrayDeque;
import java.util.Queue;

public abstract class Ghost extends MovingObject {

    /*  LU  U   RU
     *  L   G   R           G - Ghost position
     *  LD  D   RD
     *
      */
    public enum PlayerPos {
        RU(0), U(1), LU(2), L(3), LD(4), D(5), RD(6), R(7), G(8);

        public final int num;

        PlayerPos(int num) {
            this.num = num;
        }

        // values of degrees: 0 - 0 degrees, 1 - 90 degrees, 2 - 180 degrees, 3 - 270 degrees ... (counterclockwise)
        static PlayerPos rot(PlayerPos original, int degrees){
            if (original != PlayerPos.G) {
                int newPos = (original.num + 2 * degrees) % 8;
                switch (newPos){
                    case 0: return PlayerPos.RU;
                    case 1: return PlayerPos.U;
                    case 2: return PlayerPos.LU;
                    case 3: return PlayerPos.L;
                    case 4: return PlayerPos.LD;
                    case 5: return PlayerPos.D;
                    case 6: return PlayerPos.RD;
                    case 7: return PlayerPos.R;
                    default:
                        return null;
                }
            } else return PlayerPos.G;
        }
    }

    enum State { DEFAULT, HUNT, ESCAPE };

    Vector2f cagePos;
    Player player;
    State state;

    private boolean inCage;

    public int timeInCage;

    Ghost(Map map, Vector2f spawnPos, float speed, Vector2f cagePos, Player player){
        super(map, spawnPos, speed);
        this.cagePos = new Vector2f(cagePos);
        this.player = player;
        state = State.DEFAULT;
        timeInCage = 0;
        inCage = false;
    }

    public void huntMode(){
        state = State.HUNT;
    }

    public void escapeMode() { state = State.ESCAPE; }

    public void defaultMode(){
        state = State.DEFAULT;
    }

    @Override
    public void respawn(){
        inCage = false;
        pos = new Vector2f(spawnPos);
    }

    public void goToCage(){
        inCage = true;
        pos = new Vector2f(cagePos);
        timeInCage = 0;
        currentDirection = Direction.NONE;
    }

    public boolean isInCage() {
        return inCage;
    }

    @Override
    public Direction fetchNextDirection() {
        if (!isInCage()) {
            switch (state) {
                case DEFAULT:
                    return defaultMove();
                case HUNT:
                    return huntMove();
                case ESCAPE:
                    return escapeMove();
                default:
                    return Direction.NONE;
            }
        }
        else
            return Direction.NONE;
    }

    PlayerPos trackPlayer(){
        if ( player.getPos().y < getPos().y ){
            if ( player.getPos().x < getPos().x ) return PlayerPos.LU;
            else if ( player.getPos().x > getPos().x ) return PlayerPos.RU;
            else return PlayerPos.U;
        }
        else if ( player.getPos().y == getPos().y ){
            if ( player.getPos().x < getPos().x ) return PlayerPos.L;
            else if ( player.getPos().x > getPos().x ) return PlayerPos.R;
            else return PlayerPos.G;
        }
        else {
            if ( player.getPos().x < getPos().x ) return PlayerPos.LD;
            else if ( player.getPos().x > getPos().x ) return PlayerPos.RD;
            else return PlayerPos.D;
        }
    }

    /* Object of class strategy defines priorities of directions when player is in U or or RU direction.
        Object is intended to be passed to symmetricalPriorityStrategy function.
     */
    class Strategy {
        /*
        * priority[P][M][O][X]
        * Group P = 0 defines strategy when player is in position U.
        * Group P = 1 defines strategy when player is on position RU.
        * Group M = 0 defines strategy when player is moving up.
        * Group M = 1 player moving left.
        * Group M = 2 player moving down.
        * Group M = 3 player moving right.
        * Group M = 4 player is not moving.
        * O - groups elements with equal priority (max two elements can have the same strategy)
        * O = 0 the highest priority
        * O = 3 the lowest priority
        * Directions placed with same P, M, O, will be treated equally (random choice).
         * */
        Direction [][][][] priority = new Direction[2][5][4][4];
    }

    // if reverse is set to true then strategy will be reversed. (if UP was defined as highest priority then DOWN will have the highest priority )
    Direction symmetricalPriorityStrategy(Strategy strategy, boolean reverse){
        //We assume strategy is symmetrical for all directions, so rotation to one case is applied. This solution reduces amount of code.

        PlayerPos playerPosRotated;
        Direction playerDirectionRotated;
        Direction newDirection;
        int rotation; // (counterclockwise) 0 - degrees, 1 - 90 degrees, 2 - 180 degrees, 3 - 270 degrees.
        int p, m;
        //rotate to the case in which player is in U or UR direction
        switch (trackPlayer()){
            case L:
            case LU:
                rotation = 3;
                break;
            case D:
            case LD:
                rotation = 2;
                break;
            case R:
            case RD:
                rotation = 1;
                break;
            case U:
            case RU: default:
                rotation = 0;
                break;
        }

        playerPosRotated = PlayerPos.rot(trackPlayer(), rotation);
        playerDirectionRotated = Direction.rot(player.getCurrentDirection(), rotation);

        Queue<Direction> queue = new ArrayDeque<>();

        if (playerPosRotated == PlayerPos.U)
            p = 0;
        else if (playerPosRotated == PlayerPos.RU) p = 1;
        else
            return Direction.NONE; // Position of player == position of Ghost

        m = playerDirectionRotated.num;

        for (int i = 0; i < 4; ++i) {
            if (strategy.priority[p][m][i][0] == null || strategy.priority[p][m][i][0] == Direction.NONE)
                continue;
            if (strategy.priority[p][m][i][1] == null || strategy.priority[p][m][i][1] == Direction.NONE) {
                queue.add(strategy.priority[p][m][i][0]);
            } else {
                //There is more than one direction with same priority, queue them in random order
                boolean up = false, down = false, right = false, left = false;
                int j = 0;
                while (strategy.priority[p][m][i][j] != null && j < 4 && strategy.priority[p][m][i][1] != Direction.NONE) {
                    switch (strategy.priority[p][m][i][j]) {
                        case DOWN:
                            down = true;
                            break;
                        case UP:
                            up = true;
                            break;
                        case LEFT:
                            left = true;
                            break;
                        case RIGHT:
                            right = true;
                            break;
                    }
                    ++j;
                }
                while (up || down || right || left) {
                    Direction d = Direction.rand(up, down, left, right);
                    queue.add(d);
                    switch (d) {
                        case DOWN:
                            down = false;
                            break;
                        case UP:
                            up = false;
                            break;
                        case LEFT:
                            left = false;
                            break;
                        case RIGHT:
                            right = false;
                            break;
                    }
                }
            }
        }
        while (!queue.isEmpty()){
            newDirection = queue.remove();
            //rotate back result
            newDirection = Direction.rot(newDirection, (4 - rotation)%4);
            if (reverse)
                newDirection = Direction.opposite(newDirection);
            if (isMoveAvailable(newDirection)){
                if (newDirection != Direction.opposite(currentDirection)
                        || (!isMoveAvailable(currentDirection) && !isMoveAvailable(Direction.rot(currentDirection, 1))
                        && !isMoveAvailable(Direction.rot(currentDirection, 3)))){
                    return newDirection;
                }
            }
        }
        return null;
    }

    abstract Direction defaultMove();
    abstract Direction huntMove();
    abstract Direction escapeMove();
}
