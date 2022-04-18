import model.movingObjects.Direction;
import org.junit.Test;

import static org.junit.Assert.*;

public class DirectionTest {
    @Test
    public void TestRandDirection(){
        Direction dir = Direction.rand(true, false, true, false);
        assertTrue(dir == Direction.UP || dir == Direction.LEFT);

        dir = Direction.rand(false, true, true, true);
        assertTrue(dir == Direction.DOWN || dir == Direction.LEFT || dir == Direction.RIGHT);

        dir = Direction.rand(false, false, true, false);
        assertTrue(dir == Direction.LEFT);

    }

}