/**
 * Tree Set with iterator implementation
 *
 *
 * @version   $Id: UDPServer.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
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
import java.net.*;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class UDPServer extends Canvas {								//UDPServer running at port 4445
	public static void main(String args[]) throws Exception
	{
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
		DatagramSocket serverSocket = new DatagramSocket(4445);
		byte[] receiveData = new byte[100];
		byte[] sendData = new byte[100];
		UDPServer tc = new UDPServer("server");
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
		while(true)
		{
			DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
			serverSocket.receive(receivePacket);
			String sentence = new String(receivePacket.getData());
			InetAddress IPAddress =	receivePacket.getAddress();
			int port = receivePacket.getPort();	
			tc.setEnabled(false);
			String s2 = sentence.substring(0, 4);
			String s1[] = s2.split(",");
			String capitalizedSentence = sentence.toUpperCase();
			if(Running == false)						// If the game is over
   			{
   				tc.clear();
   				Running = true;
   				tc.repaint();
   			}
   			if(Turn) {	// Player X's turn
   					Turn = false;
   					capitalizedSentence = "APPROVED";
   					Grid[Integer.parseInt(s1[0])][Integer.parseInt(s1[1])] = 1;
   					Turn = false;
   			}	
   			else {										// Player O's turn
   				capitalizedSentence = "APPROVED";
   					Grid[Integer.parseInt(s1[0])][Integer.parseInt(s1[1])] = 2;
   					Turn = true;
   			}
   			Result = tc.gameStatus();						// Game status 
   			if(Result != 0) {							// Game result
   				Running  = false;
   				capitalizedSentence = "GAME OVER";
   			}
   			tc.repaint();  
   			tc.setEnabled(true);
			
   			while(!UDPServer.readyToSend) {
				Thread.sleep(2500);
			}
			tc.setEnabled(false);
			capitalizedSentence = "";
			capitalizedSentence = SEND_OUT + '\n';
			sendData = capitalizedSentence.getBytes();
			DatagramPacket sendPacket =	new DatagramPacket(sendData,sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
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
 	public UDPServer(String WhoAmI) {
 		this.WhoAmI = WhoAmI;
 		setPreferredSize(new Dimension(256,256));
 		setBackground(Color.WHITE);
 		addMouseListener(new MouseListener() {				
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
 					if(Turn) {									
 						int xval = x/50 -1;
 						int yval = y/50 -1;
 						SEND_OUT = ""+xval+","+yval+",1";
 						Grid[x/50 -1][y/50 -1] = 1;
 						Turn = false;
 						readyToSend = true;
 					}	
 					else {										
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
 	public void paint(Graphics g) {
 		g.setColor(Color.BLACK);
 		for(int x = 1;x < 3;x++)							
 			g.drawLine(x*50+50,50,x*50+50,50+50*3);
 		for(int y = 1;y < 3;y++)							
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
 		if(Running)	{										
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
 	public void clear()						
 	{
 		for(int y = 0;y < 3;y++)
 			for(int x = 0;x < 3;x++)
 				Grid[x][y] = 0;
 			Turn = true;
 	}
}
