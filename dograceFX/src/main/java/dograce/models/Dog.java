package dograce.models;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.paint.Color;

public class Dog
{
    private String dogName;
    private Color color;
    private double posX = 0;
    private int speed = 10;

    public void setPlayerName(String name)
    {
        dogName = name;
    }
    public String getPlayerName()
    {
        return dogName;
    }

    public void setColor(Color color)
    {
        this.color = color;
    }
    public Color getColor()
    {
        return color;
    }

    public void setPosX(double posX)
    {
        this.posX = posX;
    }
    public double getPosX()
    {
        return posX;
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }
    public int getSpeed()
    {
        return speed;
    }

    public Dog(){};

    public Dog(String name, Color color)
    {
        dogName = name;
        this.color = color;
    }
}
