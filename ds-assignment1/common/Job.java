/* 
 * FileName  : Job.java 
 * 
 * Author    : Karthikeyan Karur Balu (kxk2907@rit.edu)
 * 
 * Version   : 1.1 
 *     		   
 * Revisions : Created - 11th Jan 2012
 * 
 * This Class implements the Job specific application 
 * and it contains details about a Job such as the team 
 * submitted, problem and the time stamp associated with it
 *     
 */
package common;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Job contains details about any Job submitted to the Contest
 * and the details include TeamName, problem and time stamp.
 * Jobs are used by Contest, Judge and Team applications for 
 * processing the requests
 * 
 * @author Karthikeyan Karur Balu
 * @version 11-Jan-2012
 * 
 */
@SuppressWarnings("serial")
public class Job implements Serializable{
	private String team;
	private String problem;
	private Calendar time;
	private String whoAmI;
	
	/**
	 * Constructor for the Job
	 * 
	 * @param team	Name of the team
	 * @param problem Name of the problem
	 * @param time Calendar time associated with the Job
	 */
	public Job(String team,String problem, Calendar time){
		this.team = team;
		this.problem = problem;
		this.time = time;
	}
	
	/**
	 * Returns the team name
	 * 
	 * @return team 
	 */
	public String getTeam(){
		return this.team;
	}
	
	/**
	 * Returns the problem name
	 * 
	 * @return problem
	 */
	public String getProblem(){
		return this.problem;
	}
	
	
	/**
	 * Returns the time stamp (Calender)
	 * 
	 * @return time
	 */
	public Calendar getCalendar() {
		return this.time;
	}
	
	/**
	 * To Set which class calls the constructor of Job 
	 * (Judge) or (Team) ??
	 * 
	 * @param whoAmI String variable to save the class name
	 */
	public void setWhoAmI(String whoAmI) {
		this.whoAmI = whoAmI;
	}
	
	/**
	 * Return the name of the class which created the Job
	 * 
	 * @return whoAmI 
	 */
	public String getWhoAmI() {
		return this.whoAmI;
	}
}
