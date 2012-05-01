/* 
 * FileName  : Contest.java 
 * 
 * Author    : Karthikeyan Karur Balu (kxk2907@rit.edu)
 * 
 * Version   : 1.1 
 *     		   
 * Revisions : Created - 11th Jan 2012
 * 
 * This Class implements the Contest application. Teams
 * submit the job and it gets added to the queue of the 
 * application. Judge fetches the job from the Contest 
 * and it evaluates. Any event occurance is captured by
 * the event listener and sent across as a remote event
 * to the Viewer for display. 
 *     
 */

package contest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;

import common.IJudge;
import common.ICallback;
import common.IView;
import common.ISubmit;
import common.Job;
import common.ContestEvent;

import edu.rit.ds.Lease;
import edu.rit.ds.RemoteEventGenerator;
import edu.rit.ds.RemoteEventListener;
import edu.rit.ds.registry.NotBoundException;
import edu.rit.ds.registry.RegistryProxy;

/**
 * 
 * This Class implements the Contest application. Teams
 * submit the job and it gets added to the queue of the 
 * application. Judge fetches the job from the Contest 
 * and it evaluates. Any event occurance is captured by
 * the event listener and sent across as a remote event
 * to the Viewer for display. 
 * Implements ISubmit, ICallback, IJudge and IView
 * 
 * @author Karthikeyan Karur Balu
 * @version 11-Jan-2012
 *
 */
public class Contest implements ISubmit,ICallback,IJudge,IView {
	
	private Queue<Job> qJob = new LinkedList<Job>();
	
	private TreeMap<String,TreeMap<String,Detail>> mapper = 
			new TreeMap<String,TreeMap<String,Detail>>();
	
	private Calendar begin = Calendar.getInstance();
	private ArrayList<String> teamName;
	private ArrayList<String> problemName;
	private String host;
	private Integer port;
	
	private RemoteEventGenerator<ContestEvent> generator =
			new RemoteEventGenerator<ContestEvent>();
	
