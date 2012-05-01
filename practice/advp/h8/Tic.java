/**
 * TIC TAC TOE in java
 *
 *
 * @version   $Id: Tic.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) && Nipun Sud 
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/27 12:58:00  kxk2907
 *      Initial revision
 *
 */


import java.awt.Button;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.UIManager;

/*
 * Main method which calls the board class
 * Create a frame and add the Board to it.
 */
public class Tic {	
	public static void main(String args[]) {
		String lookAndFeel;
		 lookAndFeel=UIManager.getCrossPlatformLookAndFeelClassName();
	        if ( args.length == 1 )
	        {
	                if ( args[0].equals("motif") )
	                   lookAndFeel =
	                        "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
	                if ( args[0].equals("metal") )
	                   lookAndFeel =
	                        "javax.swing.plaf.metal.MetalLookAndFeel";
	                else if ( args[0].equals("system") )
	                   lookAndFeel=
	                        UIManager.getSystemLookAndFeelClassName() ;
	        }
	        try {
	            UIManager.setLookAndFeel( lookAndFeel);
	        } catch (Exception e) { }
		JFrame board = new JFrame();
		board.setTitle("Tic Tac Toe");
		board.addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent e) {
	                System.exit(0);
	            }
	        });
		Board gb = new Board();
		board.add(gb);
		board.pack();
		board.setVisible(true);
	}
}

/*
 * Board class defines board's method and properties
 */
class Board extends Canvas {
	public static boolean Turn ;							//Turn which users ?? 
	int rand =((int)(Math.random() * 10)) % 2;				//Starting the game
	public static int[][] Grid = new int[3][3];				// 3 X 3 matrix 
	public static boolean Running = true;					// To check if the game is runnig
	public int Result;										// Rsult of the game
	public Board() {
		setPreferredSize(new Dimension(256,256));
		setBackground(Color.WHITE);
		addMouseListener(new MouseListener() {				// Mouse listneer event
			public void mouseClicked(MouseEvent e) {
				int x = e.getPoint().y ;
				int y = e.getPoint().x ;
				if(x > 50 && x < 200 && y > 50 && y < 200 ) {	// Grid limitation
					if(Running == false)						// If the game is over
					{
						clear();
						Running = true;
						repaint();
						return;
					}
					if(Grid[x/50 - 1][y/50 -1] != 0) {			// if already contains a value
						return;
					}
					if(Turn) {									// Player X's turn
						Grid[x/50 -1][y/50 -1] = 1;
						Turn = false;
					}	
					else {										// Player O's turn
						Grid[x/50 -1][y/50 -1] = 2;
						Turn = true;
					}
					Result = gameStatus();						// Game status 
					if(Result != 0) {							// Game result
						Running = false;
						new Button("Click");
					}
					repaint();									// Repaint everytime you finish
				}
			}
			public void mouseEntered(MouseEvent arg0) {			}
			public void mouseExited(MouseEvent arg0) {			}
			public void mousePressed(MouseEvent arg0) {			}
			public void mouseReleased(MouseEvent arg0) {			}
		});
		rand = ((int)(Math.random() * 10)) % 2;
		clear();
		Running = true;
	}
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		for(int x = 1;x < 3;x++)							// Draw the horizontal lines
			g.drawLine(x*50+50,50,x*50+50,50+50*3);
		for(int y = 1;y < 3;y++)							// Drwa the vertical lines
			g.drawLine(50,y*50+50,50+50*3,y*50+50);
		for(int x = 0;x < 3;x++) {							// Read the matrix and draw the grid marks
			for(int y = 0;y < 3;y++) {
				if(Grid[x][y] == 1)	{
					g.setColor(Color.BLUE);
					g.drawString("X", 75+y*50,75+x*50);
				}
				if(Grid[x][y] == 2) {
					g.setColor(Color.RED);
					g.drawString("O", 75+y*50,75+x*50);
				}
			}
		}
		g.setColor(Color.BLACK);
		if(Running)	{										// If the game is running
			if(Turn)
				g.drawString("Player : X ",10,20);			
			else
				g.drawString("Player : O ",10,20);
		}
		else {									
			if(Result == 1)									//Validating the result !!
				g.drawString("Player X won!",10,20);
			if(Result == 2)
				g.drawString("Player O won!",10,20);
			if(Result == 3) 
				g.drawString("Tie !!",10,20);
			g.drawString("Click to begin a new game !!",10,40);	
		}
	}
	public int gameStatus() {						// Check for player won or not !!
		for(int x = 0;x<3;x++) {
			if((Grid[x][0] == Grid[x][1]) && (Grid[x][1] == Grid[x][2]))
				if(Grid[x][0] != 0 && Grid[x][1] != 0 && Grid[x][2] != 0)
					return Grid[x][0];
		}
		for(int y=0;y<3;y++) {
			if((Grid[0][y] == Grid[1][y]) && (Grid[1][y] == Grid[2][y])) 
				if(Grid[0][y] != 0 && Grid[1][y] != 0 && Grid[2][y] != 0)
					return Grid[0][y];
		}
		if(Grid[0][0] == Grid[1][1] && Grid[1][1] == Grid[2][2]) 
			if(Grid[0][0] != 0 && Grid[1][1] != 0 && Grid[2][2] != 0)
				return Grid[0][0];
		if(Grid[2][0] == Grid[1][1] && Grid[1][1] == Grid[0][2])
			if(Grid[2][0] != 0 && Grid[1][1] != 0 && Grid[0][2] != 0)
				return Grid[0][0];
		for(int x = 0;x<3;x++) {
			for(int y = 0;y<3;y++) {
				if(Grid[x][y] == 0)
					return 0;
			}
		}
		return 3;		
	}
	public void clear()						// Clear the game
	{
		for(int y = 0;y < 3;y++)
			for(int x = 0;x < 3;x++)
				Grid[x][y] = 0;
		if(rand == 0)
			Turn = true;
		else
			Turn = false;
	}
}

