//TO POPULATE AND ERADICATE, DRAW RECTANGLE FROM TOP LEFT CORNER TO BOTTOM RIGHT CORNER AND CLICK BUTTON


import java.awt.*;

import javax.imageio.*; // allows image loading

import java.io.*; // allows file access

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.event.*;  // Needed for ActionListener

import sun.audio.*;

public class Main extends JFrame
{
    static Colony colony;
    static Timer t;
    static JButton showBtn, Pause;
    static JButton eradicate, populate;
    static JSlider success;
    private int startx, starty, endx, endy;
    private int rate = 45;
    JPanel content;
    boolean rect = false;

    //======================================================== constructor
    public Main ()
    {
	// 1... Create/initialize components
	showBtn = new JButton ("Run");
	Pause = new JButton ("Pause");
	eradicate = new JButton ("Eradicate");
	success = new JSlider (0, 100, 50);
	populate = new JButton ("Populate");

	showBtn.addActionListener (new ShowBtnListener ()); // Connect button to listener class
	Pause.addActionListener (new ShowBtnListener ());
	eradicate.addActionListener (new EradicateListener ());
	success.addChangeListener (new SuccessListener ());
	populate.addActionListener (new PopulateListener ());

	success.setMajorTickSpacing (10);
	success.setPaintTicks (true);
	success.setPaintLabels (true);

	colony = new Colony (0.05); // create colony with 60% density
	Movement moveColony = new Movement (colony); // ActionListener
	t = new Timer (500, moveColony); // set up timer


	// 2... Create content pane, set layout
	content = new JPanel ();        // Create a content pane
	content.setLayout (new BorderLayout ()); // Use BorderLayout for panel
	JPanel north = new JPanel ();
	north.setLayout (new FlowLayout ()); // Use FlowLayout for input area

	DrawArea board = new DrawArea (790, 510);

	// 3... Add the components to the input area.

	north.add (showBtn);             // Add button
	north.add (Pause);
	north.add (eradicate);
	north.add (success);
	north.add (populate);

	content.add (north, "North"); // Input area
	content.add (board, "South"); // Output area
	AddDestroy listener = new AddDestroy ();
	board.addMouseListener (listener); //Add a mouse listener
	board.addMouseMotionListener (listener);

	// 4... Set this window's attributes.
	setContentPane (content);
	pack ();
	setTitle ("So You Think You Can Survive");
	setSize (770, 600);
	setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo (null);           // Center window.
    }


    class AddDestroy implements MouseMotionListener, MouseListener
    {
	public void mousePressed (MouseEvent evt)
	{
	    //Get x and y coordinates when clicked
	    startx = evt.getX ();
	    starty = evt.getY ();
	}
	public void mouseMoved (MouseEvent evt)
	{
	}
	public void mouseDragged (MouseEvent evt)
	{
	    //Get x and y coordinates when mouse is released
	    endx = evt.getX ();
	    endy = evt.getY ();
	    int temp;
	    //If the drag starts from right or from below, swap
	    if (starty > endy)
	    {
		temp = starty;
		starty = endy;
		endy = temp;
	    }
	    if (startx > endx)
	    {
		temp = startx;
		startx = endx;
		endx = temp;
	    }
	    rect = true;
	    content.repaint ();
	}
	public void mouseClicked (MouseEvent evt)
	{
	}
	public void mouseReleased (MouseEvent evt)
	{
	}
	public void mouseEntered (MouseEvent evt)
	{
	}
	public void mouseExited (MouseEvent evt)
	{
	}
    }


    class SuccessListener implements ChangeListener
    {
	//@Override
	public void stateChanged (ChangeEvent e)
	{
	    // TODO Auto-generated method stub
	    success = (JSlider) e.getSource ();
	    if (!success.getValueIsAdjusting ())
	    {
		rate = (int) success.getValue ();
	    }
	}
    }


