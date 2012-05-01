/* 
 * FileName  : Detail.java 
 * 
 * Author    : Karthikeyan Karur Balu (kxk2907@rit.edu)
 * 
 * Version   : 1.1 
 *     		   
 * Revisions : Created - 11th Jan 2012
 * 
 * This Class implements the Details about the problems
 * with respect to team names such as attempts, correct
 * and the time it takes to solve. Color coding is used 
 * to show if the problem submitted is not evaluated.
 *     
 */

package contest;

import java.util.Calendar;

/**
 * 
 * This Class implements the Details about the problems
 * with respect to team names such as attempts, correct
 * and the time it takes to solve. Color coding is used 
 * to show if the problem submitted is not evaluated.
 * 
 * @author Karthikeyan Karur Balu
 * @version 11-Jan-2012
 */
public class Detail {
	boolean judged = false;
	Calendar elapsed = null;
	Integer attempts = 0;
	boolean correct = false;
	boolean colorRed = false;
	Integer totalAttempts = 0;
	
	/**
	 * Constructor for Detail
	 * 
	 * @param judged To say if the problem is judged or not
	 * @param elapsed Submission time stamp
	 * @param attempts # of attempts taken to completion
	 * @param correct Says if the problem is correct or not 
	 * 
	 */
	public Detail(boolean judged,Calendar elapsed, Integer attempts,boolean correct){
		this.judged = judged;
		this.elapsed = elapsed;
		this.attempts = attempts;
		this.correct = correct;
	}
	
	/**
	 * Change the time stamp to keep account of new submission
	 * 
	 * @param newElapsed New time stamp
	 */
	public void updateElapsed(Calendar newElapsed) {
		this.elapsed = newElapsed;
	}
	
	/**
	 * Update the attempts
	 * 
	 * attempts++
	 * 
	 */
	public void updateAttempts() {
		this.attempts++;
	}
	
	/**
	 * Change to red color
	 */
	public void makeColorRed() {
		this.colorRed = true;
	}
	
	/**
	 * Change to black color
	 */
	public void UnMakeColorRed() {
		this.colorRed = false;
	}
}
