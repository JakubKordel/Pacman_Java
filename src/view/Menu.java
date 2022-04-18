package view;

import controller.PacmanEngine;

import java.awt.*;

public class Menu {

    public Rectangle playButton = new Rectangle(GamePanel.width/2 - 40, 150, 100, 50);
    public Rectangle helpButton = new Rectangle(GamePanel.width/2 - 40, 250, 100, 50);
    public Rectangle quitButton = new Rectangle(GamePanel.width/2 - 40, 350, 100, 50);

    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        Font font = new Font("arial", Font.BOLD, 50);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Pacman", GamePanel.width/2 - 100, 100);

        Font buttonFont = new Font("arial", Font.BOLD, 30);

        g.setFont(buttonFont);

        g.drawString("Play", playButton.x +  15, playButton.y + 35);
        g.drawString("Help", helpButton.x + 15, helpButton.y + 35);
        g.drawString("Quit", quitButton.x + 15, quitButton.y + 35);

        g2d.draw(playButton);
        g2d.draw(helpButton);
        g2d.draw(quitButton);
    }
}
