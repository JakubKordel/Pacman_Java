import view.GameMainFrame;

import javax.swing.*;
import java.awt.*;

public class Pacman {

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {
            JFrame ex = new GameMainFrame();
            ex.setVisible(true);
        });
    }
}