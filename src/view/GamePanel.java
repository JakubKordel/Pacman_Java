package view;

import controller.PacmanEngine;
import model.arena.MapScheme;
import model.movingObjects.*;
import model.staticObjects.Apple;
import model.staticObjects.Food;
import model.staticObjects.Orange;
import view.image.ImageFactory;
import view.image.Img;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {

    static final float sizeMultiplier = 25;
    static final float xShift = 20;
    static final float yShift = 20;
    public static int width;
    public static int height;


    private final int DELAY = 16;

    private ArrayList<Line2D> lines;
    private Thread animator;
    private MovingObjectView [] movingObjectsView;
    private StaticObjectView [] staticObjectsView;
    private MapScheme mapScheme;
    private Menu menu;
    private int escapingGhostAnimationTimer = 0;
    private int animationInterval = 40;


    public GamePanel(MapScheme mapScheme, MovingObject [] movingObjects, ArrayList<Food> foodList){
        initializeVariables(mapScheme, movingObjects, foodList);
        initializeLayout();
        menu = new Menu();
     }

    @Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this);
        animator.start();
    }

    private void initializeVariables(MapScheme mapScheme, MovingObject [] movingObjects, ArrayList<Food> foodList) {
        this.mapScheme = mapScheme;
        lines = new ArrayList<>();
        initLines();
        movingObjectsView = new MovingObjectView[movingObjects.length];
        for (int i = 0; i < movingObjects.length; ++i ){
            if (movingObjects[i] instanceof Player){
                movingObjectsView[i] = new MovingObjectView(movingObjects[i], ImageFactory.createImage(Img.PACMAN).getImage());
            } else if (movingObjects[i] instanceof Blinky){
                movingObjectsView[i] = new MovingObjectView(movingObjects[i], ImageFactory.createImage(Img.BLINKY).getImage());
            } else if (movingObjects[i] instanceof Clyde){
                movingObjectsView[i] = new MovingObjectView(movingObjects[i], ImageFactory.createImage(Img.CLYDE).getImage());
            } else if (movingObjects[i] instanceof Inky){
                movingObjectsView[i] = new MovingObjectView(movingObjects[i], ImageFactory.createImage(Img.INKY).getImage());
            } else if (movingObjects[i] instanceof Pinky){
                movingObjectsView[i] = new MovingObjectView(movingObjects[i], ImageFactory.createImage(Img.PINKY).getImage());
            }
        }
        staticObjectsView = new StaticObjectView[foodList.size()];
        for (int i = 0; i < foodList.size(); ++i){
            if (foodList.get(i) instanceof Apple){
                staticObjectsView[i] = new StaticObjectView(foodList.get(i), ImageFactory.createImage(Img.APPLE).getImage());
            } else if (foodList.get(i) instanceof Orange) {
                staticObjectsView[i] = new StaticObjectView(foodList.get(i), ImageFactory.createImage(Img.ORANGE).getImage());
            }
        }
    }

    void initializeLayout(){
        width = (int) (mapScheme.columns * sizeMultiplier + xShift);
        height = (int) (mapScheme.rows * sizeMultiplier + yShift + 50);
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
     }

    @Override
     protected void paintComponent(Graphics g){
         super.paintComponent(g);

         if (PacmanEngine.window == PacmanEngine.Window.GAME) {

             drawLines(g);
             drawStaticObjects(g);
             drawMovingObjects(g);

             drawScorePanel(g);
         } else if (PacmanEngine.window == PacmanEngine.Window.MENU){
            menu.render(g);
         } else if (PacmanEngine.window == PacmanEngine.Window.HELP){
             Font font = new Font("arial", Font.BOLD, 15);
             g.setFont(font);
             g.setColor(Color.WHITE);
             g.drawString("Move using a, w, s, d. You can pause a game using esc." , 10, 15);
             g.drawString("To switch between game (or help) and menu also use esc." , 10, 30);
             g.drawString("After you eat apple (red one) you are able to eat escaping ghosts to get more points." , 10, 45);
             g.drawString("Every next level game gets harder, good luck!" , 10, 60);
         }
     }

    private void drawScorePanel(Graphics g) {
        if (PacmanEngine.lifes > 0){
            g.drawImage(ImageFactory.createImage(Img.PACMAN).getImage(), 20, height - 60, this);
            if (PacmanEngine.lifes > 1) {
                g.drawImage(ImageFactory.createImage(Img.PACMAN).getImage(), 50, height - 60, this);
                if (PacmanEngine.lifes > 2) {
                    g.drawImage(ImageFactory.createImage(Img.PACMAN).getImage(), 80, height - 60, this);
                }
            }
        }

        Font font = new Font("arial", Font.BOLD, 20);
        g.setFont(font);
        g.setColor(Color.YELLOW);
        g.drawString("Score: " + PacmanEngine.points, 200, height - 42);
        g.drawString("Level: " + PacmanEngine.level , 400, height - 42);
        if (PacmanEngine.state == PacmanEngine.GameState.LOST){
            font = new Font("arial", Font.BOLD, 30);
            g.setFont(font);
            g.setColor(Color.RED);
            g.drawString("GAME OVER ", width/2 - 100, height/2 - 50);
        }

    }

    private void drawStaticObjects(Graphics g) {
        for (int i = 0; i < staticObjectsView.length ; ++i) {
            if (staticObjectsView[i].isDraw())
                g.drawImage(staticObjectsView[i].getImage(), staticObjectsView[i].getX(), staticObjectsView[i].getY(), this);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawMovingObjects(Graphics g){
        for (int i = 0; i < movingObjectsView.length ; ++i) {
            if (movingObjectsView[i].isDraw()) {
                if (PacmanEngine.state == PacmanEngine.GameState.ESCAPE && movingObjectsView[i].movingObject instanceof Ghost && escapingGhostAnimationTimer > animationInterval ){
                    g.drawImage(ImageFactory.createImage(Img.ESCAPING_GHOST).getImage(), movingObjectsView[i].getX(), movingObjectsView[i].getY(), this);
                }
                else{
                    g.drawImage(movingObjectsView[i].getImage(), movingObjectsView[i].getX(), movingObjectsView[i].getY(), this);
                }
            }
        }
        if (PacmanEngine.state == PacmanEngine.GameState.ESCAPE) {
            ++escapingGhostAnimationTimer;
            if (escapingGhostAnimationTimer > 1.5 * animationInterval)
                escapingGhostAnimationTimer = 0;
        }
        Toolkit.getDefaultToolkit().sync();
     }

     private void drawLines(Graphics g){
         Graphics2D g2 = (Graphics2D) g;
         g.setColor(new Color(0,70,0));
         for (int i = 0; i < lines.size(); ++i) {
             g2.draw(lines.get(i));
         }
     }

     private void initLines(){
         for ( int j = 1; j < mapScheme.walls.length - 1 ; ++j ){
             for ( int i = 1; i < mapScheme.walls[0].length - 1; ++i ){
                 if ( mapScheme.walls[j][i] > 0 && mapScheme.walls[j][i+1] > 0 && (!(mapScheme.walls[j-1][i] > 0 && mapScheme.walls[j-1][i+1] > 0)
                         || !(mapScheme.walls[j+1][i] > 0 && mapScheme.walls[j+1][i+1] > 0) ) ){
                     lines.add(new Line2D.Float((i-1)*sizeMultiplier + xShift, (j-1)*sizeMultiplier + yShift, (i)*sizeMultiplier + xShift, (j-1)*sizeMultiplier+ yShift));
                 }

                 if ( mapScheme.walls[j][i] > 0 && mapScheme.walls[j+1][i] > 0 && (!(mapScheme.walls[j][i-1] > 0 && mapScheme.walls[j+1][i-1] > 0)
                         || !(mapScheme.walls[j][i+1] > 0 && mapScheme.walls[j+1][i+1] > 0) ) ){
                    lines.add(new Line2D.Float((i-1)*sizeMultiplier + xShift, (j-1)*sizeMultiplier + yShift, (i-1)*sizeMultiplier + xShift, j*sizeMultiplier + yShift));
                 }
             }
         }
     }

    @Override
    public void run() {

        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

            PacmanEngine.update();
            repaint();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) {
                sleep = 2;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {

                String msg = String.format("Thread interrupted: %s", e.getMessage());

                JOptionPane.showMessageDialog(this, msg, "Error",
                        JOptionPane.ERROR_MESSAGE);
            }

            beforeTime = System.currentTimeMillis();
        }
    }
}
