package ics.summative;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

class Mushdoom
{
    static Image Mushdoom1 = loadImage ("mushdoom.png");
    static Image Mushdoom2 = loadImage ("mushdoom2.png");
    static Image Fire = loadImage ("fire.png");
    int moves;

    public Mushdoom ()
    {
	moves = 0;
    }


    public void go ()
    {
	moves++;
    }


    public Image getImage ()
    {
	if (moves < 4)
	    return Mushdoom1;
	if (moves < 7)
	    return Mushdoom2;
	return Fire;
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

