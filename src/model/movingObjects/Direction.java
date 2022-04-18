package model.movingObjects;

public enum Direction {
    UP(0), LEFT(1), DOWN(2), RIGHT(3), NONE(4);

    public final int num;

    Direction(int num) {
        this.num = num;
    }

    // values of degrees: 0 - 0 degrees, 1 - 90 degrees, 2 - 180 degrees, 3 - 270 degrees ... (counterclockwise)
    static Direction rot(Direction original, int degrees){
        if (original != Direction.NONE) {
            int newDir = (original.num + degrees) % 4;
            switch (newDir){
                case 0: return Direction.UP;
                case 1: return Direction.LEFT;
                case 2: return Direction.DOWN;
                case 3: return Direction.RIGHT;
                default:
                    return null;
            }
        } else return Direction.NONE;
    }

    public static Direction rand(boolean up, boolean down, boolean left, boolean right){
        int count = ((up) ? 1 : 0) + ((down) ? 1 : 0) + ((left) ? 1 : 0) + ((right) ? 1 : 0);
        if (count == 0)
            return Direction.NONE;
        int random = (int)(Math.random() * count);

        if (up){
            if (random == 0)
                return Direction.UP;
            --random;
        }

        if (down){
            if (random == 0)
                return Direction.DOWN;
            --random;
        }

        if (left){
            if (random == 0)
                return Direction.LEFT;
            --random;
        }

        if (right){
            if (random == 0)
                return Direction.RIGHT;
        }
        return null;
    }

    public static Direction opposite(Direction direction){
        switch (direction) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
            default:
                return NONE;
        }
    }
}