    class EradicateListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    colony.eradicate (startx, starty, endx, endy, rate);

	}
    }


    class PopulateListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    if (rate > 50)
	    {
		colony.populate (startx, starty, endx, endy);
	    }
	}
    }


    class ShowBtnListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    if (e.getSource () == showBtn)
	    {
		// Code to add Timer

		t.start (); // start simulation
	    }
	    if (e.getSource () == Pause)
	    {
		try
		{
		    t.stop (); //Pause the timer
		}
		catch (Exception e2)
		{
		}
	    }
	}
    }


    class DrawArea extends JPanel
    {
	public DrawArea (int width, int height)
	{
	    this.setPreferredSize (new Dimension (width, height)); // size
	}

	public void paintComponent (Graphics g)
	{
	    colony.show (g);

	    if (rect == true)
	    {
		g.setColor (Color.RED);
		g.drawRect (startx, starty, endx - startx, endy - starty);
	    }
	}
    }


    class Movement implements ActionListener
    {
	private Colony colony;

	public Movement (Colony col)
	{
	    colony = col;
	}

	public void actionPerformed (ActionEvent event)
	{
	    colony.advance (); // update life status
	    repaint ();
	}
    }


    public int getRate ()
    {
	return rate;
    }


    //======================================================== method main
    public static void main (String[] args)
    {
	Main window = new Main ();
	window.setVisible (true);
    }
}

class Colony
{
    private Object grid[] [];
    static int right_BOUND = 25, bot_BOUND = 16; //Bounds of the array and playing field
    int foodcount = 0; //Add more food to array every 30 moves
    //private Main a = new Main();

