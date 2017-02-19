package ics.summative;

import java.awt.Image;

class Zombie extends Lifeform
{
    static Image image = loadImage ("zomie.png"); //image of zombie

    public Zombie (int row, int col)
    {
	r = row;
	c = col;
    }


    public Image getImage ()
    {
	return image;
    }
}
