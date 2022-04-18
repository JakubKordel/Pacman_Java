import model.arena.Map;
import model.arena.MapScheme;
import model.movingObjects.Blinky;
import model.movingObjects.Direction;
import model.movingObjects.Player;
import org.junit.Before;
import org.junit.Test;
import org.newdawn.slick.geom.Vector2f;

import static org.junit.Assert.*;

public class PortalTest {
    Player player;
    Map map;
    MapScheme scheme;
    Blinky [] blinkies;

    @Before
    public void setUp() {
        scheme = new MapScheme();
        map = new Map(scheme);
        player = new Player(map, new Vector2f(6, 14), 0.3f );
        blinkies = new Blinky[50];
        for (int i = 0; i < 50 ; ++i){
            blinkies[i] = new Blinky(map, new Vector2f(13, 11), 0.4f, new Vector2f(13, 14), player);
        }
    }

    @Test
    public void TestPlayerMovingThroughPortal(){
        player.setWantedDirection(Direction.LEFT);
        for (int i = 0; i < 1000; ++i) {
            player.move();
        }
        assertTrue(player.getPos().x == 21 && player.getPos().y == 14);

        player.setWantedDirection(Direction.RIGHT);
        for (int i = 0; i < 1000; ++i) {
            player.move();
        }
        assertTrue(player.getPos().x == 6 && player.getPos().y == 14);
    }

    @Test
    public void TestGhostMovingOnMap(){
        for (int i = 0; i < 50 ; ++i){
            blinkies[i].respawn();
        }
        for (int i = 0; i < 100000; ++i) {
            for (int j = 0; j < 50 ; ++j){
                blinkies[j].move();
            }
        }
    }


}