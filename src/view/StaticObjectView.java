package view;

import model.arena.Node;
import model.staticObjects.Food;

import java.awt.*;

public class StaticObjectView {
    Node node;
    private Image image;
    private boolean draw;

    StaticObjectView(Node node, Image image){
        this.node = node;
        this.image = image;
        this.draw = true;
    }

    int getX(){
        return (int) (node.getPos().x * GamePanel.sizeMultiplier + GamePanel.xShift - image.getWidth(null)/2);
    }

    int getY(){
        return (int) (node.getPos().y * GamePanel.sizeMultiplier + GamePanel.yShift - image.getHeight(null)/2);
    }

    Image getImage(){
        return image;
    }

    public boolean isDraw() {
        if (node instanceof Food){
            Food f = (Food) node;
            return !f.isEaten();
        } else
            return draw;
    }

    public void setToDraw() {
        draw = true;
    }

    public void stopDrawing() {
        draw = false;
    }
}
