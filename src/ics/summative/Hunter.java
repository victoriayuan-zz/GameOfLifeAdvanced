package ics.summative;

import java.awt.Image;

class Hunter extends Lifeform
{
    static Image hunter = loadImage ("Hunter.png");

    public Hunter (int row, int col)
    {
	r = row;
	c = col;
    }


    public Image getImage ()
    {
	return hunter;
    }
}
