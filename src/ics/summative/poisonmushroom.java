package ics.summative;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

class poison //Only used to determine if a spot is poisonous
{
    public poison ()
    {
    }
}


class poisonmushroom
{
    static Image mushroom = loadImage ("derp.png");
    int moves = 0;
    boolean poison_check = false; //Whether or not poison has shot out of the mushroom

    public poisonmushroom ()
    {
    }


    public Image getImage ()
    {
	return mushroom;
    }


    public void go ()
    {
	moves++;
    }


    public static Image loadImage (String name)  //Loads image from file
    {
	Image img = null;
	try
	{
	    img = ImageIO.read (new File (name));
	}
	catch (IOException e)
	{
	}
	return img;
    }
}
