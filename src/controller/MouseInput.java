package controller;

import view.GamePanel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseInput implements MouseListener {

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (PacmanEngine.window == PacmanEngine.Window.MENU) {
            int x = e.getX();
            int y = e.getY();
            if (x >= GamePanel.width / 2 - 40 && x <= GamePanel.width / 2 + 60) {
                if (y >= 185 && y <= 235) {
                    PacmanEngine.window = PacmanEngine.Window.GAME;
                    if (PacmanEngine.state == PacmanEngine.GameState.LOST);
                        PacmanEngine.isStartWanted = true;
                } else if (y >= 280 && y <= 330) {
                    PacmanEngine.window = PacmanEngine.Window.HELP;
                } else if (y >= 380 && y <= 430) {
                    System.exit(0);
                }
            }
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
