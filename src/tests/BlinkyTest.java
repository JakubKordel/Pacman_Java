import model.arena.Map;
import model.arena.MapScheme;
import model.movingObjects.Blinky;
import model.movingObjects.Direction;
import model.movingObjects.Player;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import static org.junit.Assert.*;

public class BlinkyTest {
    Player player;
    Map map;
    MapScheme scheme;
    Blinky blinky;

    @Before
    public void setUp() {
        scheme = new MapScheme();
        map = new Map(scheme);
        player = new Player(map, new Vector2f(13, 26), 0.3f );
        blinky = new Blinky(map, new Vector2f(13, 11), 0.4f, new Vector2f(13, 14), player);
    }

    @Test
    public void TestRandomMovement(){
        blinky.respawn();
        assertTrue(blinky.isMoveAvailable(Direction.LEFT) && blinky.isMoveAvailable(Direction.RIGHT));
        assertTrue(!blinky.isMoveAvailable(Direction.UP) && !blinky.isMoveAvailable(Direction.DOWN));
        Direction dir = blinky.fetchNextDirection();
        assertTrue(dir == Direction.LEFT || dir == Direction.RIGHT );
        for (int i = 0; i < 1000000; ++i ) {
            Vector2f prevPos = new Vector2f(blinky.getPos());
            blinky.move();
            assertTrue( blinky.getPos().x != prevPos.x || blinky.getPos().y != prevPos.y); //blinky must move
            assertTrue((float)(int)blinky.getPos().x == blinky.getPos().x || (float)(int)blinky.getPos().y == blinky.getPos().y  ); //Blinky must still be on path
        }
    }

    @Test
    public void TestHuntMovement(){
        blinky.huntMode();
        blinky.respawn();
        player.respawn();
        assertTrue(blinky.isMoveAvailable(Direction.LEFT) && blinky.isMoveAvailable(Direction.RIGHT));
        assertTrue(!blinky.isMoveAvailable(Direction.UP) && !blinky.isMoveAvailable(Direction.DOWN));
        Direction dir = blinky.fetchNextDirection();
        assertTrue(dir == Direction.LEFT || dir == Direction.RIGHT );
        for (int i = 0; i < 1000000; ++i ) {
            Vector2f prevPos = new Vector2f(blinky.getPos());
            blinky.move();
            assertTrue( blinky.getPos().x != prevPos.x || blinky.getPos().y != prevPos.y || blinky.getPos().distance(player.getPos()) == 0); //blinky must move
            assertTrue((float)(int)blinky.getPos().x == blinky.getPos().x || (float)(int)blinky.getPos().y == blinky.getPos().y  ); //Blinky must still be on path
        }
        assertTrue( blinky.getPos().distance(player.getPos()) == 0);
    }

    @Test
    public void TestEscapeMovement(){
        blinky.escapeMode();
        blinky.respawn();
        player.respawn();
        assertTrue(blinky.isMoveAvailable(Direction.LEFT) && blinky.isMoveAvailable(Direction.RIGHT));
        assertTrue(!blinky.isMoveAvailable(Direction.UP) && !blinky.isMoveAvailable(Direction.DOWN));
        Direction dir = blinky.fetchNextDirection();
        assertTrue(dir == Direction.LEFT || dir == Direction.RIGHT );
        for (int i = 0; i < 10000000; ++i ) {
            Vector2f prevPos = new Vector2f(blinky.getPos());
            blinky.move();
            assertTrue( blinky.getPos().x != prevPos.x || blinky.getPos().y != prevPos.y || blinky.getPos().distance(player.getPos()) == 0); //blinky must move
            assertTrue((float)(int)blinky.getPos().x == blinky.getPos().x || (float)(int)blinky.getPos().y == blinky.getPos().y  ); //Blinky must still be on path
            assertTrue( blinky.getPos().distance(player.getPos()) > 1);
        }
    }


}