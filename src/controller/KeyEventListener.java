package controller;

import model.movingObjects.Direction;
import view.GamePanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyEventListener implements KeyListener {

    @Override
    public void keyPressed(KeyEvent keyEvent){
        if (PacmanEngine.window == PacmanEngine.Window.GAME) {
            if (keyEvent.getKeyChar() == 'a' || keyEvent.getKeyChar() == 'A') {
                PacmanEngine.player.setWantedDirection(Direction.LEFT);
                if (PacmanEngine.player.getCurrentDirection() == Direction.RIGHT)
                    PacmanEngine.player.reverse();
            } else if (keyEvent.getKeyChar() == 's' || keyEvent.getKeyChar() == 'S') {
                PacmanEngine.player.setWantedDirection(Direction.DOWN);
                if (PacmanEngine.player.getCurrentDirection() == Direction.UP)
                    PacmanEngine.player.reverse();
            } else if (keyEvent.getKeyChar() == 'd' || keyEvent.getKeyChar() == 'D') {
                PacmanEngine.player.setWantedDirection(Direction.RIGHT);
                if (PacmanEngine.player.getCurrentDirection() == Direction.LEFT)
                    PacmanEngine.player.reverse();
            } else if (keyEvent.getKeyChar() == 'w' || keyEvent.getKeyChar() == 'W') {
                PacmanEngine.player.setWantedDirection(Direction.UP);
                if (PacmanEngine.player.getCurrentDirection() == Direction.DOWN)
                    PacmanEngine.player.reverse();
            }

            if (PacmanEngine.state == PacmanEngine.GameState.WON || PacmanEngine.state == PacmanEngine.GameState.LIFELOST || PacmanEngine.state == PacmanEngine.GameState.BEFOREROUND ){
                PacmanEngine.isStartWanted = true;
            }
        }

        if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE){
            if (PacmanEngine.window == PacmanEngine.Window.GAME) {
                PacmanEngine.window = PacmanEngine.Window.MENU;
            } else if (PacmanEngine.window == PacmanEngine.Window.MENU) {
                PacmanEngine.window = PacmanEngine.Window.GAME;
            } else {
                PacmanEngine.window = PacmanEngine.Window.MENU;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent){

    }

    @Override
    public void keyTyped(KeyEvent keyEvent){

    }

}
