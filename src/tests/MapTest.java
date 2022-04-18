import model.arena.Map;
import model.arena.MapScheme;
import model.staticObjects.Apple;
import model.staticObjects.Orange;
import model.staticObjects.Portal;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapTest {
    @Test
    public void TestMap(){
        MapScheme mapScheme = new MapScheme();
        Map map = new Map(mapScheme);

        assertTrue(map.node[0][0] == null);
        assertTrue(map.node[10][1] instanceof Orange);
        assertTrue(map.node[1][14] instanceof Portal);
        assertTrue(map.node[26][3] instanceof Apple);
        assertTrue(map.node[8][4] == null);
        assertTrue(map.node[10][30] == null);
        Portal portal = (Portal) map.node[1][14];
        assertTrue(((int)portal.getPair().getPos().x) == 26 );
        assertTrue(((int) portal.getPair().getPos().y ) == 14);
    }

}