	/**
	 * constructor reads from the user to start the contest
	 * 
	 * Input args are passed to the Main 
	 * <P>
	 * The command line arguments are:
	 * <BR><TT>args[0]</TT> = Registry Server's host
	 * <BR><TT>args[1]</TT> = Registry Server's port
	 * </p>
	 * 
	 * Also it uses Buffered reader to get
	 * 1. Start time of the contest
	 * 2. List of team names
	 * 3. List of problem names
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
	 * @exception ParseException
	 * 		  Throws if there is an error in case of date processing             
	 *             
	 */
	public Contest(String[] args) {
		
		//Verify Command line arguments.
		if(args.length != 2) 
			usage();
		
		//host and port
		this.host = args[0];
		this.port = Integer.parseInt(args[1]);
		
		//Buffered Reader to get user inputs 
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {	
			//begin time stored
			String iTime = br.readLine();
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date date = sdf.parse(iTime);
			int td = this.begin.get(Calendar.DATE);
			int tm = this.begin.get(Calendar.MONTH);
			int ty = this.begin.get(Calendar.YEAR);
			this.begin.setTime(date);
			this.begin.set(Calendar.DATE, td);
			this.begin.set(Calendar.MONTH, tm);
			this.begin.set(Calendar.YEAR, ty);
			
			//team names stored
			this.teamName = new ArrayList<String>(Arrays.asList((br.readLine().split(" "))));
			
			//problem names stored
			this.problemName = new ArrayList<String>(Arrays.asList((br.readLine().split(" "))));
			
			//team map building for storing the results after every event
			for(int i=0;i<this.teamName.size();i++) {
				TreeMap<String,Detail> temp = new TreeMap<String,Detail>();
				for(int j=0;j<this.problemName.size();j++) {
					Detail local = new Detail(false,this.begin,0,false);
					temp.put(this.problemName.get(j), local);
				}
				//Storing the "Total" as well
				Detail tlocal = new Detail(false,this.begin,0,false);
				temp.put("total", tlocal);
				this.mapper.put(this.teamName.get(i), temp);
			}
						
			System.out.println("Start time :: " + this.begin.getTime());
			System.out.println("Teams      :: " + this.teamName);
			System.out.println("Problems   :: " + this.problemName);
			
			//Get a proxy for the Registry Server
			RegistryProxy proxy = new RegistryProxy (this.host, this.port);
			
			//Export the object
			UnicastRemoteObject.exportObject (this, 0);
			
			//Rebind to the Registry Server
			proxy.rebind(name, this);
			
			
		} catch(RemoteException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Remote method called from Team to submit the job
	 * 
	 * Job that was submitted by "Team" 
	 */
	public void submit(Job job) throws RemoteException {
		this.mapper.get(job.getTeam()).get(job.getProblem()).updateElapsed(job.getCalendar());
		this.mapper.get(job.getTeam()).get(job.getProblem()).updateAttempts();
		this.mapper.get(job.getTeam()).get(job.getProblem()).makeColorRed();
		this.qJob.add(job);
	}
		
	/**
	 * Remote method called from Team to submit the job 
	 * or from Judge to evaluate the job 
	 * 
	 * Job that was judged by Judge or Team(submission)
	 * ok Either true or false of the job
	 */
	public void judged(Job job, boolean ok) throws RemoteException {
		
		//if the method called from team
		if(job.getWhoAmI().equals("Team")) {
		
			//team always says "false"
			if(!ok){
				System.out.println("Adding "+job.getTeam() + "::" +job.getProblem() + " "+ job.getCalendar().getTime() +" to the queue !!");
				if(!this.mapper.get(job.getTeam()).get(job.getProblem()).correct)
					this.submit(job);
			}
			else {
				System.out.println("Never will happen !!");
			}
		}
		//if the method called from Judge
		else if(job.getWhoAmI().equals("Judge")){
			
			//if Judge says true change color and increments the counts accordingly
			if(ok) {
				if(!this.mapper.get(job.getTeam()).get(job.getProblem()).correct) {
					System.out.println("Updating result :: "+job.getTeam() + "::" +job.getProblem() +" TRUE !! ");
					this.mapper.get(job.getTeam()).get(job.getProblem()).correct = true;
					this.mapper.get(job.getTeam()).get(job.getProblem()).UnMakeColorRed();
					this.mapper.get(job.getTeam()).get("total").updateAttempts();
					this.mapper.get(job.getTeam()).get("total").updateElapsed(this.mapper.get(job.getTeam()).get(job.getProblem()).elapsed);
					this.mapper.get(job.getTeam()).get("total").totalAttempts += this.mapper.get(job.getTeam()).get(job.getProblem()).attempts;
				}
			}
			
			//if Judge says false change color and increments the counts accordingly
			else {
				if(!this.mapper.get(job.getTeam()).get(job.getProblem()).correct) {
					System.out.println("Updating result :: "+job.getTeam() + "::" +job.getProblem() +" FALSE !!");
					this.mapper.get(job.getTeam()).get(job.getProblem()).correct = false;
					//if another job present mark red
					Queue<Job> localJob = new LinkedList<Job>(this.qJob);
					Iterator<Job> browse = localJob.iterator();
					boolean insider = false;
					while(browse.hasNext()) {
						Job looker= (Job) browse.next();
						System.out.println("Processing for :: " + job.getTeam() + " :: "+ job.getProblem());
						if(looker.getTeam().equals(job.getTeam()) && looker.getProblem().equals(job.getProblem())) {
							this.mapper.get(job.getTeam()).get(job.getProblem()).makeColorRed();
							insider = true;
							System.out.println("Retaining the red color !! " + job.getTeam() + " :: " + job.getProblem() );
						}
					}
					if(!insider) {
						this.mapper.get(job.getTeam()).get(job.getProblem()).UnMakeColorRed();
						System.out.println("Changing to black color !! " + job.getTeam() + " :: " + job.getProblem() );
					}
				}
			}
		}
		
		//Thread for the Remote Event kick start
		new MyThread(this).start();
				
		//System.out.println("From Class :: " + job.getWhoAmI().split(" ")[1]);
		//System.out.println("Job added : "+job.getProblem() + " "+job.getTeam()+ " " +job.getCalendar().getTime());
	}
	
	/**
	 * 
	 * @return returns the Job for the Judge to do the processing
	 * 
	 * @throws RemoteException if a remote error occurs
	 * @throws InterruptedException if the Thread is interrupted
	 */
	public synchronized Job fetch() throws RemoteException, InterruptedException {
		while(qJob.isEmpty()) {
			System.out.println("Sleeping for the job to be present !!");
			Thread.sleep(1000);
		}
		if(!qJob.isEmpty()) {
			Job temp = this.qJob.poll();
			return temp;
		}
		return null;
	}
	
	/**
	 * Private static method to throw error and exit is the usage is wrong
	 */
	public static void usage() {
		System.out.println("Usage: java Contest <host> <port>");
		System.exit(1);
	}
		
	
	/**
	 * 
	 * Adds listener to and triggers the viewer
	 * 
	 * @return Lease return the lease
	 * 
	 * RemoteEventListener Trigger the listener and get the lease
	 *        Also add the listener to the generator
	 *        
	 * @throws RemoteException Throws a remote error
	 */
	public Lease addListener(RemoteEventListener<ContestEvent> listener)
			throws RemoteException {
		//all listener to a list, don't return
		System.out.println("Viewer added !!");
		return this.generator.addListener(listener);
	}


	/**
	 * 
	 * From the mapper build the html table to be sent to the Viewer
	 * 
	 * @return ContestEvent Returns a contest which is a String generated
	 * 
	 */
	public synchronized ContestEvent pull() throws RemoteException {
		String table =  new String();
		
		System.out.println("Listener responded, Pulling data for Viewer !! ");
		
		//arraylist of team names and sort them alphabetically
		ArrayList<String> local = new ArrayList<String>(teamName);
		Collections.sort(local);
		
		
        //The table rows are sorted by (higher) number of problems solved, 
		//then by (lower) total elapsed time, then by (lower) 
		//total number of attempts if any, and then by team name.		
		for(int i = 0;i<local.size();i++) {
			for(int j = 1;j<local.size();j++) {
				if(i != j) {
					String a = local.get(i);
					String b = local.get(j);
					if(mapper.get(a).get("total").attempts > mapper.get(b).get("total").attempts) {
						//do nothing
					}
					else if(mapper.get(a).get("total").attempts < mapper.get(b).get("total").attempts) {
						String swap = local.get(i);
						local.set(i, local.get(j));
						local.set(j, swap);
					}
					else if(mapper.get(a).get("total").attempts == mapper.get(b).get("total").attempts) {
						long aTime = mapper.get(a).get("total").elapsed.getTimeInMillis();
						long bTime = mapper.get(b).get("total").elapsed.getTimeInMillis();
						if(aTime > bTime) {
							String swap = local.get(i);
							local.set(i, local.get(j));
							local.set(j, swap);
						}
						else if (aTime < bTime) {
							//Do nothing
						}
						else if(aTime == bTime){
							if(mapper.get(a).get("total").totalAttempts > mapper.get(a).get("total").totalAttempts) {
								String swap = local.get(i);
								local.set(i, local.get(j));
								local.set(j, swap);
							}
							else if(mapper.get(a).get("total").totalAttempts < mapper.get(a).get("total").totalAttempts) {
								//Do nothing
							}
							else if(mapper.get(a).get("total").totalAttempts == mapper.get(a).get("total").totalAttempts) {
								//have to check !!
							}
						}
					}
				}
			}	
		}
		
		
		//Building the html table
		//1st row
		table += "<html><body>";
		table += "<table><tr>";
		table += "<th></th>";
		ArrayList<String> localProblem = new ArrayList<String>(problemName);
		Collections.sort(localProblem);
		for(int i=0;i<localProblem.size();i++)  {
			table += "<th>";
			table += localProblem.get(i);
			table += "</th>";
		}
		table += "<th></th>";
		table += "</tr>";
		
		//2nd row
		table += "<tr>";
		table += "<td>Team</td>";
		for(int i=0;i<localProblem.size();i++)  {
			table+= "<td>Elapsed&nbsp;&nbsp;&nbsp;n&nbsp;&nbsp;</td>";
		}
		table += "<td>Total&nbsp;&nbsp;&nbsp;&nbsp;n</td>"; 
		table += "</tr>";
		
		//3rd row and other  rows
		for(int i=0;i<local.size();i++) {
			table += "<tr>";
			table += "<td>";
			table += local.get(i);
			table += "</td>";
			TreeMap<String,Detail> tempProblem = mapper.get(local.get(i));
			Iterator<String> it = tempProblem.keySet().iterator();
			while(it.hasNext()) {
				Detail tempDetail = tempProblem.get(it.next());
					table += "<td>";
					if(tempDetail.colorRed)
						table += "<font color=\"red\">";
					String checker = timeDiff(tempDetail.elapsed,begin);
					if(checker.equals("00:00"))
						table += "&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-";
					else {
						table += checker;
						table += "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
						table += tempDetail.attempts;
					}
					if(tempDetail.colorRed)
						table += "<font color=\"red\">";
					table += "</td>";
			}	
			table += "</tr>";
		}
		
		//return the table that is built
		return new ContestEvent(table);
	}
	
	/**
	 * Method to calculate the time difference between 
	 * contest start time and the time the Job is submitted
	 * for evaluation
	 * 
	 * @param time time of submission
	 * @param begin start time of the contest
	 * @return String Returns (time-begin) 
	 */
	public String timeDiff(Calendar time, Calendar begin) {
		long diff = time.getTimeInMillis() - begin.getTimeInMillis();		
		long diffMinutes = diff / (60 * 1000);
		long diffHours = diff / (60 * 60 * 1000);
		String hrs = (String) (diffHours < 10 ? "0"+diffHours : ""+diffHours);
		String mins = (String) (diffMinutes < 10 ? "0"+diffMinutes : ""+diffMinutes);
		return(hrs+":"+mins);
	}
	
	/**
	 * 
	 * Class that extends Thread to report an Event to the 
	 * Event Listener
	 * 
	 * @author Karthikeyan Karur Balu
	 * @version 11-Jan-2012
	 *
	 */
	class MyThread extends Thread {
		Contest cont;
		
		/**
		 * Constructor to the class
		 * 
		 * @param cont Contest class variable 
		 */
		MyThread(Contest cont) {
			this.cont = cont;
		}
		
		/**
		 * Thread run() method 
		 * 
		 * @exception RemoteException
		 *            Throws any remote errors
		 */
		public synchronized void run() {
			try {
				cont.generator.reportEvent(cont.pull());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
}
