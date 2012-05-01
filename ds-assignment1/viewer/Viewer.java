/* 
 * FileName  : Viewer.java 
 * 
 * Author    : Karthikeyan Karur Balu (kxk2907@rit.edu)
 * 
 * Version   : 1.1 
 *     		   
 * Revisions : Created - 11th Jan 2012
 * 
 * This Class implements the Viewer specific application 
 * and it implements a RemoteEventListener to get event 
 * detail updates from Contest. After any new event such 
 * as adding Job to evealuate or an evaluate event by the 
 * Judge the viewer gets updated. It uses the Push method 
 * from the Contest to update the Viewer. Jframe is used 
 * to display and JTextPane is used to display the String
 *     
 */

package viewer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import javax.swing.JFrame;
import javax.swing.JTextPane;

import common.ContestEvent;
import common.IContest;
import common.IView;

import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;


/**
 * This Class implements the Viewer specific application 
 * and it implements a RemoteEventListener to get event 
 * detail updates from Contest. After any new event such 
 * as adding Job to evaluate or an evaluate event by the 
 * Judge occurs the viewer gets updated. It uses the Push 
 * method from the Contest to update the Viewer. Jframe is 
 * used to display and JTextPane is used to display the 
 * String.
 * 
 * 
 * @author Karthikeyan Karur Balu
 * @version 11-Jan-2012
 * 
 */
public class Viewer {

	private String host;
	private Integer port;
	private Integer width;
	private Integer height;
	private String title;

	/**
	 * Constructor for the viewer 
	 * 
	 * @param host host name for the proxy
	 * @param port port for the proxy
	 * @param width width of the viewer window
	 * @param height height of the viewer window
	 * @param title title of the viewer window
     *
	 */
	public Viewer(String host, Integer port, Integer width, Integer height, String title) {
		this.host = host;
		this.port = port;
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	/**
	 *  Remote Event Listener to listen to any remote Event
	 *  
	 */
	public RemoteEventListener<ContestEvent> 
	  listener = new RemoteEventListener<ContestEvent>() {
		public void report(long theSequenceNumber, ContestEvent theEvent) {
			resultTable.report(theEvent);
		}
	};

	private ResultTable resultTable = new ResultTable();

	/**
	 * 
	 * Class used to display table whenever an event occurs 
	 * and the event captured by listener
	 * 	  
	 * @author Karthikeyan Karur Balu
	 * @version 11-Jan-2012
	 *
	 */
	@SuppressWarnings("serial")
	public class ResultTable extends JTextPane {
		/**
		 * Constructor calling the super class JTextPane
		 */
		ResultTable() {
			super();
		}

		/**
		 * Event listener call the report method and
		 * passes ContestEvent which is a String to be 
		 * displayed. This String is set to the text for
		 * the TextPane.
		 * 
		 * @param theEvent ContestEvent (which is a string that is 
		 * 					built for the JTextPane that contain detail
		 * 					about the events (job) or evaluation)
		 */
		public void report(ContestEvent theEvent) {
			this.setContentType("text/html");
			System.out.println("Updating Viewer !!");
			this.setText(theEvent.table);
		}
	}

	/**
	 * Main Reads input from the user to start the Judge
	 * 
	 * Input args are passed to the Main 
	 * <P>
	 * The command line arguments are:
	 * <BR><TT>args[0]</TT> = Registry Server's host
	 * <BR><TT>args[1]</TT> = Registry Server's port
	 * <BR><TT>args[2]</TT> = Width of the viewer window
	 * <BR><TT>args[3]</TT> = Height of the viewer window
	 * <BR><TT>args[4]</TT> = Title of the viewer window
	 * </p>
	 * 
	 * @param args Command line arguments. 
	 *             args[0] - hostname
	 *             args[1] - port number
	 *             args[2] - width
	 *             args[3] - height
	 *             args[4] - title
	 *       
	 * @exception  RemoteException
	 *        Thrown if a remote error occurred.
	 * @exception  NotBoundException
	 *        Thrown if proxy lookup fails.
	 *             
	 */
	public static void main(String[] args) {
		
		//Verify Command line arguments.
		if (args.length != 5)
			usage();

		Viewer viewer = new Viewer(args[0],Integer.parseInt(args[1]),
				Integer.parseInt(args[2]),Integer.parseInt(args[3]),args[4]);

		//Declaring the remote methods and interfaces
		IView remoteContest = null;
		RegistryProxy proxy = null;

		try {
			// Export the listener
			UnicastRemoteObject.exportObject(viewer.listener, 0);

			// Get a proxy for the Registry Server.
			proxy = new RegistryProxy(viewer.host, viewer.port);
			remoteContest = (IView) proxy.lookup(IContest.name);
			
			//adding the listener there
			remoteContest.addListener(viewer.listener);

			//building the frame for display
			JFrame frame = new JFrame(viewer.title);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(viewer.width + 50, viewer.height + 50);
			frame.setLocation(1000, 250);
			frame.setVisible(true);
			frame.getContentPane().add(viewer.resultTable);

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Private static method to throw error and exit is the usage is wrong
	 */
	private static void usage() {
		System.out
				.println("Usage: java Contest <host> <port> <width> <height> <title>");
		System.exit(1);
	}

}