package ics.summative;

import java.awt.Image;

class Humans extends Lifeform
{
    static Image male = loadImage ("male.png"); //Image of human
    static Image malestarve = loadImage ("malestarve.png");
    static Image female = loadImage ("female.png");
    static Image femalestarve = loadImage ("femalestarve.png");
    static Image Baby = loadImage ("baby.png");

    boolean gender; //Gender of the human (true means male, false means female)
    boolean baby; //whether the human is a baby


    public Humans (int row, int col)
    {
	r = row;
	c = col;

	if (Math.random () <= 0.5)
	    gender = true; //Male
	else
	    gender = false; //Female
    }


    public Image getImage ()
    {
	if (moves >= 10)
	    baby = false;
	if (baby == true)
	    return Baby;
	if (gender == true)
	{
	    if (moves > 60) //If moves is greater than 60, show a human thats about to die
	    {
		return malestarve;
	    }
	    return male;   //if moves are less than 60, show a regular picture
	}
	else
	{
	    if (moves > 60)
	    {
		return femalestarve;
	    }
	    return female;
	}
    }
}
