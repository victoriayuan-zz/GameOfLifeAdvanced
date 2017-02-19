package ics.summative;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

class Food
{
    static Image food = loadImage ("burger.gif");

    public Food ()
    {
    }


    public Image getImage ()
    {
	return food;
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
