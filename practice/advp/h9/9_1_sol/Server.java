/**
 * Tree Set with iterator implementation
 *
 *
 * @version   $Id: Server.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
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
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class Server extends Canvas{												// Server class 
        public static void main(String args[]) throws Exception {		
                String clientSentence, capitalizedSent;
                ServerSocket welcomesocket = new ServerSocket(4444);			// TCP Listen socket
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
                Server tc = new Server("server");					//game board
                JFrame jf = new JFrame();
                jf.setTitle("Tic Tac Toe Server");
                jf.addWindowListener(new WindowAdapter() {
        	            public void windowClosing(WindowEvent e) {
        	                System.exit(0);
        	            }
        	        });
        		jf.add(tc);
        		jf.pack();
        		jf.setVisible(true);
                while(true) {
                        Socket connectionSocket = welcomesocket.accept();				//accepting the socket connection
                        BufferedReader fromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream())); // iunputs and outputs
                        DataOutputStream toClient = new DataOutputStream(connectionSocket.getOutputStream());
                        clientSentence = fromClient.readLine();       //read from the socket 
                        tc.setEnabled(false);
                        String s1[] = clientSentence.split(",");
                        if(Running == false)						
            			{
            				tc.clear();
            				Running = true;
            				tc.repaint();
            			}
            			if(Turn) {	// Player X's turn
            					Turn = false;
            					capitalizedSent = "APPROVED";
            					Grid[Integer.parseInt(s1[0])][Integer.parseInt(s1[1])] = 1;
            					Turn = false;
            			}	
            			else {			// Player 0's turn							
            					capitalizedSent = "APPROVED";
            					Grid[Integer.parseInt(s1[0])][Integer.parseInt(s1[1])] = 2;
            					Turn = true;
            			}
            			Result = tc.gameStatus();								// check the game status 
            			if(Result != 0) {						
            				Running  = false;
            				capitalizedSent = "GAME OVER";
            			}
            			tc.repaint();  
            			tc.setEnabled(true);
            			while(!Server.readyToSend) {							// if readyToSend then send
            				Thread.sleep(1000);
            			}
            			tc.setEnabled(false);
            			capitalizedSent = "";
            			capitalizedSent = SEND_OUT + '\n';						//Server contacxting the client
            			toClient.writeBytes(capitalizedSent);
                        capitalizedSent = clientSentence.toUpperCase() + '\n';
                        toClient.writeBytes(capitalizedSent);
                        readyToSend = false;
                }
        }
       
        
      
        private static final long serialVersionUID = 1L;
    	public String WhoAmI;
    	public static String SEND_OUT;
    	public static boolean readyToSend = false;
    	public static boolean Turn = true;							 
    	int rand =((int)(Math.random() * 10)) % 2;				
    	public static int[][] Grid = new int[3][3];				 
    	public static boolean Running = true;					
    	public static int Result;										
    	public Server(String WhoAmI) {
    		this.WhoAmI = WhoAmI;
    		setPreferredSize(new Dimension(256,256));					// Game Board dimensions
    		setBackground(Color.WHITE);
    		addMouseListener(new MouseListener() {						// Mouse click listener 
    			public void mouseClicked(MouseEvent e) {
    				int x = e.getPoint().y ;
    				int y = e.getPoint().x ;
    				if(x > 50 && x < 200 && y > 50 && y < 200 ) {	
    					if(Running == false)						
    					{
    						clear();
    						Running = true;
    						repaint();
    						return;
    					}
    					if(Grid[x/50 - 1][y/50 -1] != 0) {			
    						return;
    					}
    					if(Turn) {									 // X's turn 
    						int xval = x/50 -1;
    						int yval = y/50 -1;
    						SEND_OUT = ""+xval+","+yval+",1";
    						Grid[x/50 -1][y/50 -1] = 1;
    						Turn = false;
    						readyToSend = true;
    					}	
    					else {											// O's turn
    						int xval = x/50 -1;
    						int yval = y/50 -1;
    						SEND_OUT = ""+xval+","+yval+",2";
    						Grid[x/50 -1][y/50 -1] = 2;
    						Turn = true;
    						readyToSend = true;
    					}
    					Result = gameStatus();						
    					if(Result != 0) {							
    						Running = false;			
    					}
    					repaint();									
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
    	public void paint(Graphics g) {						//FOr painting the board
    		g.setColor(Color.BLACK);
    		for(int x = 1;x < 3;x++)							// Horizontal lines
    			g.drawLine(x*50+50,50,x*50+50,50+50*3);
    		for(int y = 1;y < 3;y++)							// Vertical lines
    			g.drawLine(50,y*50+50,50+50*3,y*50+50);
    		for(int x = 0;x < 3;x++) {							
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
    		if(Running)	{													//Print the string
    			if(Turn)
    				g.drawString("Player : X - Client",10,20);			
    			else
    				g.drawString("Player : O - Server",10,20);
    		}
    		else {									
    			if(Result == 1)									
    				g.drawString("Player X - Client won!",10,20);
    			if(Result == 2)
    				g.drawString("Player O - Server won!",10,20);
    			if(Result == 3) 
    				g.drawString("Tie !!",10,20);
    			g.drawString("Click to begin a new game !!",10,40);	
    		}
    	}
    	public int gameStatus() {						
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
    	public void clear()									//Clear the game board
    	{
    		for(int y = 0;y < 3;y++)
    			for(int x = 0;x < 3;x++)
    				Grid[x][y] = 0;
    			Turn = true;
    	}
}


