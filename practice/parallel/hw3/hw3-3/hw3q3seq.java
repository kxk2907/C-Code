/**
 * hw3q3seq.java
 *
 *
 * @version   $Id: hw3q3seq.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */

import java.io.IOException;
import edu.rit.pj.Comm;

/**
 * 
 * @author Karthikeyan Karur Balu
 * 
 *         To prove collatz conjecture
 * 
 */
public class hw3q3seq {
	public static long number = 0;		//// number of iterations
	public static void main(String[] args) throws IOException {
		number = Long.parseLong(args[0]);
		long maxiter = 0;				//maxiter contains the result
		long maxnumber = 1;				//maxnumber for which it is max
		long t1 = System.currentTimeMillis();
		Comm.init(args); 
		for(int i = 1;i<=number;i++) {
			long x = i;
			long localmaxiter = 0;
			while( x > 1) {
				if((x & 1) == 0)
					x = x / 2;
				else 
					x = 3 * x + 1;
				localmaxiter ++;
			}
			if(localmaxiter > maxiter) {
				maxiter = localmaxiter;
				maxnumber = i;
			}
		}
		long t2 = System.currentTimeMillis();
		System.out.println(maxnumber);
		System.out.println((t2-t1) + " msec");
	}
}