    public Colony (double density)
    {
	grid = new Object [17] [26];

	for (int row = 0 ; row < grid.length ; row++) //Populate the humans
	    for (int col = 0 ; col < grid [0].length ; col++)
	    {
		if (Math.random () < density)
		    grid [row] [col] = new Humans (row, col);
	    }

	for (int row = 0 ; row < grid.length ; row++) //Populate the rocks
	    for (int col = 0 ; col < grid [0].length ; col++)
	    {
		if (Math.random () < 0.04)
		    grid [row] [col] = new Obstacle ();
	    }

	for (int row = 0 ; row < grid.length ; row++) //Populate the zomvies
	    for (int col = 0 ; col < grid [0].length ; col++)
	    {
		if (Math.random () < 0.02)
		    grid [row] [col] = new Zombie (row, col);
	    }

	for (int row = 0 ; row < grid.length ; row++) //place the food
	    for (int col = 0 ; col < grid [0].length ; col++)
	    {
		if (Math.random () < 0.03)
		    grid [row] [col] = new Food ();
	    }

	// grid [0] [0] = new Zombie (0, 0); //Zombie
	// grid [0] [5] = new Obstacle (); //Obstacle
	// grid [1] [5] = new Obstacle ();
	// grid [2] [5] = new Obstacle ();
	// grid [3] [5] = new Obstacle ();
	// grid [3] [4] = new Obstacle ();
	// grid [3] [3] = new Obstacle ();
	// grid [4] [3] = new Obstacle ();
	// grid [4] [3] = new Obstacle ();
	// grid [0] [20] = new Obstacle ();
	// grid [1] [20] = new Obstacle ();
	// grid [2] [20] = new Obstacle ();
	// grid [3] [20] = new Obstacle ();
	// grid [4] [20] = new Obstacle ();
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


    public void show (Graphics g)
    {
	//g.setColor(Color.BLUE);
	//g.fillRect(0,0,26*29+2,17*30+2);
	Image background = loadImage ("background.jpg");
	g.drawImage (background, 2, 2, null);

	for (int row = 0 ; row < grid.length ; row++)
	    for (int col = 0 ; col < grid [0].length ; col++)
	    {
		if (grid [row] [col] != null && grid [row] [col] instanceof Lifeform) // life form present
		{
		    g.drawImage (((Lifeform) grid [row] [col]).getImage (), col * 29 + 2, row * 30 + 2, null);
		}
		else if (grid [row] [col] instanceof Obstacle)
		{
		    g.drawImage (((Obstacle) grid [row] [col]).getImage (), col * 29 + 2, row * 30 + 2, null);
		}
		else if (grid [row] [col] instanceof Food)
		{
		    g.drawImage (((Food) grid [row] [col]).getImage (), col * 29 + 2, row * 30 + 2, null);
		}
		else
		{ // no life form
		    //g.setColor (Color.BLACK);
		    //g.fillRect (col * 29 + 2, row * 30 + 2, 29, 30); // draw life form (or erase previous one)
		}
	    }
    }


    public void advance ()  //Update the life status after each generation and save it to a new array
    {
	//*******************************************************MOVEMENT FOR THE LIFEFORMS******************************************************************************
	for (int row = 0 ; row < grid.length ; row++)
	    for (int col = 0 ; col < grid [0].length ; col++)
		if (grid [row] [col] instanceof Lifeform) //If object is a lifeform
		    if (((Lifeform) grid [row] [col]).moved == false) //If there is a lifeform there and hasnt been moved
		    {
			((Lifeform) grid [row] [col]).moved = true; //let program know that lifeform has been moved so its not moved again during current move

			int ORIGINAL_r = ((Lifeform) grid [row] [col]).r; //Save the original position of the human in case it cant move to its new space
			int ORIGINAL_c = ((Lifeform) grid [row] [col]).c;

			((Lifeform) grid [row] [col]).move (); //Move the human around


			if (grid [((Lifeform) grid [row] [col]).r] [((Lifeform) grid [row] [col]).c] == null || (grid [((Lifeform) grid [row] [col]).r] [((Lifeform) grid [row] [col]).c] instanceof Food && grid [row] [col] instanceof Humans)) //only move the human to the new space if it is empty or theres food there
			{
			    if (grid [((Lifeform) grid [row] [col]).r] [((Lifeform) grid [row] [col]).c] instanceof Food)
				((Lifeform) grid [row] [col]).moves = 0; //reset the number of moves of the person if they eat food

			    grid [((Lifeform) grid [row] [col]).r] [((Lifeform) grid [row] [col]).c] = grid [row] [col]; //Move the human to its new space
			    grid [ORIGINAL_r] [ORIGINAL_c] = null; //Make the spot it just left empty
			}
			else //If the space it was supposed to go to is not empty, make it stay
			{
			    ((Lifeform) grid [row] [col]).c = ORIGINAL_c; //Assign original position to the human object
			    ((Lifeform) grid [row] [col]).r = ORIGINAL_r;
			}
		    }
	//********************************************ACTIONS THAT OCCUR AFTER ALL LIFEFORMS HAVE MOVED*****************************************************

	for (int row = 0 ; row < grid.length ; row++) //Reset moved variable of all the lifeforms after done one move
	    for (int col = 0 ; col < grid [0].length ; col++)
		if (grid [row] [col] instanceof Lifeform)
		    if (grid [row] [col] != null)
		    {
			((Lifeform) grid [row] [col]).moved = false;

			//***********************************************************INFECTION FOR ZOMBIES*******************************************************
			if (grid [row] [col] instanceof Zombie)
			    if (((Lifeform) grid [row] [col]).moves > 1) //To prevent newly turned zombies from infecting others
			    {
				if (row > 0)
				    if (grid [row - 1] [col] instanceof Humans)
					grid [row - 1] [col] = new Zombie (row - 1, col);

				if (row < bot_BOUND)
				    if (grid [row + 1] [col] instanceof Humans)
					grid [row + 1] [col] = new Zombie (row + 1, col);

				if (col > 0)
				    if (grid [row] [col - 1] instanceof Humans)
					grid [row] [col - 1] = new Zombie (row, col - 1);

				if (col < right_BOUND)
				    if (grid [row] [col + 1] instanceof Humans)
					grid [row] [col + 1] = new Zombie (row, col + 1);
			    }

			//********************************************************REPRODUCTION**********************************************************************
			if (grid [row] [col] instanceof Humans)
			{
			    if (((Humans) grid [row] [col]).gender == true && ((Humans) grid [row] [col]).baby == false) //if the lifeform is a male (and babys cannot reproduce)
			    {
				if (row > 1) //Check bounds
				    if (grid [row - 1] [col] != null && grid [row - 1] [col] instanceof Humans) //If space above male is not just empty
					if (((Humans) grid [row - 1] [col]).gender == false && ((Humans) grid [row] [col]).baby == false) //If there is a female above the male and its not a baby
					    if (grid [row - 2] [col] == null) //If theres space above the female put a baby there
						if (Math.random () < 0.5) //Half chance to create a baby
						{
						    grid [row - 2] [col] = new Humans (row - 2, col);
						    ((Humans) grid [row - 2] [col]).baby = true; //The new human is a baby
						}

				if (row < bot_BOUND - 1) //Check bounds
				    if (grid [row + 1] [col] != null && grid [row + 1] [col] instanceof Humans) //If space below male is not just empty
					if (((Humans) grid [row + 1] [col]).gender == false && ((Humans) grid [row] [col]).baby == false) //If there is a female below the male and its not a baby
					    if (grid [row + 2] [col] == null) //If theres space below the female put a baby there
						if (Math.random () < 0.5) //Half chance to create a baby
						{
						    grid [row + 2] [col] = new Humans (row + 2, col);
						    ((Humans) grid [row + 2] [col]).baby = true; //The new human is a baby
						}
			    }
			}

			//************************************************DEATH***********************************************************************
			if (grid [row] [col] instanceof Humans)
			    if (((Lifeform) grid [row] [col]).moves > 70) //If the human has moved more than 70 moves without food
				grid [row] [col] = null; //Make the lifeform disappear


		    } //End of if for main for loop

	//************************************************ADD IN MORE FOOD*****************************************
	foodcount++; //increment food counter
	if (foodcount == 30)
	{
	    for (int row = 0 ; row < grid.length ; row++) //place the food
		for (int col = 0 ; col < grid [0].length ; col++)
		    if (grid [row] [col] == null)
			if (Math.random () < 0.03)
			    grid [row] [col] = new Food ();

	    foodcount = 0; //reset the food counter
	}

    } //End of advance method


    //Eradicates an area
    public void eradicate (int startx, int starty, int endx, int endy, int rate)
    {
	int startcol = ((startx - 2) - (startx - 2) % 29) / 29;
	int startrow = ((starty - 2) - (starty - 2) % 30) / 30;
	int endcol = ((endx - 2) - (endx - 2) % 29) / 29;
	int endrow = ((endy - 2) - (endx - 2) % 30) / 30;

	if (startcol == endcol & startrow == endrow)
	{
	    try
	    {
		grid [startrow] [endcol] = null;
	    }
	    catch (Exception ed)
	    {
	    }
	}

	for (int i = startrow ; i <= endrow ; i++)
	{
	    for (int j = startcol ; j <= endcol ; j++)
	    {
		int random = (int) (Math.random () * 50);
		try
		{
		    if (random < rate && grid [i] [j] != null)
		    {
			grid [i] [j] = null;
		    }
		}
		catch (Exception e)
		{

		}
	    }
	}
    }


    //Populates an area from mouselistener
    public void populate (int startx, int starty, int endx, int endy)
    {
	int startcol = ((startx - 2) - (startx - 2) % 29) / 29;
	int startrow = ((starty - 2) - (starty - 2) % 30) / 30;
	int endcol = ((endx - 2) - (endx - 2) % 29) / 29;
	int endrow = ((endy - 2) - (endx - 2) % 30) / 30;

	for (int i = startrow ; i <= endrow ; i++)
	{
	    for (int j = startcol ; j <= endcol ; j++)
	    {
		int random = (int) (Math.random () * 10);
		try
		{
		    if (random < 7 && grid [i] [j] == null)
		    {
			grid [i] [j] = null;
		    }
		}
		catch (Exception e)
		{

		}
	    }
	}
    }
}

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

class Obstacle
{
    static Image Stone = loadImage ("stone.png");

    public Obstacle ()
    {
    }


    public Image getImage ()
    {
	return Stone;
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

class Humans extends Lifeform
{
    static Image male = loadImage ("male.png"); //Image of human
    static Image female = loadImage ("female.png");
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
	    return male;
	return female;

    }
}
