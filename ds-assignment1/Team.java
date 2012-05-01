/* 
 * FileName  : Team.java 
 * 
 * Author    : Karthikeyan Karur Balu (kxk2907@rit.edu)
 * 
 * Version   : 1.1 
 *     		   
 * Revisions : Created - 11th Jan 2012
 * 
 * This Class implements the Team specific application 
 * and it submits the Job (timeStamp, problem) by each 
 * team and the output will be judged
 *     
 */

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import common.ICallback;
import common.Job;
import common.IContest;

import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;

/**
 * Class Team runs for each team specified in the Contest
 * and it extends Java RMI Remote through interface ICallback
 * The Remote object from the Registry with the String "Context"
 * is used to execute the methods such as judged to submit the 
 * job to the queue maintained by the Contest. 
 * 
 * @author Karthikeyan Karur Balu
 * @version 11-Jan-2012
 */
public class Team implements ICallback {

	private String host;
	private Integer port;
	private String teamName;
	
	/**
	 * 
	 * Judged method is from ICallback 
	 * but never implemented locally but a Remote Object 
	 * calls the remote method
	 * 
	 * @param job Job to be executed
	 * @param ok  Result of the job
	 * @throws RemoteException 
	 *         Remote error thrown
	 */
	public void judged(Job job, boolean ok) throws RemoteException {
		System.out.println("Entering local judged !!");
	}
	
	//Constructor implementatio
	/**
	 * Constructor of the Team Class
	 * 
	 * Input args are passed to the constructor 
	 * <P>
	 * The command line arguments are:
	 * <BR><TT>args[0]</TT> = Registry Server's host
	 * <BR><TT>args[1]</TT> = Registry Server's port
	 * <BR><TT>args[1]</TT> = Team's Name
	 * </p>
	 * 
	 * @param args Command line arguments. 
	 *             args[0] - hostname
	 *             args[1] - port number
	 *             args[2] - teamName
	 * @exception  RemoteException
	 *        Thrown if a remote error occurred.
	 * @exception  NotBoundException
	 *        Thrown if proxy lookup fails.
	 *             
	 */
	public Team(String[] args) {
		
		//Verify Command line arguments.
		if(args.length != 3)
			usage();
		System.out.println("Initializing Team ...");
		this.host = args[0];
		this.port = Integer.parseInt(args[1]);
		this.teamName = args[2];
		System.out.println("Name : "+teamName + " @ " + host + ":" + port);
		
		//Declaring the remote methods and interfaces
		ICallback remoteContest = null;
		RegistryProxy proxy = null;
		
		//To read inputs - timestamp, problem name
		Scanner sc = new Scanner(System.in);
				
		// Get a proxy for the Registry Server.
		try {
			proxy = new RegistryProxy (this.host, this.port);		
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		//Read input and declare Job
		//Send the job to the Queue in Contest through remote method
		//Do a proxy lookup every time before the job is submitted
		//jobTimeNormalizer to store the time of submission
		while(sc.hasNextLine()) {
			String[] sub = sc.nextLine().split(" ");	
			Job j = new Job(this.teamName,sub[0],this.jobTimeNormalizer(sub[1])); 
			j.setWhoAmI(this.getClass().getName());
			
			System.out.println("Job submitted : "+j.getTeam()+ " :: " +j.getProblem() + " "+j.getCalendar());	
			try {
			    remoteContest = (ICallback) proxy.lookup(IContest.name);
				remoteContest.judged(j,false);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Method to store the calendar 
	 * 
	 * @param input Input from the user 
	 * 
	 * @return A calendar time is set based on the input from the user
	 * 
	 * @exception ParseException
	 * 			  If the input is not in specified format
	 */
	private Calendar jobTimeNormalizer(String input) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = sdf.parse(input);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int td = c.get(Calendar.DATE);
		int tm = c.get(Calendar.MONTH);
		int ty = c.get(Calendar.YEAR);
		
		c.setTime(date);
		c.set(Calendar.DATE, td);
		c.set(Calendar.MONTH, tm);
		c.set(Calendar.YEAR, ty);
		
		return c;
	}	

	/**
	 * Private static method to throw error and exit is the usage is wrong
	 */
	private static void usage() {
		System.out.println("Usage: java Contest <host> <port> <TeamName>");
		System.exit(1);
	}
}
