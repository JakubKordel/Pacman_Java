package model.arena;

import model.staticObjects.Apple;
import model.staticObjects.Orange;
import model.staticObjects.Portal;
import org.newdawn.slick.geom.Vector2f;

public class Map {
    private final int width;
    private final int height;
    public Node[][] node;

    public Map(MapScheme scheme){
        width = scheme.columns;
        height = scheme.rows;
        node = new Node[width][height];
        boolean up = false, down = false, left = false, right = false;
        for (int r = 0; r < height; ++r ) {
            for (int c = 0; c < width; ++c){
                if ( scheme.tab[r][c] != 0 ) {
                    if (scheme.tab[r - 1][c] != 0) up = true;
                    else up = false;
                    if (scheme.tab[r + 1][c] != 0) down = true;
                    else down = false;
                    if (scheme.tab[r][c - 1] != 0) left = true;
                    else left = false;
                    if (scheme.tab[r][c + 1] != 0) right = true;
                    else right = false;
                }
                switch(scheme.tab[r][c]){
                    case 0:
                        node[c][r] = null;
                        break;
                    case 1:
                        node[c][r] = new Orange(new Vector2f((float) c, (float) r), up, down, left, right);
                        break;
                    case 2:
                        node[c][r] = new Apple(new Vector2f((float) c, (float) r), up, down, left, right);
                        break;
                    case 3:
                        node[c][r] = new Node(new Vector2f((float) c, (float) r), up, down, left, right);
                        break;
                    case 4:
                        node[c][r] = new Portal(new Vector2f((float) c, (float) r), up, down, left, right);
                        break;
                }
            }
        }

        //Connect portals
        for (int i = 0; i < scheme.portalsPairNum; ++i ){
            if (! (node[scheme.portal[i][0]][scheme.portal[i][1]] instanceof Portal) || ! (node[scheme.portal[i][2]][scheme.portal[i][3]] instanceof Portal)) {
                System.exit(0);
            } else {
                Portal portal1 = (Portal) node[scheme.portal[i][0]][scheme.portal[i][1]];
                Portal portal2 = (Portal) node[scheme.portal[i][2]][scheme.portal[i][3]];
                portal1.setPair(portal2);
                portal2.setPair(portal1);
            }
        }
    }


}
