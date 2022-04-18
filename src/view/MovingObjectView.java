package view;

import model.movingObjects.MovingObject;

import java.awt.*;

public class MovingObjectView {
    MovingObject movingObject;
    private Image image;
    private boolean draw;

    MovingObjectView(MovingObject movingObject, Image image){
        this.movingObject = movingObject;
        this.image = image;
        this.draw = true;
    }

    int getX(){
        return (int) (movingObject.getPos().x * GamePanel.sizeMultiplier + GamePanel.xShift - image.getWidth(null)/2);
    }

    int getY(){
        return (int) (movingObject.getPos().y * GamePanel.sizeMultiplier + GamePanel.yShift - image.getHeight(null)/2);
    }

    Image getImage(){
        return image;
    }

    public boolean isDraw() {
        return draw;
    }

    public void setToDraw() {
        draw = true;
    }

    public void stopDrawing() {
        draw = false;
    }
}
