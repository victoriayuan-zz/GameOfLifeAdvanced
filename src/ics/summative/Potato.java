package ics.summative;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

class Potato
{
    static Image Potato1 = loadImage ("potato.png");
    static Image Potato2 = loadImage ("potato2.png");
    static Image Potato3 = loadImage ("potato3.png");

    int moves = 0;
    
    public Potato ()
    {
    }
    
    public void go() {
	moves++;
    }

    int random = (int) (Math.random () * 3) + 1;
    public Image getImage ()
    {
	if (random == 1)
	{
	    return Potato1;
	}
	else if (random == 2)
	{
	    return Potato2;
	}
	else
	{
	    return Potato3;
	}
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

