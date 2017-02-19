package ics.summative;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Colony {
	   Object grid[] [];
	    static int right_BOUND = 25, bot_BOUND = 16; //Bounds of the array and playing field
	    int foodcount = 0; //Add more food to array every 30 moves

	    //Variables for poisonmushroom
	    int x = 0; //radius for the poisonous cloud
	    

	    public Colony (double density)
	    {
		grid = new Object [17] [26];

		for (int row = 0 ; row < grid.length ; row++) //Populate the humans
		    for (int col = 0 ; col < grid [0].length ; col++)
		    {
			if (Math.random () < density)
			    grid [row] [col] = new Humans (row, col);
		    }

		for (int row = 0 ; row < grid.length ; row++) //place the food
		    for (int col = 0 ; col < grid [0].length ; col++)
		    {
			if (Math.random () < 0.03)
			    grid [row] [col] = new Food ();
		    }

		grid [6] [6] = new Zombie (6,6); //Zombie
		grid [10] [10] = new Hunter (10, 10); //Zombie hunter
		grid [15] [15] = new Hunter (15, 15);
		grid [11] [5] = new Hunter (11, 5);
		grid [5] [5] = new Obstacle ();
		grid [6] [5] = new Obstacle ();
		grid [7] [5] = new Obstacle ();
		grid [12] [10] = new Obstacle ();
		grid [12] [11] = new Obstacle ();
		grid [12] [12] = new Obstacle (); 

		grid [7] [20] = new Obstacle ();
		grid [8] [20] = new Obstacle ();
		grid [9] [20] = new Obstacle ();
		grid [3] [17] = new Obstacle ();
		grid [4] [17] = new Obstacle ();
		grid [5] [17] = new poisonmushroom ();
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
			else if (grid [row] [col] instanceof Mushdoom)
			{
			    g.drawImage (((Mushdoom) grid [row] [col]).getImage (), col * 29 + 2, row * 30 + 2, null);
			}
			else if (grid [row] [col] instanceof poisonmushroom)
			{
			    g.drawImage (((poisonmushroom) grid [row] [col]).getImage (), col * 29 + 2, row * 30 + 2, null);
			}
			else if (grid [row] [col] instanceof poison) //draw poisonous cloud
			{
			    Color c1 = new Color (210, 0, 210);

			    g.setColor (c1);
			    g.fillRect (col * 29 + 2, row * 30 + 2, 29, 30);
			}
			else if (grid [row] [col] instanceof Potato)
			{
			    g.drawImage (((Potato) grid [row] [col]).getImage (), col * 29 + 2, row * 30 + 2, null);
			}
			/*else if (grid[row][col] instanceof Player)
			{
			    g.setColor(Color.BLUE);
			    g.fillRect (col * 29 + 2, row * 30 + 2, 29, 30);
			     
			}*/
		    }
	    }


	    public void advance ()  //Update the life status after each generation and save it to a new array
	    {
		//*******************************************************MOVEMENT FOR THE LIFEFORMS******************************************************************************
		for (int row = 0 ; row < grid.length ; row++)
		{
		    for (int col = 0 ; col < grid [0].length ; col++)
		    {
			if (grid [row] [col] instanceof Lifeform) //If object is a lifeform
			{
			    if (((Lifeform) grid [row] [col]).moved == false) //If there is a lifeform there and hasnt been moved
			    {
				((Lifeform) grid [row] [col]).moved = true; //let program know that lifeform has been moved so its not moved again during current move

				int ORIGINAL_r = ((Lifeform) grid [row] [col]).r; //Save the original position of the human in case it cant move to its new space
				int ORIGINAL_c = ((Lifeform) grid [row] [col]).c;

				((Lifeform) grid [row] [col]).move (); //Move the human around


				if (grid [((Lifeform) grid [row] [col]).r] [((Lifeform) grid [row] [col]).c] == null || (grid [((Lifeform) grid [row] [col]).r] [((Lifeform) grid [row] [col]).c] instanceof Food)) //only move the human to the new space if it is empty or theres food there
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
			}
		    }
		}
		//********************************************ACTIONS THAT OCCUR AFTER ALL LIFEFORMS HAVE MOVED*****************************************************

		for (int row = 0 ; row < grid.length ; row++)
		    //Reset moved variable of all the lifeforms after done one move
		    for (int col = 0 ; col < grid [0].length ; col++)
		    {
			if (grid [row] [col] instanceof Lifeform)
			{
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


				//**************************************Death for zombies***************************************************
				if (grid [row] [col] instanceof Hunter) //If there is a hunter in the selected space
				{
				    if (row > 0)
					if (grid [row - 1] [col] instanceof Zombie) //If there is a zombie touching the hunter
					    grid [row - 1] [col] = null; //Kill the zombie

				    if (row < bot_BOUND)
					if (grid [row + 1] [col] instanceof Zombie)
					    grid [row + 1] [col] = null;

				    if (col > 0)
					if (grid [row] [col - 1] instanceof Zombie)
					    grid [row] [col - 1] = null;

				    if (col < right_BOUND)
					if (grid [row] [col + 1] instanceof Zombie)
					    grid [row] [col + 1] = null;

				}

				//************************************************DEATH FOR HUMANS***********************************************************************
				if (grid [row] [col] instanceof Humans)
				    if (((Lifeform) grid [row] [col]).moves > 70) //If the human has moved more than 70 moves without food
					grid [row] [col] = null; //Make the lifeform disappear


			    } ///if not null
			} //if not life form


			//************************************************MUSHDOOM EXPLOSION***********************************************************************
			if (grid [row] [col] instanceof Mushdoom)
			{
			    ((Mushdoom) (grid [row] [col])).go ();
			    if ((((Mushdoom) grid [row] [col]).moves > 8))
			    {
				grid [row] [col] = null;
				try
				{
				    grid [row + 1] [col] = null;
				    grid [row + 2] [col] = null;
				    grid [row] [col + 1] = null;
				    grid [row + 1] [col + 1] = null;
				    grid [row + 2] [col + 1] = null;

				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
			    }
			} //end of checking for mushdoom
		   // }

	 //************************************************POTATORITE***********************************************************************
			if (grid [row] [col] instanceof Potato)
			{
			    ((Potato) (grid [row] [col])).go ();
			    if ((((Potato) grid [row] [col]).moves > 5))
			    {
				grid [row] [col] = null;
			    }
			} //end of checking for potatorite
		    }

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


		//****************************************************POISONOUS MUSHROOM**********************************
		for (int row = 0 ; row < grid.length ; row++)
		    for (int col = 0 ; col < grid [0].length ; col++)
			if (grid [row] [col] instanceof poisonmushroom)
			{
			    if (((poisonmushroom) grid [row] [col]).moves < 20) //If the number of moves of mushroom is less than 20
			    {
				if (((poisonmushroom) grid [row] [col]).poison_check == false) //If there is no poison there, make there be poison
				{
				    int x = 1; //radius for poisonous cloud

				    if (row > 0)
					grid [row - x] [col] = new poison (); //Make the spots around the mushroom poisonous

				    if (row < bot_BOUND)
					grid [row + x] [col] = new poison ();

				    if (col > 0)
					grid [row] [col - x] = new poison ();

				    if (col < right_BOUND)
					grid [row] [col + x] = new poison ();

				    ((poisonmushroom) grid [row] [col]).poison_check = true; //change the switch variable to true now so next time the program can rid of poison
				}
				else if (((poisonmushroom) grid [row] [col]).poison_check == true)
				{
				    int x = 1; //radius for poison

				    if (row > 0)
					grid [row - x] [col] = null; //Make the spots around the mushroom poisonous

				    if (row < bot_BOUND)
					grid [row + x] [col] = null;

				    if (col > 0)
					grid [row] [col - x] = null;

				    if (col < right_BOUND)
					grid [row] [col + x] = null;

				    ((poisonmushroom) grid [row] [col]).poison_check = false; //change the switch variable to true now
				}
				((poisonmushroom) grid [row] [col]).go (); //increment the number of moves
			    }
			    else if (((poisonmushroom) grid [row] [col]).moves >= 20) //If its been more than 20 moves, rid of the poisonous mushroom
			    {
				grid [row] [col] = null;

				int x = 1;

				if (row > 0)
				    grid [row - x] [col] = null;

				if (row < bot_BOUND)
				    grid [row + x] [col] = null;

				if (col > 0)
				    grid [row] [col - x] = null;

				if (col < right_BOUND)
				    grid [row] [col + x] = null;
			    }
			}
	    } //End of advance method


	    public void mushdoom ()
	    {
		int row = (int) (Math.random () * 16);
		int col = (int) (Math.random () * 25);
		grid [row] [col] = new Mushdoom ();
	    } //end of mushdoom method;


	    public void poisonmushroom ()
	    {
		int row = (int) (Math.random () * 17);
		int col = (int) (Math.random () * 26);
		grid [row] [col] = new poisonmushroom ();
	    } //end of poisonousmushroom method;


	    public void potato ()
	    {
		for (int i = 0 ; i < 20 ; i++)
		{
		    int row = (int) (Math.random () * 17);
		    int col = (int) (Math.random () * 26);
		    grid [row] [col] = new Potato ();
		}
	    } //end of potato method;


	    //Eradicates an area
	    public void eradicate (int startx, int starty, int endx, int endy, String lifeform, int rate)
	    {
		int startcol = ((startx - 2) - (startx - 2) % 29) / 29;
		int startrow = ((starty - 2) - (starty - 2) % 30) / 30;
		int endcol = ((endx - 2) - (endx - 2) % 29) / 29;
		int endrow = ((endy - 2) - (endx - 2) % 30) / 30;

		if (startcol == endcol & startrow == endrow)
		{
		    grid [startrow] [endcol] = null;
		}


		for (int i = startrow ; i <= endrow ; i++)
		{
		    for (int j = startcol ; j <= endcol ; j++)
		    {

			int random = (int) (Math.random () * 100);
			try
			{
			    if (grid [i] [j] instanceof poisonmushroom ||
				    grid [i] [j] instanceof Mushdoom || 
				    grid [i] [j] instanceof Potato)
			    {

			    }
			    //Selective eradication
			    else if (lifeform.equals("Human") && 
			    grid [i] [j] instanceof Humans && random<rate) {
				grid [i] [j] = null;
			    }
			    
			    else if (lifeform.equals("Zombie Hunter") && 
			    grid [i] [j] instanceof Hunter && random<rate) {
				grid [i] [j] = null;
			    }
			    
			    else if (lifeform.equals("Zombie") && 
			    grid [i] [j] instanceof Zombie && random<rate) {
				grid [i] [j] = null;
			    }
			    
			    else if (lifeform.equals("All")&& random<rate) {
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
	    public void populate (int startx, int starty, int endx, int endy, String lifeForm, int rate)
	    {
		int startcol = ((startx - 2) - (startx - 2) % 29) / 29;
		int startrow = ((starty - 2) - (starty - 2) % 30) / 30;
		int endcol = ((endx - 2) - (endx - 2) % 29) / 29;
		int endrow = ((endy - 2) - (endx - 2) % 30) / 30;

		for (int i = startrow ; i <= endrow ; i++)
		{
		    for (int j = startcol ; j <= endcol ; j++)
		    {
			int random = (int) (Math.random () * 100);
			try
			{
			    if (random < rate && grid [i] [j] == null)
			    {
				if (lifeForm.equals ("Zombie"))
				{
				    grid [i] [j] = new Zombie (i, j);
				}
				else if (lifeForm.equals ("Zombie Hunter"))
				{
				    grid [i] [j] = new Hunter (i, j);
				}
				else if (lifeForm.equals ("Human"))
				{
				    grid [i] [j] = new Humans (i, j);
				}
				//Randomly generates a lifeform
				else if (lifeForm.equals ("All")) {
				    int ran = (int)(Math.random()*3);
				    if (ran==0) {
					grid [i] [j] = new Humans (i, j);
				    }
				    else if (ran==1) {
					grid [i] [j] = new Zombie (i, j);
				    }
				    else if (ran==2) {
					grid [i] [j] = new Hunter (i, j);
				    }
				}
			    }
			}
			catch (Exception e)
			{

			}
		    }
		}
	    }
	}
