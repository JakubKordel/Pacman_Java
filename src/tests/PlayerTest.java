import model.arena.Map;
import model.arena.MapScheme;
import model.movingObjects.Direction;
import model.movingObjects.Player;
import org.junit.*;
import org.newdawn.slick.geom.Vector2f;

import static org.junit.Assert.*;

public class PlayerTest {
    Player player;
    Map map;
    MapScheme scheme;
    @Before
    public void setUp() {
        scheme = new MapScheme();
        map = new Map(scheme);
        player = new Player(map, new Vector2f(13, 26), 0.3f );
    }

    @Test
    public void TestPlayerMovement(){
        player.setWantedDirection(Direction.LEFT);
        for (int i = 0; i < 10000; ++i) {
            player.move();
        }
        assertTrue((int)player.getPos().x == 4 && (int)player.getPos().y == 26);
        player.setWantedDirection(Direction.DOWN);
        for (int i = 0; i < 10000; ++i) {
            player.move();
        }
        assertTrue((int)player.getPos().x == 4 && (int)player.getPos().y == 29);
        player.setWantedDirection(Direction.RIGHT);
        player.move();
        player.setWantedDirection(Direction.UP);
        for (int i = 0; i < 10000; ++i) {
            player.move();
        }
        assertTrue((int)player.getPos().x == 23 && (int)player.getPos().y == 26);
        player.setWantedDirection(Direction.LEFT);
        player.move();
        player.setWantedDirection(Direction.DOWN);
        for (int i = 0; i < 10000; ++i) {
            player.move();
        }
        assertTrue((int)player.getPos().x == 4 && (int)player.getPos().y == 29);
    }
}