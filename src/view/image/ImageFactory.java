package view.image;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ImageFactory {
    public static ImageIcon createImage(Img img){
        ImageIcon imageIcon = null;
        switch (img){
            case PACMAN:
                switch (System.getProperty("os.name")) {
                    case "Linux":  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Pacman.png"));
                        break;
                    default:  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Pacman.png"));;
                        break;
                }
                break;
            case BLINKY:
                switch (System.getProperty("os.name")) {
                    case "Linux":  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Blinky.png"));
                        break;
                    default:  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Blinky.png"));;
                        break;
                }
                break;
            case INKY:
                switch (System.getProperty("os.name")) {
                    case "Linux":  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Inky.png"));
                        break;
                    default:  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Inky.png"));;
                        break;
                }
                break;
            case PINKY:
                switch (System.getProperty("os.name")) {
                    case "Linux":  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Pinky.png"));
                        break;
                    default:  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Pinky.png"));;
                        break;
                }
                break;
            case CLYDE:
                switch (System.getProperty("os.name")) {
                    case "Linux":  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Clyde.png"));
                        break;
                   default:  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Clyde.png"));;
                        break;
                }
                break;
            case APPLE:
                switch (System.getProperty("os.name")) {
                    case "Linux":  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Apple.png"));
                        break;
                    default:  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Apple.png"));;
                        break;
                }
                break;
            case ORANGE:
                switch (System.getProperty("os.name")) {
                    case "Linux":  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Orange.png"));
                        break;
                    default:  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "Orange.png"));;
                        break;
                }
                break;
            case ESCAPING_GHOST:
                switch (System.getProperty("os.name")) {
                    case "Linux":  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "EscapingGhost.png"));
                        break;
                    default:  imageIcon = new ImageIcon(ClassLoader.getSystemResource( "EscapingGhost.png"));;
                        break;
                }
                break;
            default:
                break;

        }
        return imageIcon;

    }
}
