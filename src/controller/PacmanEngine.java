package controller;

import model.arena.Map;
import model.arena.MapScheme;
import model.movingObjects.*;
import model.staticObjects.Food;
import org.newdawn.slick.geom.Vector2f;
import view.GamePanel;

import java.util.ArrayList;

public class PacmanEngine {

    public static Player player;
    public static Blinky blinky;
    public static Pinky pinky;
    public static Clyde clyde;
    public static Inky inky;
    public static MovingObject [] movingObjects;
    public static ArrayList<Food> foodList;
    public static MapScheme mapScheme;
    public static Map map;
    public enum GameState { BEFOREROUND, RANDOM, HUNT, ESCAPE, LOST, WON, LIFELOST };
    public static GameState state = GameState.BEFOREROUND;
    public static int level = 0;
    public static int points = 0;
    public static int lifes = 3;
    public static int foodNum;
    public static int foodLeft;
    public static boolean isStartWanted = true;

    public static int stateTimer = 0;

    public static int inCageTime = 180;
    public static int randomTime = 660;
    public static int huntTime = 540;
    public static int escapeTime = 660;

    public static Window window = Window.MENU;

    public enum Window {
        MENU, GAME, HELP
    }

    public static void initialize(){
        mapScheme = new MapScheme();
        map = new Map(mapScheme);
        foodList = new ArrayList<>();
        for (int x = 0; x < map.node.length; ++x){
            for (int y = 0; y < map.node[0].length; ++y){
                if (map.node[x][y] != null && map.node[x][y] instanceof Food ){
                    foodList.add((Food) map.node[x][y]);
                }
            }
        }

        foodNum = foodList.size();
        foodLeft = foodNum;

        player = new Player(map, new Vector2f(13, 26), 0.1f);
        blinky = new Blinky(map, new Vector2f(13, 11), 0.1f, new Vector2f(12, 14), player);
        inky = new Inky(map, new Vector2f(13, 11), 0.1f, new Vector2f(16, 14), player);
        clyde = new Clyde(map, new Vector2f(13, 11), 0.1f, new Vector2f(11, 14), player);
        pinky = new Pinky(map, new Vector2f(13, 11), 0.1f, new Vector2f(15, 14), player);
        movingObjects = new MovingObject[5];
        movingObjects[0] = player;
        movingObjects[1] = blinky;
        movingObjects[2] = inky;
        movingObjects[3] = pinky;
        movingObjects[4] = clyde;
    }

    public static void update(){
        if (window == Window.GAME) {
            ++stateTimer;
            if (foodLeft == 0) state = GameState.WON;
            if (state != GameState.BEFOREROUND && state != GameState.LIFELOST) {
                for (MovingObject m : movingObjects) {
                    if (m instanceof Ghost) {
                        Ghost g = (Ghost) m;
                        if (g.isInCage()) {
                            ++g.timeInCage;
                            if (g.timeInCage == inCageTime) {
                                g.respawn();
                            }
                        }
                    }
                }
            }
            if (state == GameState.BEFOREROUND) {
                if (isStartWanted) startRound();
            } else if (state == GameState.RANDOM && playerGhostCollisionCheck() == null) {
                moveObjects();
                if (stateTimer == randomTime) startHunt();
            } else if (state == GameState.HUNT && playerGhostCollisionCheck() == null) {
                moveObjects();
                if (stateTimer == huntTime) startRandom();
            } else if (state == GameState.ESCAPE) {
                moveObjects();
                Ghost g = playerGhostCollisionCheck();
                if (g != null) {
                    g.goToCage();
                    points += 40;
                }
                if (stateTimer == escapeTime) startRandom();
            } else if (state == GameState.LOST) {
                if (isStartWanted) reset();
            } else if (state == GameState.WON) {
                isStartWanted = false;
                resetPositions();
                resetFoods();
                state = GameState.BEFOREROUND;
            } else if (playerGhostCollisionCheck() != null) {
                --lifes;
                isStartWanted = false;
                if (lifes > 0) {
                    resetPositions();
                    state = GameState.LIFELOST;
                } else {
                    state = GameState.LOST;
                }

            } else if (state == GameState.LIFELOST && isStartWanted) {
                startRandom();
            }
        }
    }

    public static GamePanel createGamePanel(){
        return new GamePanel(mapScheme, movingObjects, foodList);
    }

    public static Ghost playerGhostCollisionCheck(){
        for ( MovingObject m : movingObjects ){
            if (m instanceof Ghost) {
                Ghost g = (Ghost) m;
                if ( Math.abs( player.getPos().x - g.getPos().x) < g.getSpeed() + player.getSpeed() && Math.abs( player.getPos().y - g.getPos().y ) < g.getSpeed() + player.getSpeed() )
                    return g;
            }
        }
        return null;
    }

    public static void startRound(){
        ++level;
        startRandom();
        inky.timeInCage = inCageTime - 1;
        clyde.timeInCage = inCageTime - 1;
        pinky.timeInCage = inCageTime - 1;
        blinky.timeInCage = inCageTime - 1;
        huntTime += 60;
        if ( randomTime > 61 ) randomTime -= 60;
        if ( escapeTime > 61 ) escapeTime -= 60;
    }


    public static void startHunt(){
        state = GameState.HUNT;
        for ( MovingObject m : movingObjects ){
            if (m instanceof Ghost)
                ((Ghost) m).huntMode();
        }
        stateTimer = 0;
    }

    public static void startRandom(){
        state = GameState.RANDOM;
        for ( MovingObject m : movingObjects ){
            if (m instanceof Ghost)
                ((Ghost) m).defaultMode();
        }
        stateTimer = 0;
    }

    public static void startEscape(){
        state = GameState.ESCAPE;
        for ( MovingObject m : movingObjects ){
            if (m instanceof Ghost)
                ((Ghost) m).escapeMode();
        }
        stateTimer = 0;
    }

    public static void moveObjects(){
        for ( MovingObject m : movingObjects ){
            m.move();
        }
    }

    public static void resetPositions(){
        for ( MovingObject m : movingObjects ){
            if (m instanceof Ghost)
                ((Ghost) m).goToCage();
        }
        inky.timeInCage = inCageTime - 1;
        clyde.timeInCage = inCageTime - 1;
        pinky.timeInCage = inCageTime - 1;
        blinky.timeInCage = inCageTime - 1;
        player.respawn();
    }

    public static void resetFoods(){
        for ( Food f : foodList ) f.reset();
        foodLeft = foodNum;
    }

    public static void reset(){
        resetPositions();
        resetFoods();
        state = GameState.BEFOREROUND;
        level = 0;
        lifes = 3;
        points = 0;
        inCageTime = 180;
        huntTime = 540;
        randomTime = 660;
        escapeTime = 1200;
        level = 0;
    }
}
