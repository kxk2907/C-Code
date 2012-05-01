/* 
 * FileName  : Judge.java 
 * 
 * Author    : Karthikeyan Karur Balu (kxk2907@rit.edu)
 * 
 * Version   : 1.1 
 *     		   
 * Revisions : Created - 11th Jan 2012
 * 
 * This Class implements the Judge specific application 
 * and it fetches the Job (timeStamp, problem) from the 
 * Queue in Contest and provides the result for each job
 *     
 */

import java.rmi.RemoteException;
import java.util.Scanner;

import common.IJudge;
import common.Job;
import common.IContest;

import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;

/**
 * Class Judge fetches job from the queue in Contest 
 * and provides the result. The result is a simulation from
 * the standard input and is provided randomly either "true"
 * or "false". RMI remote object from Contest is used to call 
 * the remote method judged.  
 * 
 * @author Karthikeyan Karur Balu
 * @version 11-Jan-2012
 * 
 */
public class Judge  {
	public String host;
	public Integer port;

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

	/**
	 * Main Reads input from the user to start the Judge
	 * 
	 * Input args are passed to the Main 
	 * <P>
	 * The command line arguments are:
	 * <BR><TT>args[0]</TT> = Registry Server's host
	 * <BR><TT>args[1]</TT> = Registry Server's port
	 * </p>
	 * 
	 * @param args Command line arguments. 
	 *             args[0] - hostname
	 *             args[1] - port number
	 *       
	 * @exception  RemoteException
	 *        Thrown if a remote error occurred.
	 * @exception  NotBoundException
	 *        Thrown if proxy lookup fails.
	 * @exception  InterruptedException
	 *        Thrown if the fetch method fails            
	 *             
	 */
	public static void main(String[] args) {
		//Verify Command line arguments.
		if(args.length != 2)
			usage();
		Judge judge = new Judge();
		judge.host = args[0];
		judge.port = Integer.parseInt(args[1]);
		
		//Declaring the remote methods and interfaces
		IJudge remoteContest = null;
		RegistryProxy proxy = null;
		
		//To read inputs - Either "true" or "false"
		Scanner sc = new Scanner(System.in);

		// Get a proxy for the Registry Server.
		try {
			proxy = new RegistryProxy (judge.host, judge.port);		
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		//Read input and fetch a job
		//Call the remoteContes.judged to submit the result
		while(sc.hasNextLine()) {
		    try {
				remoteContest = (IJudge) proxy.lookup(IContest.name);
				Job j = remoteContest.fetch();
				boolean judgement = Boolean.parseBoolean(sc.nextLine());
				System.out.println("Judging :: "+ j.getTeam() + " :: " + j.getProblem() + " " + j.getCalendar().getTime() + " " + judgement);
				j.setWhoAmI(judge.getClass().getName());
				remoteContest.judged(j, judgement);
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}
	
	/**
	 * Private static method to throw error and exit is the usage is wrong
	 */
	private static void usage() {
		System.out.println("Usage: java Contest <host> <port>");
		System.exit(1);
	}
}
