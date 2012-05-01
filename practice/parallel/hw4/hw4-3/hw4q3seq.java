/**
 * hw4q3seq.java
 *
 *
 * @version   $Id: hw4q3seq.java,v 1.6 
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
import edu.rit.util.Random;

/**
 * Sequential program for moore's algorithm for travelling salesperson
 * 
 * @author Karthikeyan Karur Balu
 *
 */
public class hw4q3seq {
	public static int seed;
	public static int n;

	/**
	 * Main program 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		seed = Integer.parseInt(args[0]);		//seed for random number
		n = Integer.parseInt(args[1]);			//total number of numbers
		Comm.init(args);
		Random random = Random.getInstance(seed);
		int x = 0, y = 0;
		long t1 = System.currentTimeMillis();
		//Moore's algorithm 
		for (int i = 0; i < n; i++) {
			int next = random.nextInt(8);
			if (next <= 3)
				x++;
			else if (next <= 5)
				x--;
			else if (next == 6)
				y++;
			else if (next == 7)
				y--;
		}
		//final position
		System.out.println(x + "," + y);
		long t2 = System.currentTimeMillis();
		System.out.println((t2 - t1) + " msec");
	}
}
