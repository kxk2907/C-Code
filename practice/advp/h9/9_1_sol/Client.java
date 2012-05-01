/**
 * Tree Set with iterator implementation
 *
 *
 * @version   $Id: Client.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) && Nipun Sud 
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/27 12:58:00  kxk2907
 *      Initial revision
 *
 */

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class Client extends Canvas{				//Client Class
        public static void main(String args[]) throws IOException, InterruptedException {
        	String lookAndFeel;
   		 lookAndFeel=UIManager.getCrossPlatformLookAndFeelClassName();			//Look and feel
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
        	Client tc = new Client("client");
            JFrame jf = new JFrame();
            jf.setTitle("Tic Tac Toe Client");
            jf.addWindowListener(new WindowAdapter() {
    	            public void windowClosing(WindowEvent e) {
    	                System.exit(0);
    	            }
    	        });
    		jf.add(tc);
    		jf.pack();
    		jf.setVisible(true);	
         while(true) {
        	    tc.setEnabled(true);
                String sentence, modifiedSent;
                Socket clientSocket = new Socket("localhost",4444);			//Listening at the port 4444
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while(!Client.readyToSend) {
                	Thread.sleep(1000);
                }
                sentence = SEND_OUT;										//Client makes the initial contact
                outToServer.writeBytes(sentence + '\n');
                tc.setEnabled(false);
                modifiedSent = inFromServer.readLine();
                String s1[] = modifiedSent.split(",");
                if(Running == false)					
    			{
    				tc.clear();
    				Running = true;
    				tc.repaint();
    			}
    			if(Turn) {	
    					Grid[Integer.parseInt(s1[0])][Integer.parseInt(s1[1])] = 1;
    					Turn = false;
    			}	
    			else {										
    					Grid[Integer.parseInt(s1[0])][Integer.parseInt(s1[1])] = 2;
    					Turn = true;
    			}
    			Result = tc.gameStatus();						
    			if(Result != 0) {						
    				Running  = false;
    			}
    			tc.repaint();  										//Repaint after making the changes to the board
                System.out.println("Output : " + modifiedSent);
                clientSocket.close();
                readyToSend = false;
         }
       }
        private static final long serialVersionUID = 1L;
    	public String WhoAmI;
    	public static String SEND_OUT;
    	public static boolean readyToSend;
    	public static boolean Turn = true;							//Turn which users ?? 
    	int rand =((int)(Math.random() * 10)) % 2;				//Starting the game
    	public static int[][] Grid = new int[3][3];				// 3 X 3 matrix 
    	public static boolean Running = true;					// To check if the game is runnig
    	public static int Result;										// Rsult of the game
    	public Client(String WhoAmI) {
    		this.WhoAmI = WhoAmI;
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
    						int xval = x/50 -1;
    						int yval = y/50 -1;
    						SEND_OUT = ""+xval+","+yval+",1";
    						Grid[x/50 -1][y/50 -1] = 1;
    						Turn = false;
    						readyToSend = true;
    					}	
    					else {										// Player O's turn
    						int xval = x/50 -1;
    						int yval = y/50 -1;
    						SEND_OUT = ""+xval+","+yval+",2";
    						Grid[x/50 -1][y/50 -1] = 2;
    						Turn = true;
    						readyToSend = true;
    					}
    					Result = gameStatus();						// Game status 
    					if(Result != 0) {							// Game result
    						Running = false;	
    						readyToSend = true;
    					}
    					repaint();									// Repaint everytime you finish
    				}
    			}
    			public void mouseEntered(MouseEvent arg0) {			}
    			public void mouseExited(MouseEvent arg0) {			}
    			public void mousePressed(MouseEvent arg0) {			}
    			public void mouseReleased(MouseEvent arg0) {			}
    		});
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
    				g.drawString("Player : X - Client",10,20);			
    			else
    				g.drawString("Player : O - Server",10,20);
    		}
    		else {									
    			if(Result == 1)									//Validating the result !!
    				g.drawString("Player X - Client won!",10,20);
    			if(Result == 2)
    				g.drawString("Player O - Server won!",10,20);
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
    			Turn = true;
    	}
}

