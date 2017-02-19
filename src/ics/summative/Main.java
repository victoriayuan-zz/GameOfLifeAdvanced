package ics.summative;

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
	//Button Declarations
    static Colony colony;
    static Timer t;
    static JButton showBtn, Pause, mushdoomBtn, poisonBtn, potatoBtn;
    static JButton eradicate, populate;
    static JSlider success;
    private int startx, starty, endx, endy;
    private int rate = 45; //Rate of success and eradication for slider
    private JPanel content;
    private JPanel mainscreen = new JPanel(); 
    boolean rect = false; //Boolean for drawing rectangles
    private String lifeList[] = {"All", "Human", "Zombie", "Zombie Hunter"};
    private JComboBox LifeJComboList = new JComboBox (lifeList);
    private String lifeForm = "All"; //Initial ComboBox value
    JButton normal; //One of buttons for menu

    //======================================================== constructor
    public Main ()
    {
	//*********************************************************************COMPONENTS FOR THE MAIN SCREEN
	//For the main menu
    mainscreen.setLayout(null);
	Color c1 = new Color (13,13,13);
	mainscreen.setBackground(c1);
	//Add a title
	Image title = loadImage("title.gif");
	JLabel thetitle = new JLabel (new ImageIcon(title));
	mainscreen.add(thetitle);
	thetitle.setBounds(90,0,600,200);
	//Select the mode
	normal = new JButton ("Normal Mode");
	mainscreen.add(normal); 
	normal.setBounds(50,350,250,150);
	normal.setBackground(Color.WHITE);
	normal.setFont(new Font("Arial", Font.PLAIN, 20));
	normal.addMouseListener(new MenuButtons ());
	normal.addActionListener(new Menu_Listener ());
	
	
	// 4... Set this window's attributes.
	setContentPane (mainscreen);
	pack ();
	setTitle ("So You Think You Can Survive");
	setSize (770, 640);
	setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo (null);           // Center window.
    }
    
    class MenuButtons implements MouseListener
    {
	public void mousePressed (MouseEvent evt)
	{
	}
	public void mouseClicked (MouseEvent evt)
	{
	}
	public void mouseReleased (MouseEvent evt)
	{
	}
	public void mouseEntered (MouseEvent evt)
	{        
	    normal.setBackground(Color.RED);
	}
	public void mouseExited (MouseEvent evt)
	{
	    normal.setBackground(Color.WHITE);
	}
    }    
    
    private class Menu_Listener implements ActionListener 
    {
	public void actionPerformed (ActionEvent e) 
	{
	    if (e.getSource () == normal) //If person clicked the normal button call that method 
	    {
		Normal();
	    }
	}
    }    
    
    public void Normal()
    {
	mainscreen.setVisible(false); //Hide the main screen
    
	// 1... Create/initialize components
	showBtn = new JButton ("Run");
	showBtn.setBackground(Color.RED);
	Pause = new JButton ("Pause");
	Pause.setBackground(Color.RED);
	eradicate = new JButton ("Eradicate");
	eradicate.setBackground(Color.RED);
	success = new JSlider (0, 100, 50);
	success.setBackground(Color.RED);
	populate = new JButton ("Populate");
	populate.setBackground(Color.RED);
	mushdoomBtn = new JButton ("Mushdoom");
	mushdoomBtn.setBackground(Color.RED);
	poisonBtn = new JButton ("Poisonous Mushroom");
	poisonBtn.setBackground(Color.RED);
	potatoBtn = new JButton ("Potatorite");
	potatoBtn.setBackground(Color.RED);
	LifeJComboList.setSelectedIndex (0);

	// Connect button to listener class
	showBtn.addActionListener (new ShowBtnListener ()); 
	Pause.addActionListener (new ShowBtnListener ());
	eradicate.addActionListener (new EradicateListener ());
	success.addChangeListener (new SuccessListener ());
	populate.addActionListener (new PopulateListener ());
	LifeJComboList.addActionListener (new LifeListener ());
	mushdoomBtn.addActionListener (new MushdoomListener ());
	poisonBtn.addActionListener (new PoisonListener ());
	potatoBtn.addActionListener (new PotatoListener ());

	//Set ticks to the JSlider
	success.setMajorTickSpacing (10);
	success.setPaintTicks (true);
	success.setPaintLabels (true);

	colony = new Colony (0.03); // create colony with 5% density
	Movement moveColony = new Movement (colony);
	t = new Timer (500, moveColony); // set up timer


	// Create content pane, set layout
	content = new JPanel ();        // Create a content pane
	
	content.setLayout (new BorderLayout ()); // Use BorderLayout for panel
	JPanel north = new JPanel ();
	north.setBackground(Color.BLACK);
	north.setLayout (new FlowLayout ()); // Use FlowLayout for input area
	JPanel south = new JPanel();
	south.setBackground(Color.BLACK);
	south.setLayout (new FlowLayout()); //For natural disaster buttons at the bottom
	JLabel info = new JLabel ("Natural Disasters");
	info.setForeground(Color.RED);
	

	DrawArea board = new DrawArea (790, 510);

	// Add the components to the input area (JPanel)
	north.add (showBtn);             
	north.add (Pause);
	north.add (eradicate);
	north.add (success);
	north.add (populate);
	north.add (LifeJComboList);
	south.add (info);
	south.add (mushdoomBtn);
	south.add (poisonBtn);
	south.add (potatoBtn);

	content.add (north, "North"); // Input area
	content.add (board, "Center"); // Output area
	content.add (south, "South"); //Add the natural disaster buttons 
	AddDestroy listener = new AddDestroy ();
	board.addMouseListener (listener); //Add a mouse listener
	board.addMouseMotionListener (listener);
	
	setContentPane (content); //Display the main game screen 
    
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


    class MushdoomListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    colony.mushdoom ();
	}
    }


    class PoisonListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    colony.poisonmushroom ();
	}
    }


    class PotatoListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    colony.potato ();
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


    class LifeListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    JComboBox cb = (JComboBox) e.getSource ();
	    lifeForm = (String) cb.getSelectedItem ();
	}
    }


    class EradicateListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    colony.eradicate (startx, starty, endx, endy, lifeForm, rate);
	    repaint();

	}
    }


    class PopulateListener implements ActionListener
    {
	public void actionPerformed (ActionEvent e)
	{
	    colony.populate (startx, starty, endx, endy, lifeForm, rate);
	    repaint();
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
	window.setResizable(false);
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