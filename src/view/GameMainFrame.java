package view;

import controller.KeyEventListener;
import controller.MouseInput;
import controller.PacmanEngine;
import model.arena.Map;
import model.arena.MapScheme;
import model.movingObjects.Blinky;
import model.movingObjects.Player;
import org.newdawn.slick.geom.Vector2f;

import javax.swing.*;

public class GameMainFrame extends JFrame {

    public GameMainFrame() {

        initUI();
    }

    private void initUI() {

        PacmanEngine.initialize();
        add(PacmanEngine.createGamePanel());
        addKeyListener(new KeyEventListener());
        addMouseListener(new MouseInput());

        setResizable(true);
        pack();

        setTitle("Pacman");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}

