package ics.summative;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

	abstract class Lifeform
	{
	    static int right_BOUND = 25, bot_BOUND = 16; //Bounds of the array and playing field
	    boolean moved = false; //Whether lifeform has moved during one generation already (make sure to not move it several times)
	    int c; // column that the human object is in
	    int r; //row that the human object is in
	    int moves = 0; //number of moves of person (after 10 moves a baby turns into an adult)

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


	    abstract Image getImage ();

	    public void move ()  //makes all the humans move around one spot
	    {
		double chance = Math.random ();

		if (chance >= 0 && chance < 0.25) //Move the human left or right (half chance)
		{
		    if (c > 0)
			c--; //Move human to left
		}
		else if (chance >= 0.25 && chance < 0.5) //Move human to right
		{
		    if (c < right_BOUND)
			c++;
		}
		else if (chance >= 0.5 && chance < 0.75) //Move the human down or up
		{
		    if (r < bot_BOUND)
			r++; //Move the human down
		}
		else if (chance >= 0.75 && chance < 1) //Move the human up
		{
		    if (r > 0)
			r--;
		}
		moves++; //Increment number of moves
	    }
	}
